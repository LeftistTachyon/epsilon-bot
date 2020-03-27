package com.github.leftisttachyon.epsilon;

import java.time.Period;

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
    public Period fromString(String s) {
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
}
