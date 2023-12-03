package combat.squad.proposal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateValidator {

    public final static long ONE_HOUR = 3600000;
    public final static String DATE_FORMAT = "dd:MM:yyyy HH:mm";

    /**
     * Sprawdza, czy podane daty spełniają wymagane kryteria.
     *
     * @param startDate Data początkowa.
     * @param endDate   Data końcowa.
     * @throws IllegalArgumentException jeśli daty nie spełniają kryteriów.
     */
    public static void validateDates(Date startDate, Date endDate) {

        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        Date now = new Date();
        System.out.println("Now: " + now);
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (startDate.before(now)) {
            throw new IllegalArgumentException("Start date must be in the future");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        long duration = Duration.between(startDate.toInstant(), endDate.toInstant()).toMillis();
        if (duration != ONE_HOUR) {
            throw new IllegalArgumentException("Event must last at least one hour");
        }
    }
}
