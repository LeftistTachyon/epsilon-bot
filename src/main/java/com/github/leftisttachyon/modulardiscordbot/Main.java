package com.github.leftisttachyon.modulardiscordbot;

import com.github.leftisttachyon.epsilon.GuildInfoService;
import com.github.leftisttachyon.epsilon.data.GuildConfig;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import com.github.leftisttachyon.modulardiscordbot.commands.Commands;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static com.github.leftisttachyon.modulardiscordbot.commands.Command.PREFIX;

/**
 * The main class for this application.
 *
 * @author LeftistTachyon
 * @since 0.9.0
 */
@Slf4j
public class Main extends ListenerAdapter {
    // perm number: 477248
    // invite link: https://discordapp.com/api/oauth2/authorize?client_id=690006370140815500&permissions=537348160&redirect_uri=http%3A%2F%2Fdsipaint.com%2Fgroup%2F%3Fid%3D11945&response_type=code&scope=bot

    /**
     * Creates a new Main object
     */
    public Main() {
    }

    /**
     * The main method
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            String envvar = System.getenv("BOT_TOKEN");
            log.debug("EnvVar: {}", envvar);
            builder.setToken(envvar);
            builder.addEventListeners(new Main());
            log.info("JDABuilder initialized");

            JDA build = builder.build();
            log.trace("JDABuilder#build() invoked");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("q".equalsIgnoreCase(line)) {
                        build.shutdown();
                        log.info("Shutdown initiated.");
                    }
                }
            } catch (IOException e) {
                log.warn("Could not read input message", e);
            }
        } catch (LoginException ex) {
            log.error("Could not log in successfully", ex);
        }
    }

    /**
     * Called when a message is received on any accessible text channel.
     *
     * @param event a {@code MessageReceivedEvent}F that describes the event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        log.trace("Message received from {}: \"{}\"", event.getAuthor().getName(),
                event.getMessage().getContentDisplay());

        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        String[] data = message.split("\\s+");
        log.trace("data: {}", Arrays.toString(data));
        if (data.length == 0 || !data[0].startsWith(PREFIX)) {
            return;
        }

        TreeMap<String, List<Command>> commands = Commands.getCommands();

        outer:
        for (List<Command> commandList : commands.values()) {
            for (Command c : commandList) {
                log.trace("Checking for invocation of {}{}", PREFIX, c.getPrimaryAlias());

                if (c.shouldInvoke(message)) {
                    c.invoke(event);
                    break outer;
                }
            }
        }
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        log.info("Shutting down...");

        GuildInfoService.getInstance().close();

        log.info("Save and close successful, now exiting");

        System.exit(0);
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent evt) {
        Guild guild = evt.getGuild();
        log.info("Just joined guild #{}", guild.getId());

        Role r = guild.createRole()
                .setName("Music Trade Manager")
                .setMentionable(true).complete();

        GuildInfoService gis = GuildInfoService.getInstance();
        GuildConfig guildConfig = gis.getGuildConfig(guild.getIdLong(), true);
        guildConfig.setManagerRoleID(r.getIdLong());

        TextChannel defaultChannel = guild.getDefaultChannel();
        if (defaultChannel != null) {
            defaultChannel.sendMessage("Hello there! I'm Epsilon, and I can manage music trades.\n" +
                    "Type `" + PREFIX + "help` to get started.").queue();
            defaultChannel.sendMessage("Those who wish to manage the music trades should give themselves the " +
                    "`Music Trade Manager` role.").queue();
        }
    }
}
