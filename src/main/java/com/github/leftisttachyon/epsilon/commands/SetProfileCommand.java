package com.github.leftisttachyon.epsilon.commands;

import com.github.leftisttachyon.epsilon.GuildInfoService;
import com.github.leftisttachyon.epsilon.data.UserData;
import com.github.leftisttachyon.modulardiscordbot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * A command that allows users to associate a URL with their profile to help others choose what kind of music to give
 * them.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class SetProfileCommand extends Command {
    /**
     * The only instance to be created.
     */
    private static final SetProfileCommand INSTANCE = new SetProfileCommand();

    /**
     * Creates a new {@link SetProfileCommand}.
     */
    private SetProfileCommand() {
        super("A command that sets associates a URL with your profile to help others choose good songs and albums for you.\n" +
                        "This is how you include your music profile page or something similar.",
                new String[]{"setprofile"});
    }

    /**
     * Returns the global instance of a {@link SetProfileCommand}.
     *
     * @return the global instance of a {@link SetProfileCommand}
     */
    public static SetProfileCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void invoke(MessageReceivedEvent evt) {
        String message = evt.getMessage().getContentRaw();
        if (!message.contains(" ")) {
            evt.getChannel().sendMessage("You need to specify a URL!\n" +
                    "ex. `" + PREFIX + "setprofile http://dsipaint.com/group/page.php?page=10436`").queue();
            return;
        }

        String data = message.substring(message.indexOf(' ') + 1);

        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(data)) {
            UserData userData = GuildInfoService.getInstance()
                    .getUserData(evt.getGuild().getIdLong(), evt.getAuthor(), true);
            userData.setMusicProfile(data);

            evt.getChannel().sendMessage("Music profile page successfully set!").queue();
        } else {
            evt.getChannel().sendMessage("Hmm, that doesn't seem like a valid URL. Please try again.").queue();
        }
    }
}
