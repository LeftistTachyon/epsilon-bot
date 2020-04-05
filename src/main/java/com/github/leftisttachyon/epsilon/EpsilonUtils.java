package com.github.leftisttachyon.epsilon;

import com.github.leftisttachyon.epsilon.data.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Period;
import java.util.List;
import java.util.Objects;

/**
 * A utility class used for some behind-the-scenes processing.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class EpsilonUtils {
    /**
     * All static methods here!
     */
    private EpsilonUtils() {
    }

    /**
     * Converts an expression stored in a {@link String} to an equivalent {@link Period} object.<br>
     * For example, "2d" will be converted to a period two days long, "1w" to one week,
     * "5m" to five months, and "10y" to ten years.
     *
     * @param s the {@link String} that holds the expression to convert
     * @return a {@link Period} which represents the given {@link String}, or {@code null} if nothing could be matched
     */
    public static Period parsePeriod(String s) {
        int len = s.length(), number = Integer.parseInt(s.substring(0, len - 1));
        switch (s.charAt(len - 1)) {
            case 'd':
            case 'D':
                return Period.ofDays(number);
            case 'w':
            case 'W':
                return Period.ofWeeks(number);
            case 'm':
            case 'M':
                return Period.ofMonths(number);
            case 'y':
            case 'Y':
                return Period.ofYears(number);
            default:
                return null;
        }
    }

    /**
     * Validates that the person that sent the message is a music trade manager in the guild.<br>
     * If the member is not a manager, then a message that notifies the member of the lacking permissions is sent.
     *
     * @param evt the {@link MessageReceivedEvent} that the message generated
     * @return whether the member is a music trade manager.
     */
    public static boolean validateIsManager(MessageReceivedEvent evt) {
        Guild guild = evt.getGuild();
        List<Role> roles = Objects.requireNonNull(
                guild.getMember(evt.getAuthor()), "User is not in this server")
                .getRoles();

        long guildID = guild.getIdLong();
        GuildConfig guildConfig = GuildInfoService.getInstance()
                .getGuildConfig(guildID, true);

        for (Role r : roles) {
            if (r.getIdLong() == guildConfig.getManagerRoleID()) {
                return true;
            }
        }

        evt.getChannel().sendMessage("You need to have the `Music Trade Manager` role to use this command!")
                .queue();
        return false;
    }
}
