package util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class ReferenceMaker {
    
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static Long generateReferenceNumber() {
        // We obtain the current time in milliseconds
        long time = System.currentTimeMillis();
        // A cyclic counter (from 0 to 999) to handle requests on the same millisecond
        int sequential = counter.incrementAndGet() % 1000;
        // We multiply the time to create space for the sequential
        return (time * 1000) + sequential;
    }
}