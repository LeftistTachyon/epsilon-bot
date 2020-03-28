package com.github.leftisttachyon.epsilon;

import com.github.leftisttachyon.epsilon.data.UserData;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A command that opts in a user from trades.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
@Slf4j
public class OptInCommand extends Command {

    /**
     * Creates a new {@link OptInCommand}.
     */
    public OptInCommand() {
        super("A command that opts in to the regular song or album trades.\n" +
                "To opt in to song trades, then type \"" + PREFIX + "optin song\".\n" +
                "To opt in to album trades, then type \"" + PREFIX + "optin album\".", new String[]{"optin"});
    }

    @Override
    public void invoke(MessageReceivedEvent evt) {
        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify what you want to opt in to!\n" +
                    "To opt in to song trades, type `" + PREFIX + "optin song`.\n" +
                    "To opt in to album trades, type `" + PREFIX + "optin album`.").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1).toLowerCase();
        UserData userData = GuildInfoService.getInstance()
                .getUserData(evt.getGuild().getIdLong(), evt.getAuthor(), true);

        if ("song".equals(data)) {
            if (userData.isInSong()) {
                evt.getChannel().sendMessage("You're already opted into song trades.").queue();
            } else {
                userData.setInSong(true);

                evt.getChannel().sendMessage("Successfully opted you into song trades!").queue();
            }
        } else if ("album".equals(data)) {
            if (userData.hasParticipated()) {
                if (userData.isInAlbum()) {
                    evt.getChannel().sendMessage("You're already opted into album trades.").queue();
                } else {
                    userData.setInAlbum(true);

                    evt.getChannel().sendMessage("Successfully opted you into album trades!").queue();
                }
            } else {
                evt.getChannel().sendMessage("You need to first participate in a song trade before trying an album trade. Try again later!").queue();
            }
        } else {
            evt.getChannel().sendMessage("`" + data + "` doesn't match any type of music trades that I know.").queue();
        }

        log.info("User after opting in: {}", userData);
    }
}
