package seedu.job.logic.parser;

import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseFailure;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseSuccess;
import static seedu.job.model.jobapplication.JobApplication.Status.APPLIED;
import static seedu.job.model.jobapplication.Model.PREDICATE_SHOW_ALL_APPLICATIONS;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.job.logic.jobcommands.FilterCommand;
import seedu.job.model.jobapplication.DeadlinePredicate;
import seedu.job.model.jobapplication.RoleContainsKeywordPredicate;
import seedu.job.model.jobapplication.StatusMatchesKeywordPredicate;
import seedu.job.model.jobapplication.TagsContainKeywordPredicate;

/**
 * Contains unit tests for FilterCommandParser.
 */
public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noneKeyword_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        assertParseSuccess(parser, "none", expectedCommand);
        assertParseSuccess(parser, " none ", expectedCommand);
        assertParseSuccess(parser, "NONE", expectedCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFilterCommand() {
        // Test with tag "engineer"
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("engineer");
        FilterCommand expectedCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, " t/engineer", expectedCommand);
    }

    @Test
    public void parse_validRoleArgs_returnsFilterCommand() {
        // Test with role "developer"
        RoleContainsKeywordPredicate predicate = new RoleContainsKeywordPredicate("developer");
        FilterCommand expectedCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, " r/developer", expectedCommand);
    }

    @Test
    public void parse_validStatusArgs_returnsFilterCommand() {
        // Test with status "APPLIED"
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(APPLIED);
        FilterCommand expectedCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, " s/applied", expectedCommand);
        assertParseSuccess(parser, " s/APPLIED", expectedCommand);
    }

    @Test
    public void parse_validDeadlineArgs_returnsFilterCommand() {
        // Test with deadline "2025-12-31"
        LocalDate date = LocalDate.of(2025, 12, 31);
        DeadlinePredicate predicate = new DeadlinePredicate(date);
        FilterCommand expectedCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, " d/2025-12-31", expectedCommand);
    }

    @Test
    public void parse_invalidStatusValue_throwsParseException() {
        assertParseFailure(parser, " s/INVALID",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }

    @Test
    public void parse_invalidDeadlineFormat_throwsParseException() {
        assertParseFailure(parser, " d/31-12-2025",
                "Invalid date format. Expected format: yyyy-MM-dd");
        assertParseFailure(parser, " d/2025/12/31",
                "Invalid date format. Expected format: yyyy-MM-dd");
    }

    @Test
    public void parse_duplicatePrefix_throwsParseException() {
        // Duplicate prefix should fail
        assertParseFailure(parser, " t/engineer t/backend",
                "Multiple values specified for the following single-valued field(s): t/");
    }

    @Test
    public void parse_noPrefixProvided_throwsParseException() {
        assertParseFailure(parser, " engineer",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preambleWithPrefix_throwsParseException() {
        assertParseFailure(parser, " extra t/engineer",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
