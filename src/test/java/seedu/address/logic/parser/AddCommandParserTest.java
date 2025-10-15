package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.JobCommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.JobCommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.jobcommands.AddJobCommand;
import seedu.address.model.jobapplication.JobApplication;

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

        JobApplication expectedApplication = new JobApplication(companyName, role, deadline, status);
        assertParseSuccess(parser, "Google SoftwareEngineer 2025-12-31T23:59 APPLIED",
                new AddJobCommand(expectedApplication));
    }

    @Test
    public void parse_validArgsWithDifferentStatus_returnsAddJobCommand() {
        String companyName = "Microsoft";
        String role = "DataScientist";
        LocalDateTime deadline = LocalDateTime.parse("2025-11-30T17:00");
        JobApplication.Status status = JobApplication.Status.INPROGRESS;

        JobApplication expectedApplication = new JobApplication(companyName, role, deadline, status);
        assertParseSuccess(parser, "Microsoft DataScientist 2025-11-30T17:00 INPROGRESS",
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
        assertParseFailure(parser, "Google",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        // Only company name and role
        assertParseFailure(parser, "Google SoftwareEngineer",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        // Missing status
        assertParseFailure(parser, "Google SoftwareEngineer 2025-12-31T23:59",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDeadline_throwsParseException() {
        assertParseFailure(parser, "Google SoftwareEngineer invalid-date APPLIED",
                "Invalid deadline format. Expected format: yyyy-MM-ddTHH:mm");
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertParseFailure(parser, "Google SoftwareEngineer 2025-12-31T23:59 INVALID_STATUS",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }
}
