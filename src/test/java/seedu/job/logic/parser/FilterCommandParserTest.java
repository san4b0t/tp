package seedu.job.logic.parser;

import static seedu.job.logic.JobMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseFailure;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseSuccess;
import static seedu.job.model.jobapplication.JobApplication.Status.APPLIED;
import static seedu.job.model.jobapplication.JobApplication.Status.INPROGRESS;
import static seedu.job.model.jobapplication.JobApplication.Status.REJECTED;
import static seedu.job.model.jobapplication.Model.PREDICATE_SHOW_ALL_APPLICATIONS;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.job.logic.jobcommands.FilterCommand;
import seedu.job.model.jobapplication.DeadlinePredicate;
import seedu.job.model.jobapplication.StatusMatchesKeywordPredicate;
import seedu.job.model.jobapplication.TagsContainKeywordPredicate;

/**
 * Contains unit tests for FilterCommandParser.
 */
public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    // ============== Empty/Invalid Input Tests ==============

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
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

    // ============== None Keyword Tests ==============

    @Test
    public void parse_noneKeywordLowercase_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        assertParseSuccess(parser, "none", expectedCommand);
    }

    @Test
    public void parse_noneKeywordUppercase_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        assertParseSuccess(parser, "NONE", expectedCommand);
    }

    @Test
    public void parse_noneKeywordMixedCase_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        assertParseSuccess(parser, "NoNe", expectedCommand);
    }

    @Test
    public void parse_noneKeywordWithWhitespace_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        assertParseSuccess(parser, " none ", expectedCommand);
        assertParseSuccess(parser, "  none  ", expectedCommand);
    }

    // ============== Tag Filtering Tests ==============

    @Test
    public void parse_validTagSingleWord_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("engineer");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/engineer", expectedCommand);
    }

    @Test
    public void parse_validTagWithHyphen_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("backend-engineer");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/backend-engineer", expectedCommand);
    }

    @Test
    public void parse_validTagLowercase_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("frontend");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/frontend", expectedCommand);
    }

    @Test
    public void parse_validTagUppercase_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("backend");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/BACKEND", expectedCommand);
    }

    @Test
    public void parse_validTagMixedCase_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("java");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/JaVa", expectedCommand);
    }

    @Test
    public void parse_duplicateTagPrefix_throwsParseException() {
        assertParseFailure(parser, " t/engineer t/backend",
                "Multiple values specified for the following single-valued field(s): t/");
    }

    // ============== Status Filtering Tests ==============

    @Test
    public void parse_validStatusAppliedLowercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(APPLIED);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/applied", expectedCommand);
    }

    @Test
    public void parse_validStatusAppliedUppercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(APPLIED);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/APPLIED", expectedCommand);
    }

    @Test
    public void parse_validStatusInProgressLowercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(INPROGRESS);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/inprogress", expectedCommand);
    }

    @Test
    public void parse_validStatusInProgressUppercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(INPROGRESS);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/INPROGRESS", expectedCommand);
    }

    @Test
    public void parse_validStatusRejectedLowercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(REJECTED);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/rejected", expectedCommand);
    }

    @Test
    public void parse_validStatusRejectedUppercase_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(REJECTED);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " s/REJECTED", expectedCommand);
    }

    @Test
    public void parse_invalidStatusValue_throwsParseException() {
        assertParseFailure(parser, " s/INVALID",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }

    @Test
    public void parse_invalidStatusPending_throwsParseException() {
        assertParseFailure(parser, " s/PENDING",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }

    @Test
    public void parse_invalidStatusAccepted_throwsParseException() {
        assertParseFailure(parser, " s/ACCEPTED",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");
    }

    @Test
    public void parse_duplicateStatusPrefix_throwsParseException() {
        assertParseFailure(parser, " s/applied s/rejected",
                "Multiple values specified for the following single-valued field(s): s/");
    }

    // ============== Deadline Filtering Tests ==============

    @Test
    public void parse_validDeadlineStandardFormat_returnsFilterCommand() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        DeadlinePredicate predicate = new DeadlinePredicate(date);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " d/2025-12-31", expectedCommand);
    }

    @Test
    public void parse_validDeadlineEarlyMonth_returnsFilterCommand() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        DeadlinePredicate predicate = new DeadlinePredicate(date);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " d/2025-01-15", expectedCommand);
    }

    @Test
    public void parse_validDeadlineLeapYear_returnsFilterCommand() {
        LocalDate date = LocalDate.of(2024, 2, 29);
        DeadlinePredicate predicate = new DeadlinePredicate(date);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " d/2024-02-29", expectedCommand);
    }

    @Test
    public void parse_invalidDeadlineFormatDayMonthYear_throwsParseException() {
        assertParseFailure(parser, " d/31-12-2025",
                "Invalid date format. Expected format: yyyy-MM-dd");
    }

    @Test
    public void parse_invalidDeadlineFormatSlashes_throwsParseException() {
        assertParseFailure(parser, " d/2025/12/31",
                "Invalid date format. Expected format: yyyy-MM-dd");
    }

    @Test
    public void parse_invalidDeadlineFormatDots_throwsParseException() {
        assertParseFailure(parser, " d/2025.12.31",
                "Invalid date format. Expected format: yyyy-MM-dd");
    }

    @Test
    public void parse_invalidDeadlineInvalidDate_throwsParseException() {
        assertParseFailure(parser, " d/2025-13-01",
                "Invalid date. Please enter a valid date in yyyy-MM-dd format.");
    }

    @Test
    public void parse_invalidDeadlineInvalidDay_throwsParseException() {
        assertParseFailure(parser, " d/2025-02-30",
                "Invalid date. Please enter a valid date in yyyy-MM-dd format.");
    }

    @Test
    public void parse_duplicateDeadlinePrefix_throwsParseException() {
        assertParseFailure(parser, " d/2025-12-31 d/2025-11-30",
                "Multiple values specified for the following single-valued field(s): d/");
    }

    // ============== Multiple Flags Tests ==============

    @Test
    public void parse_tagAndStatus_throwsParseException() {
        assertParseFailure(parser, " t/backend s/applied",
                "Filter command accepts only one filter flag at a time. Please use only one of: t/, s/, or d/");
    }

    @Test
    public void parse_tagAndDeadline_throwsParseException() {
        assertParseFailure(parser, " t/backend d/2025-12-31",
                "Filter command accepts only one filter flag at a time. Please use only one of: t/, s/, or d/");
    }

    @Test
    public void parse_statusAndDeadline_throwsParseException() {
        assertParseFailure(parser, " s/applied d/2025-12-31",
                "Filter command accepts only one filter flag at a time. Please use only one of: t/, s/, or d/");
    }

    @Test
    public void parse_allThreeFlags_throwsParseException() {
        assertParseFailure(parser, " t/backend s/applied d/2025-12-31",
                "Filter command accepts only one filter flag at a time. Please use only one of: t/, s/, or d/");
    }

    // ============== Whitespace Handling Tests ==============

    @Test
    public void parse_tagWithLeadingWhitespace_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("backend");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, "  t/backend", expectedCommand);
    }

    @Test
    public void parse_tagWithTrailingWhitespace_returnsFilterCommand() {
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate("backend");
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, " t/backend  ", expectedCommand);
    }

    @Test
    public void parse_statusWithLeadingWhitespace_returnsFilterCommand() {
        StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(APPLIED);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, "  s/applied", expectedCommand);
    }

    @Test
    public void parse_deadlineWithLeadingWhitespace_returnsFilterCommand() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        DeadlinePredicate predicate = new DeadlinePredicate(date);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        assertParseSuccess(parser, "  d/2025-12-31", expectedCommand);
    }
}
