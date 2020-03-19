package com.github.leftisttachyon.modulardiscordbot.commands;

/**
 * A POJO object that represents a subcommand.
 *
 * @author Jed Wang
 * @see Command
 * @since 0.9.0
 */
public abstract class Subcommand extends Command {
    /**
     * The aliases of this subcommand
     */
    private String[] subAliases;

    /**
     * Creates a new command
     *
     * @param description a description of this subcommand
     * @param aliases     aliases of the parent command; the first one in the array will always be the primary alias
     * @param subAliases  the aliases of the subcommand; the first one is the primary alias
     */
    public Subcommand(String description, String[] aliases, String[] subAliases) {
        super(description, aliases);

        for (int i = 0; i < subAliases.length; i++) {
            subAliases[i] = subAliases[i].toLowerCase();
        }
        this.subAliases = subAliases;
    }

    /**
     * Creates a subcommand based off of a parent command
     *
     * @param description a description of this subcommand
     * @param subAliases  the aliases of this subcommand
     * @param parent      the parent of this subcommand
     */
    public Subcommand(String description, String[] subAliases, ConsumerCommand parent) {
        super(description, parent.getAliases());
        for (int i = 0; i < subAliases.length; i++) {
            subAliases[i] = subAliases[i].toLowerCase();
        }
        this.subAliases = subAliases;
    }
}
