package util;

import java.time.LocalTime;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class TimeValidator {
    
    private final static int DATE_HOUR_LENGTH = 15;
    
    public static void verifyTime(LocalTime time) {
        // Verifies if the given minute is a multiple of 15
        if (time.getMinute() % DATE_HOUR_LENGTH != 0) {
            throw new RuntimeException(String.format("Invalid time: All appointments must have intervals of %d minutes.", DATE_HOUR_LENGTH));
        }
        // Verifies if the given second is zero
        if (time.getSecond() != 0) {
            throw new RuntimeException("Invalid time: All appointments' second must be zero.");
        }
        // Patient shif's initial and final hours are pending...
    }
}