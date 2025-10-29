package seedu.job.logic.parser;

import static seedu.job.logic.JobMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseFailure;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.job.logic.jobcommands.AddJobCommand;
import seedu.job.model.jobapplication.JobApplication;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the AddJobCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the AddJobCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_validArgs_returnsAddJobCommand() {
        String companyName = "Google";
        String role = "SoftwareEngineer";
        LocalDateTime deadline = LocalDateTime.parse("2025-12-31T23:59");
        JobApplication.Status status = JobApplication.Status.APPLIED;

        JobApplication expectedApplication = new JobApplication(companyName, role, deadline, status, new HashSet<>());
        assertParseSuccess(parser, " n/Google r/SoftwareEngineer s/APPLIED d/2025-12-31T23:59",
                new AddJobCommand(expectedApplication));
    }

    @Test
    public void parse_validArgsWithDifferentStatus_returnsAddJobCommand() {
        String companyName = "Microsoft";
        String role = "DataScientist";
        LocalDateTime deadline = LocalDateTime.parse("2025-11-30T17:00");
        JobApplication.Status status = JobApplication.Status.INPROGRESS;

        JobApplication expectedApplication = new JobApplication(companyName, role, deadline, status, new HashSet<>());
        assertParseSuccess(parser, " n/Microsoft r/DataScientist s/INPROGRESS d/2025-11-30T17:00",
                new AddJobCommand(expectedApplication));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingArgs_throwsParseException() {
        // Only company name
        assertParseFailure(parser, " n/Google",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        // Only company name and role
        assertParseFailure(parser, " n/Google r/SoftwareEngineer",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        // Missing status
        assertParseFailure(parser, " n/Google r/SoftwareEngineer d/2025-12-31T23:59",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDeadline_throwsParseException() {
        String expectedMessage = "Invalid deadline format. Supported formats: "
                + String.join(", ", FlexibleDateTimeParser.getSupportedFormatsExamples());
        assertParseFailure(parser, " n/Google r/SoftwareEngineer s/APPLIED d/invalid-date",
                expectedMessage);
    }

    @Test
    public void parse_missingDeadline_defaultsToToday() {
        // When deadline is not specified, should default to today at 23:59
        String companyName = "Apple";
        String role = "ProductManager";
        JobApplication.Status status = JobApplication.Status.APPLIED;

        JobApplication expectedApplication = new JobApplication(companyName, role,
                FlexibleDateTimeParser.getDefaultDateTime(), status, new HashSet<>());
        assertParseSuccess(parser, " n/Apple r/ProductManager s/APPLIED",
                new AddJobCommand(expectedApplication));
    }

    @Test
    public void parse_dateOnlyFormat_success() {
        // Test yyyy-MM-dd format
        String companyName = "Amazon";
        String role = "CloudEngineer";
        LocalDateTime deadline = LocalDateTime.parse("2025-12-31T23:59");
        JobApplication.Status status = JobApplication.Status.APPLIED;

        JobApplication expectedApplication = new JobApplication(companyName, role, deadline, status, new HashSet<>());
        assertParseSuccess(parser, " n/Amazon r/CloudEngineer s/APPLIED d/2025-12-31",
                new AddJobCommand(expectedApplication));
    }

    @Test
    public void parse_monthDayFormat_success() throws Exception {
        // Test MM-dd format (should infer year)
        String companyName = "Meta";
        String role = "SoftwareEngineer";
        JobApplication.Status status = JobApplication.Status.APPLIED;

        // Parse the command
        AddJobCommand command = parser.parse(" n/Meta r/SoftwareEngineer s/APPLIED d/12-31");

        // Verify it parsed successfully and created a valid command
        assert command != null;
        assert command instanceof AddJobCommand;
    }

    @Test
    public void parse_dayMonthFormat_success() throws Exception {
        // Test dd MMM format
        String companyName = "Netflix";
        String role = "DataEngineer";
        JobApplication.Status status = JobApplication.Status.APPLIED;

        // Parse the command
        AddJobCommand command = parser.parse(" n/Netflix r/DataEngineer s/APPLIED d/31 Dec");

        // Verify it parsed successfully and created a valid command
        assert command != null;
        assert command instanceof AddJobCommand;
    }

    @Test
    public void parse_dayMonthFullFormat_success() throws Exception {
        // Test dd MMMM format
        String companyName = "Tesla";
        String role = "MLEngineer";
        JobApplication.Status status = JobApplication.Status.APPLIED;

        // Parse the command
        AddJobCommand command = parser.parse(" n/Tesla r/MLEngineer s/APPLIED d/31 December");

        // Verify it parsed successfully and created a valid command
        assert command != null;
        assert command instanceof AddJobCommand;
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertParseFailure(parser, " n/Google r/SoftwareEngineer s/INVALID_STATUS d/2025-12-31T23:59",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }

    @Test
    public void parse_pastDeadline_throwsParseException() {
        assertParseFailure(parser, " n/Google r/SoftwareEngineer s/APPLIED d/2020-01-01T00:00",
                "Deadline cannot be in the past. Please provide a future date and time.");
    }
}
