package com.github.leftisttachyon.modulardiscordbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A POJO that represents a bot command.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public abstract class Command {
    /**
     * The logger for this class
     */
    private static final Logger log = LoggerFactory.getLogger(Command.class);

    /**
     * The prefix this bot will use
     */
    public static final String PREFIX = "!!";

    /**
     * A description of the command
     */
    private final String description;

    /**
     * A list of aliases (or nicknames or shorthand) that can call this command.<br>
     * For example, {@code resume} could be an alias of {@code unpause}
     */
    private final String[] aliases;

    /**
     * Creates a new ConsumerCommand
     *
     * @param description a description of the command
     * @param aliases     aliases of the command; the first one in the array will always be the primary alias
     */
    public Command(String description, String[] aliases) {
        this.description = description;
        if (aliases.length == 0) {
            throw new IllegalArgumentException("A command must have at least one alias");
        }
        for (int i = 0; i < aliases.length; i++) {
            aliases[i] = aliases[i].toLowerCase();
        }
        this.aliases = aliases;
    }

    /**
     * Returns the description of the command
     *
     * @return the description of the command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the aliases of this command
     *
     * @return the aliases of this command
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Returns the primary alias of this command
     *
     * @return the primary alias of this command
     */
    public String getPrimaryAlias() {
        return aliases[0];
    }

    /**
     * Determines whether this command should be invoked by the given string command.
     *
     * @param s the entire command
     * @return whether this command should be invoked
     */
    public boolean shouldInvoke(String s) {
        String[] data = s.split("\\s+");
        return shouldInvoke(data);
    }

    /**
     * Given the parsed message, determines whether this command should be invoked
     *
     * @param data the parsed command
     * @return whether this command should be invoked
     */
    protected boolean shouldInvoke(String[] data) {
        boolean startsWith = data[0].startsWith(PREFIX), isAlias = isAlias(data[0].substring(PREFIX.length()));
        log.trace("shouldInvoke for {}: {} {} {}", getPrimaryAlias(), data.length > 0, startsWith,
                isAlias);
        return data.length > 0 && startsWith && isAlias;
    }

    /**
     * Invokes this command with the given MessageReceivedEvent
     *
     * @param event the event that gives the information needed to invoke this command
     */
    public abstract void invoke(MessageReceivedEvent event);

    /**
     * Determines whether the given string is an alias of this command
     *
     * @param alias the possible alias
     * @return whether the given string is an alias of this command
     */
    public boolean isAlias(String alias) {
        for (String s : aliases) {
            if (s.equals(alias)) {
                return true;
            }
        }

        return false;
    }
}
