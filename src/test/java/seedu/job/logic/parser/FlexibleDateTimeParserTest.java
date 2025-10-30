package seedu.job.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

public class FlexibleDateTimeParserTest {

    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);

    @Test
    public void parse_fullDateTimeWithT_success() {
        // Test format: yyyy-MM-ddTHH:mm
        LocalDateTime result = FlexibleDateTimeParser.parse("2025-12-31T23:59");
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_fullDateTimeWithTAndSeconds_success() {
        // Test format: yyyy-MM-ddTHH:mm:ss
        LocalDateTime result = FlexibleDateTimeParser.parse("2025-12-31T23:59:59");
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59, 59), result);
    }

    @Test
    public void parse_fullDateTimeWithSpace_success() {
        // Test format: yyyy-MM-dd HH:mm
        LocalDateTime result = FlexibleDateTimeParser.parse("2025-12-31 23:59");
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_dateOnly_defaultsToEndOfDay() {
        // Test format: yyyy-MM-dd
        LocalDateTime result = FlexibleDateTimeParser.parse("2025-12-31");
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_monthDay_infersYear() {
        // Test format: MM-dd
        LocalDateTime result = FlexibleDateTimeParser.parse("12-31");

        // Should use current year or next year
        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        // If the date would be in the past, it should use next year
        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_monthDayWithSlash_infersYear() {
        // Test format: MM/dd
        LocalDateTime result = FlexibleDateTimeParser.parse("12/31");

        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_dayMonthAbbreviated_infersYear() {
        // Test format: dd MMM
        LocalDateTime result = FlexibleDateTimeParser.parse("31 Dec");

        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_dayMonthAbbreviatedWithDash_infersYear() {
        // Test format: dd-MMM
        LocalDateTime result = FlexibleDateTimeParser.parse("31-Dec");

        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_dayMonthFull_infersYear() {
        // Test format: dd MMMM
        LocalDateTime result = FlexibleDateTimeParser.parse("31 December");

        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_dayMonthFullWithDash_infersYear() {
        // Test format: dd-MMMM
        LocalDateTime result = FlexibleDateTimeParser.parse("31-December");

        int expectedYear = LocalDate.now().getYear();
        LocalDate expectedDate = LocalDate.of(expectedYear, 12, 31);

        if (expectedDate.isBefore(LocalDate.now())) {
            expectedYear++;
        }

        assertEquals(LocalDateTime.of(expectedYear, 12, 31, 23, 59), result);
    }

    @Test
    public void parse_pastDateInCurrentYear_usesNextYear() {
        // Use January 1st which should always be in the past during most of the year
        // Unless we're currently on January 1st
        LocalDate today = LocalDate.now();
        if (today.getMonthValue() != 1 || today.getDayOfMonth() != 1) {
            LocalDateTime result = FlexibleDateTimeParser.parse("01-Jan");

            // Should use next year since Jan 1st has passed
            int expectedYear = today.getYear() + 1;
            assertEquals(LocalDateTime.of(expectedYear, 1, 1, 23, 59), result);
        }
    }

    @Test
    public void parse_nullInput_throwsException() {
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse(null));
    }

    @Test
    public void parse_emptyString_throwsException() {
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse(""));
    }

    @Test
    public void parse_invalidFormat_throwsException() {
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("invalid-date"));
    }

    @Test
    public void parse_whitespaceOnly_throwsException() {
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("   "));
    }

    @Test
    public void parse_invalidDate_throwsException() {
        // Test invalid dates like Feb 30, April 31, etc.
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("30 Feb"));
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("30 February"));
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("31 Apr"));
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("2025-02-30"));
        assertThrows(DateTimeParseException.class, () -> FlexibleDateTimeParser.parse("2025-04-31"));
    }

    @Test
    public void parse_trimmedInput_success() {
        // Test that leading/trailing whitespace is handled
        LocalDateTime result = FlexibleDateTimeParser.parse("  2025-12-31T23:59  ");
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59), result);
    }

    @Test
    public void getDefaultDateTime_returnsToday() {
        LocalDateTime result = FlexibleDateTimeParser.getDefaultDateTime();

        assertEquals(LocalDate.now(), result.toLocalDate());
        assertEquals(DEFAULT_TIME, result.toLocalTime());
    }

    @Test
    public void getSupportedFormatsExamples_returnsNonEmptyList() {
        var formats = FlexibleDateTimeParser.getSupportedFormatsExamples();

        assertNotNull(formats);
        assertTrue(formats.size() > 0);
        // Check that it contains an example with the date format
        assertTrue(formats.stream().anyMatch(f -> f.contains("2025-12-31")));
    }
}
