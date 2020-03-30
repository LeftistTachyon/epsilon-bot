package com.github.leftisttachyon.epsilon.data;

import lombok.Data;

import java.io.Serializable;
import java.time.Period;

/**
 * A class that stores the configuration for each guild.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
@Data
public class GuildConfig implements Serializable {
    /**
     * An ID for serialization
     */
    private static final long serialVersionUID = -1960983752378643009L;

    /**
     * The interval between each song trade.
     */
    private Period songInterval;
    /**
     * The interval between each album trade.
     */
    private Period albumInterval;
    /**
     * Whether error messages should be sent into the guild channel which caused them.
     */
    private boolean sendErrorMessages;
}
