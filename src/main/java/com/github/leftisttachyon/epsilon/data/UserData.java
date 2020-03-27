package com.github.leftisttachyon.epsilon.data;

import lombok.Data;
import net.dv8tion.jda.api.entities.User;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A class that stores data related to users.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
@Data
public class UserData implements Serializable {
    /**
     * Whether this user is in song trades.
     */
    private boolean inSong;
    /**
     * Whether this user is in album trades.
     */
    private boolean inAlbum;
    /**
     * The last date when this {@link UserData} participated in trades.
     */
    private LocalDate lastParticipated;
    /**
     * The ID of the user that this object wraps.
     */
    private final long userID;

    /**
     * Determines whether this {@link UserData} has participated in a trade before.
     * @return whether this {@link UserData} has participated in a trade before
     */
    public boolean hasParticipated() {
        return lastParticipated != null;
    }
}
