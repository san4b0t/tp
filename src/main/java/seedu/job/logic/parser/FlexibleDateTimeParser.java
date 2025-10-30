package seedu.job.logic.parser;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for parsing date and time strings in multiple flexible formats.
 * Supports various input formats and intelligently defaults missing components.
 */
public class FlexibleDateTimeParser {

    // Default time to use when only date is provided (end of day)
    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);

    // List of supported date-time formatters in order of specificity
    // Use uuuu pattern with STRICT resolver to prevent invalid dates like Feb 30
    private static final List<DateTimeFormatter> DATETIME_FORMATTERS = List.of(
            // Full date-time formats (with zero-padded values)
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            // Date-only formats (will use default time)
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT)
    );

    // List of supported date-only formatters that need year inference
    // Use uuuu pattern with STRICT resolver to prevent invalid dates like Feb 30
    private static final List<DateTimeFormatter> DATE_FORMATTERS_WITH_INFERENCE = List.of(
            // With full month names
            DateTimeFormatter.ofPattern("d MMMM")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-MMMM")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            // With abbreviated month names
            DateTimeFormatter.ofPattern("d MMM")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-MMM")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            // Month-day formats
            DateTimeFormatter.ofPattern("M-d")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("M/d")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT)
    );

    /**
     * Parses a date-time string in various formats and returns a LocalDateTime object.
     * Supports the following formats:
     * - Full date-time: yyyy-MM-ddTHH:mm, yyyy-MM-dd HH:mm, yyyy-MM-ddTHH:mm:ss
     * - Date only: yyyy-MM-dd (defaults to 23:59)
     * - Month-day: MM-dd, MM/dd (infers current or next year, defaults to 23:59)
     * - Day-month: dd MMM, dd MMMM, dd-MMM, dd-MMMM (infers current or next year, defaults to 23:59)
     *
     * @param dateTimeStr The date-time string to parse
     * @return A LocalDateTime object representing the parsed date and time
     * @throws DateTimeParseException if the string cannot be parsed in any supported format
     */
    public static LocalDateTime parse(String dateTimeStr) throws DateTimeParseException {
        if (dateTimeStr == null) {
            throw new DateTimeParseException("Date-time string cannot be null or empty", "", 0);
        }

        String trimmed = dateTimeStr.trim();

        if (trimmed.isEmpty()) {
            throw new DateTimeParseException("Date-time string cannot be null or empty", dateTimeStr, 0);
        }

        // Try full date-time formats first
        for (DateTimeFormatter formatter : DATETIME_FORMATTERS) {
            try {
                return tryParseDateTime(trimmed, formatter);
            } catch (DateTimeParseException e) {
                // If the error is due to invalid date (not just wrong format), re-throw immediately
                if (e.getMessage() != null && e.getMessage().startsWith("Invalid date:")) {
                    throw e;
                }
                // Continue to next format
            }
        }

        // Try date formats that need year inference
        for (DateTimeFormatter formatter : DATE_FORMATTERS_WITH_INFERENCE) {
            try {
                return tryParseDateWithYearInference(trimmed, formatter);
            } catch (DateTimeParseException e) {
                // If the error is due to invalid date (not just wrong format), re-throw immediately
                if (e.getMessage() != null && e.getMessage().startsWith("Invalid date:")) {
                    throw e;
                }
                // Continue to next format
            }
        }

        // If none of the formats worked, throw a detailed exception
        throw new DateTimeParseException(
                "Unable to parse date-time string. Supported formats include: "
                        + "yyyy-MM-ddTHH:mm, yyyy-MM-dd, MM-dd, dd MMM, dd MMMM, etc.",
                dateTimeStr,
                0
        );
    }

    /**
     * Tries to parse a string as a full date-time or date-only format.
     * If only a date is provided, uses the default time (23:59).
     */
    private static LocalDateTime tryParseDateTime(String dateTimeStr, DateTimeFormatter formatter)
            throws DateTimeParseException {
        try {
            // Try to parse as full LocalDateTime
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            // Check if this is due to invalid date values (STRICT resolver rejects invalid dates)
            // STRICT mode error messages contain "Invalid date" for bad dates like Feb 30
            if (e.getMessage() != null && e.getMessage().contains("Invalid date")) {
                throw new DateTimeParseException("Invalid date: " + e.getMessage(), dateTimeStr, 0);
            }

            // Try to parse as LocalDate and add default time
            try {
                LocalDate date = LocalDate.parse(dateTimeStr, formatter);
                return LocalDateTime.of(date, DEFAULT_TIME);
            } catch (DateTimeParseException dtpe) {
                // Check if this is also due to invalid date values
                if (dtpe.getMessage() != null && dtpe.getMessage().contains("Invalid date")) {
                    throw new DateTimeParseException("Invalid date: " + dtpe.getMessage(), dateTimeStr, 0);
                }
                // Re-throw original exception
                throw dtpe;
            } catch (DateTimeException dte) {
                // Invalid date like Feb 30
                throw new DateTimeParseException("Invalid date: " + dte.getMessage(), dateTimeStr, 0);
            }
        }
    }

    /**
     * Tries to parse a string as a date format without year, then infers the year.
     * If the inferred date would be in the past, uses next year instead.
     */
    private static LocalDateTime tryParseDateWithYearInference(String dateStr, DateTimeFormatter formatter)
            throws DateTimeParseException {
        try {
            // Parse with current year first
            int currentYear = Year.now().getValue();
            LocalDate dateWithCurrentYear = parseMonthDayWithYear(dateStr, formatter, currentYear);

            // If the date is in the past, use next year
            LocalDate today = LocalDate.now();
            LocalDate finalDate;
            if (dateWithCurrentYear.isBefore(today)) {
                finalDate = parseMonthDayWithYear(dateStr, formatter, currentYear + 1);
            } else {
                finalDate = dateWithCurrentYear;
            }

            return LocalDateTime.of(finalDate, DEFAULT_TIME);
        } catch (DateTimeParseException e) {
            // Pattern didn't match - let caller try next format
            throw e;
        } catch (DateTimeException e) {
            // Invalid date like Feb 30 (from LocalDate.of())
            throw new DateTimeParseException("Invalid date: " + e.getMessage(), dateStr, 0);
        }
    }

    /**
     * Parses a month-day string with a specified year.
     * With STRICT resolver style, invalid dates like Feb 30 will throw DateTimeParseException.
     */
    private static LocalDate parseMonthDayWithYear(String dateStr, DateTimeFormatter formatter, int year)
            throws DateTimeParseException {
        // Parse the month and day from the string
        java.time.temporal.TemporalAccessor accessor = formatter.parse(dateStr);

        int month = accessor.get(java.time.temporal.ChronoField.MONTH_OF_YEAR);
        int day = accessor.get(java.time.temporal.ChronoField.DAY_OF_MONTH);

        // Create the date with the inferred year
        return LocalDate.of(year, month, day);
    }

    /**
     * Returns the current date and time with the default time (23:59).
     * This is useful for commands that want to default to "today" when no date is specified.
     *
     * @return LocalDateTime representing today at 23:59
     */
    public static LocalDateTime getDefaultDateTime() {
        return LocalDateTime.of(LocalDate.now(), DEFAULT_TIME);
    }

    /**
     * Returns a list of example date formats that are supported by this parser.
     *
     * @return A list of example date format strings
     */
    public static List<String> getSupportedFormatsExamples() {
        List<String> examples = new ArrayList<>();
        examples.add("2025-12-31T23:59 (full date-time)");
        examples.add("2025-12-31 (date only, defaults to 23:59)");
        examples.add("12-31 (month-day, infers year)");
        examples.add("31 Dec (day-month, infers year)");
        examples.add("31 December (day-month with full name, infers year)");
        return examples;
    }
}
