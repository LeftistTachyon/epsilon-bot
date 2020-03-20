package com.github.leftisttachyon.epsilon;

import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import com.github.leftisttachyon.modulardiscordbot.commands.ConsumerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.github.leftisttachyon.modulardiscordbot.commands.Command.PREFIX;

/**
 * A command that toggles the given user's status.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class OptCommands {
    /**
     * A command tha opts in a user.
     */
    private static final ConsumerCommand OPT_IN = new ConsumerCommand(OptCommands::optIn,
            "A command that opts in to the regular song or album trades.\n" +
                    "To opt in to song trades, then type \"" + PREFIX + "optin song\".\n" +
                    "To opt in to album trades, then type \"" + PREFIX + "optin album\".", new String[]{"optin"});

    /**
     * A command tha opts out a user.
     */
    private static final ConsumerCommand OPT_OUT = new ConsumerCommand(OptCommands::optOut,
            "A command that opts out of the regular song or album trades.\n" +
                    "To opt out of song trades, then type \"" + PREFIX + "optout song\".\n" +
                    "To opt out of album trades, then type \"" + PREFIX + "optout album\".", new String[]{"optout"});

    /**
     * Returns the {@link Command} that can opt in users.
     *
     * @return the {@link Command} that can opt in users
     */
    public static Command getOptInCommand() {
        return OPT_IN;
    }

    /**
     * Returns the {@link Command} that can opt out users.
     *
     * @return the {@link Command} that can opt out users
     */
    public static Command getOptOutCommand() {
        return OPT_OUT;
    }

    /**
     * A method that opts the user into the song or album trades.
     *
     * @param evt the event created by the user's message
     */
    private static void optIn(MessageReceivedEvent evt) {
        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to opt in to!\n" +
                    "To opt in to song trades, type `" + PREFIX + "optin song`.\n" +
                    "To opt in to album trades, type `" + PREFIX + "optin album`.").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1).toLowerCase();
        if ("song".equals(data)) {
            evt.getChannel().sendMessage("So you want to opt in to the song trades?").queue();
        } else if ("album".equals(data)) {
            evt.getChannel().sendMessage("So you want to opt in to the album trades?").queue();
        } else {
            evt.getChannel().sendMessage("`" + data + "` doesn't match any type of music trades that I know.").queue();
        }
    }

    /**
     * A method that opts the user out of the song or album trades.
     *
     * @param evt the event created by the user's message
     */
    private static void optOut(MessageReceivedEvent evt) {
        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to opt out of!\n" +
                    "To opt out of song trades, type `" + PREFIX + "optin song`.\n" +
                    "To opt out of album trades, type `" + PREFIX + "optin album`.").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1).toLowerCase();
        if ("song".equals(data)) {
            evt.getChannel().sendMessage("So you want to opt out of the song trades?").queue();
        } else if ("album".equals(data)) {
            evt.getChannel().sendMessage("So you want to opt out of the album trades?").queue();
        } else {
            evt.getChannel().sendMessage("`" + data + "` doesn't match any type of music trades that I know.").queue();
        }
    }
}
