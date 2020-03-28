package com.github.leftisttachyon.epsilon;

import com.github.leftisttachyon.epsilon.blob.BlobModel;
import com.github.leftisttachyon.epsilon.blob.ContainerModel;
import com.github.leftisttachyon.epsilon.data.GuildConfig;
import com.github.leftisttachyon.epsilon.data.UserData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * A service that supplies commands with guild and user info.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
@Slf4j
public class GuildInfoService implements Closeable {
    /**
     * The only instance to be created
     */
    private static final GuildInfoService SINGLETON = new GuildInfoService();
    /**
     * A {@link HashMap} that stores data related to each server.
     */
    private HashMap<Long, GuildData> guildData;

    /**
     * Creates a new {@link GuildInfoService}.
     */
    private GuildInfoService() {
        guildData = new HashMap<>();
    }

    /**
     * Gets the singleton instance of this class.
     *
     * @return the singleton instance of this class
     */
    public static GuildInfoService getInstance() {
        return SINGLETON;
    }

    /**
     * Gets the {@link GuildConfig} object associated with the guild with the given ID.
     *
     * @param guildID           the {@link Guild} to get the configuration for
     * @param createIfNotExists whether to create an entry for the given guild
     * @return the {@link GuildConfig} associated with the given guild, or {@code null} if none could be found and
     * {@code createIfNotExists} is false
     */
    public GuildConfig getGuildConfig(long guildID, boolean createIfNotExists) {
        if (guildData.containsKey(guildID)) {
            return guildData.get(guildID).config;
        } else if (createIfNotExists) {
            GuildData blank = new GuildData(guildID, false);
            guildData.put(guildID, blank);

            return blank.config;
        } else return null;
    }

    /**
     * Gets the {@link UserData} object associated with the given guild and user.
     *
     * @param guildID           the {@link Guild} to look for
     * @param user              the {@link User} to look for
     * @param createIfNotExists whether to create a corresponding object if it cannot be found
     * @return the found or created object or {@link null}
     */
    public UserData getUserData(long guildID, User user, boolean createIfNotExists) {
        log.trace("Looking for guild #{}", guildID);

        if (guildData.containsKey(guildID)) {
            log.trace("Found it! Passing off to guild object.");

            return guildData.get(guildID).getUserData(user, createIfNotExists);
        } else if (createIfNotExists) {
            long userID = user.getIdLong();
            log.trace("Could not find, creating guild data object");

            GuildData blank = new GuildData(guildID, false);
            guildData.put(guildID, blank);

            return blank.getUserData(user, true);
        } else return null;
    }

    @Override
    public void close() {
        for (GuildData data : guildData.values()) {
            data.save();
            data.close();
        }
    }

    /**
     * A convenience class to store data associated with the guild.
     */
    private static class GuildData implements Closeable {
        /**
         * The {@link Guild} object that this object wraps.
         */
        private final long guildID;
        /**
         * The {@link BlobModel} that stores the config of the guild.
         */
        private final BlobModel configBlob;
        /**
         * The {@link BlobModel} that stores the user data of the guild.
         */
        private final BlobModel userBlob;
        /**
         * The internal {@link GuildConfig}
         */
        private GuildConfig config;
        /**
         * An internal {@link HashMap} that stores all active users in this guild
         */
        private HashMap<Long, UserData> users;

        /**
         * Creates a new {@link GuildData} object based around the given guild.
         *
         * @param guildID the {@link Guild} to base this object off of
         * @param blank   whether to create a blank instance or fill this instance with data from the cloud.
         */
        public GuildData(long guildID, boolean blank) {
            log.info("Creating guild #{} ({})", guildID, blank);

            this.guildID = guildID;

            ContainerModel container = new ContainerModel(String.valueOf(guildID));

            configBlob = container.createBlob("config.dat");
            userBlob = container.createBlob("users.dat");

            if (blank) {
                config = new GuildConfig();
                users = new HashMap<>();
            } else {
                loadConfig();
                loadUsers();
            }
        }

        /**
         * Saves all data within this object.
         */
        public void save() {
            try {
                File f1 = Files.createTempFile("config", "dat").toFile();
                try (FileOutputStream fos = new FileOutputStream(f1);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(config);
                }

                configBlob.uploadFile(f1);

                File f2 = Files.createTempFile("users", "dat").toFile();
                try (FileOutputStream fos = new FileOutputStream(f2);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(users);
                }

                userBlob.uploadFile(f2);
            } catch (IOException e) {
                log.warn("Could not upload configuration or user data for server #" + guildID, e);
            }
        }

        /**
         * Returns the {@link UserData} object associated with the given user.
         *
         * @param user              the {@link User} to look for
         * @param createIfNotExists whether to create a corresponding object if one could not be found
         * @return the found/created object or {@link null}
         */
        private UserData getUserData(User user, boolean createIfNotExists) {
            long userID = user.getIdLong();
            log.trace("Retrieving user #{}", userID);

            if (users.containsKey(userID)) {
                log.trace("Found it!");

                return users.get(userID);
            } else if (createIfNotExists) {
                log.trace("Could not find, creating.");

                UserData blank = new UserData(userID);
                users.put(userID, blank);

                return blank;
            } else return null;
        }

        /**
         * Loads the config settings from the cloud.
         */
        private void loadConfig() {
            log.trace("Loading config for #{}....", guildID);

            try (FileInputStream fis = new FileInputStream(configBlob.getBlob());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                config = (GuildConfig) ois.readObject();
                log.trace("config: {}", config);
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Could not load configuration for guild #" + guildID, e);

                config = new GuildConfig();
            }
        }

        /**
         * Loads user data from the cloud.
         */
        private void loadUsers() {
            log.trace("Loading user data for #{}...", guildID);

            try (FileInputStream fis = new FileInputStream(userBlob.getBlob());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                users = (HashMap<Long, UserData>) ois.readObject();
                log.trace("users: {}", users);
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Could not load user data for guild #" + guildID, e);

                users = new HashMap<>();
            }
        }

        @Override
        public void close() {
            userBlob.close();
            configBlob.close();
        }
    }
}
