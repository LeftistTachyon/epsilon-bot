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
    private HashMap<Long, GuildData> serverContainers;

    /**
     * Creates a new {@link GuildInfoService}.
     */
    private GuildInfoService() {
        serverContainers = new HashMap<>();
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
     * @param guild             the {@link Guild} to get the configuration for
     * @param createIfNotExists whether to create an entry for the given guild
     * @return the {@link GuildConfig} associated with the given guild, or {@code null} if none could be found and
     * {@code createIfNotExists} is false
     */
    public GuildConfig getGuildConfig(Guild guild, boolean createIfNotExists) {
        long guildID = guild.getIdLong();

        if (serverContainers.containsKey(guildID)) {
            return serverContainers.get(guildID).config;
        } else if (createIfNotExists) {
            GuildData blank = new GuildData(guild, true);
            serverContainers.put(guildID, blank);

            return blank.config;
        } else return null;
    }

    /**
     * Gets the {@link UserData} object associated with the given guild and user.
     *
     * @param guild             the {@link Guild} to look for
     * @param user              the {@link User} to look for
     * @param createIfNotExists whether to create a corresponding object if it cannot be found
     * @return the found or created object or {@link null}
     */
    public UserData getUserData(Guild guild, User user, boolean createIfNotExists) {
        long guildID = guild.getIdLong();

        if (serverContainers.containsKey(guildID)) {
            return serverContainers.get(guildID).getUserData(user, createIfNotExists);
        } else if (createIfNotExists) {
            GuildData blank = new GuildData(guild, true);
            serverContainers.put(guildID, blank);

            long userID = user.getIdLong();
            UserData temp = new UserData(userID);
            blank.users.put(userID, temp);

            return temp;
        } else return null;
    }

    @Override
    public void close() {
        for (GuildData data : serverContainers.values()) {
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
        private final Guild guild;
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
         * Creates a new {@link GuildData} object based around the given {@link Guild} object.
         *
         * @param guild the {@link Guild} to base this object off of
         * @param blank whether to create a blank instance or fill this instance with data from the cloud.
         */
        public GuildData(Guild guild, boolean blank) {
            this.guild = guild;

            ContainerModel container = new ContainerModel(guild.getId());

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
                log.warn("Could not upload configuration or user data for server #" + guild.getId(), e);
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

            if (users.containsKey(userID)) {
                return users.get(userID);
            } else if (createIfNotExists) {
                UserData blank = new UserData(userID);
                users.put(userID, blank);

                return blank;
            } else return null;
        }

        /**
         * Loads the config settings from the cloud.
         */
        private void loadConfig() {
            try (FileInputStream fis = new FileInputStream(configBlob.getBlob());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                config = (GuildConfig) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Could not load configuration for guild #" + guild.getId(), e);

                config = new GuildConfig();
            }
        }

        /**
         * Loads user data from the cloud.
         */
        private void loadUsers() {
            try (FileInputStream fis = new FileInputStream(userBlob.getBlob());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                users = (HashMap<Long, UserData>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Could not load user data for guild #" + guild.getId(), e);

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
