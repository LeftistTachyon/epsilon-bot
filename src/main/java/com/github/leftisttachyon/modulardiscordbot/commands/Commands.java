package com.github.leftisttachyon.modulardiscordbot.commands;

import com.github.leftisttachyon.epsilon.commands.OptInCommand;
import com.github.leftisttachyon.epsilon.commands.OptOutCommand;
import com.github.leftisttachyon.epsilon.commands.SetProfileCommand;
import com.github.leftisttachyon.modulardiscordbot.commands.impl.HelpCommand;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A utility class that deals with Commands
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class Commands {
    /**
     * A Map of all commands, sorted by category
     */
    private static final TreeMap<String, List<Command>> commands;

    static {
        // the map
        commands = new TreeMap<>();

        // a list of "Meta" commands
        List<Command> metaCommands = new ArrayList<>();

        // the "!!ping" command
        metaCommands.add(new ConsumerCommand(event -> {
            OffsetDateTime time = event.getMessage().getTimeCreated(),
                    now = OffsetDateTime.now();
            Duration delay = Duration.between(time, now).abs();
            long millis = delay.toMillis();

            event.getChannel().sendMessageFormat("Pong! The delay is %,d milliseconds.", millis).queue();
        }, "Pings me!", new String[]{"ping"}));

        // the "!!help" command
        metaCommands.add(HelpCommand.getHelpCommand());

        // the "!!github" command
        metaCommands.add(new ConsumerCommand(event -> event.getChannel()
                .sendMessage("You can find my source code here:\nhttps://github.com/LeftistTachyon/epsilon-bot")
                .queue(), "Links to my source code on GitHub", new String[]{"github"}));

        // add the "Meta" commands
        commands.put("Meta", metaCommands);

        // a list of "Song Trade"-related commands
        List<Command> songTradeCommands = new ArrayList<>();

        // the "!!optin" command
        songTradeCommands.add(OptInCommand.getInstance());

        // the "!!optout" command
        songTradeCommands.add(OptOutCommand.getInstance());

        // the "!!setprofile" command
        songTradeCommands.add(SetProfileCommand.getInstance());

        // add the "Song Trade" commands
        commands.put("Song Trade", songTradeCommands);
    }

    /**
     * Returns the TreeMap of Commands that are used in this bot.
     *
     * @return all commands
     * @see Command
     */
    public static TreeMap<String, List<Command>> getCommands() {
        return commands;
    }
}
