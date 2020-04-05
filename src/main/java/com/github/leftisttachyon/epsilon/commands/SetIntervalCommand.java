package com.github.leftisttachyon.epsilon.commands;

import com.github.leftisttachyon.epsilon.EpsilonUtils;
import com.github.leftisttachyon.epsilon.GuildInfoService;
import com.github.leftisttachyon.epsilon.data.GuildConfig;
import com.github.leftisttachyon.epsilon.data.UserData;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Period;

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
        if (!EpsilonUtils.validateIsManager(evt)) {
            return;
        }

        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to set the interval of.\n" +
                    "To change song trades, type `" + PREFIX + "setinterval song <interval>`.\n" +
                    "To change album trades, type `" + PREFIX + "setinterval album <interval>`.").queue();
            return;
        }

        String[] data = message.split(" ");
        GuildConfig guildConfig = GuildInfoService.getInstance()
                .getGuildConfig(evt.getGuild().getIdLong(), true);

        if (data[1].equalsIgnoreCase("song")) {
            Period p = EpsilonUtils.parsePeriod(data[2]);
            guildConfig.setSongInterval(p);
        } else if (data[1].equalsIgnoreCase("album")) {
            Period p = EpsilonUtils.parsePeriod(data[2]);
            guildConfig.setAlbumInterval(p);
        } else {
            evt.getChannel().sendMessage("`" + data[1] + "` doesn't match any type of music trades that I know.")
                    .queue();
        }
    }
}
