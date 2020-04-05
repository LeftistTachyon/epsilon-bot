package com.github.leftisttachyon.epsilon.commands;

import com.github.leftisttachyon.epsilon.GuildInfoService;
import com.github.leftisttachyon.epsilon.data.UserData;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A command that opts out a user from trades.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
@Slf4j
public class OptOutCommand extends Command {
    /**
     * The global instance
     */
    private static final OptOutCommand INSTANCE = new OptOutCommand();

    /**
     * Creates a new {@link OptOutCommand}.
     */
    private OptOutCommand() {
        super("A command that opts out of the regular song or album trades.\n" +
                "To opt out of song trades, then type \"" + PREFIX + "optout song\".\n" +
                "To opt out of album trades, then type \"" + PREFIX + "optout album\".", new String[]{"optout"});
    }

    /**
     * Returns the global instance of an {@link OptOutCommand}.
     *
     * @return the global instance of an {@link OptOutCommand}
     */
    public static OptOutCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void invoke(MessageReceivedEvent evt) {
        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to opt out of!\n" +
                    "To opt out of song trades, type `" + PREFIX + "optin song`.\n" +
                    "To opt out of album trades, type `" + PREFIX + "optin album`.").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1);
        UserData userData = GuildInfoService.getInstance()
                .getUserData(evt.getGuild().getIdLong(), evt.getAuthor(), true);

        if ("song".equalsIgnoreCase(data)) {
            if (!userData.isInSong()) {
                evt.getChannel().sendMessage("You're already opted out of song trades.").queue();
            } else {
                userData.setInSong(false);

                evt.getChannel().sendMessage("Successfully opted you out of song trades!").queue();
            }
        } else if ("album".equalsIgnoreCase(data)) {
            if (!userData.isInAlbum()) {
                evt.getChannel().sendMessage("You're already opted out of album trades.").queue();
            } else {
                userData.setInAlbum(false);

                evt.getChannel().sendMessage("Successfully opted you out of album trades!").queue();
            }
        } else {
            evt.getChannel().sendMessage("`" + data + "` doesn't match any type of music trades that I know.").queue();
        }

        log.trace("user after opting out: {}", userData);
    }
}
