package com.github.leftisttachyon.epsilon.commands;

import com.github.leftisttachyon.epsilon.GuildInfoService;
import com.github.leftisttachyon.epsilon.data.GuildConfig;
import com.github.leftisttachyon.epsilon.data.UserData;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

/**
 * A command that sets the interval between each trade event.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class SetIntervalCommand extends Command {
    /**
     * The singleton for this application
     */
    private static final SetIntervalCommand INSTANCE = new SetIntervalCommand();

    /**
     * No external instantiation
     */
    private SetIntervalCommand() {
        super("A command that lets server admins set how often either song or album trades should occur.",
                new String[]{"setinterval"});
    }

    /**
     * Returns the only instance of a {@link SetIntervalCommand} in this application.
     *
     * @return the only instance of a {@link SetIntervalCommand} in this application
     */
    public static SetIntervalCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void invoke(MessageReceivedEvent evt) {
        User author = evt.getAuthor();
        Guild guild = evt.getGuild();
        List<Role> roles =
                Objects.requireNonNull(guild.getMember(author), "User is not in this server")
                        .getRoles();


        GuildInfoService gis = GuildInfoService.getInstance();
        long guildID = guild.getIdLong();
        GuildConfig guildConfig = gis.getGuildConfig(guildID, true);

        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to set the interval of.\n" +
                    "To change song trades, type `" + PREFIX + "setinterval song <interval>`.\n" +
                    "To change album trades, type `" + PREFIX + "setinterval album <interval>`.").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1).toLowerCase();
        UserData userData = gis.getUserData(guildID, author, true);
    }
}
