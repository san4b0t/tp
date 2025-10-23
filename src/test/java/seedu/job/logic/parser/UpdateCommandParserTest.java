package seedu.job.logic.parser;

import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.job.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.job.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseFailure;
import static seedu.job.logic.parser.JobCommandParserTestUtil.assertParseSuccess;
import static seedu.job.testutil.TypicalIndexes.INDEX_FIRST_JOB;
import static seedu.job.testutil.TypicalIndexes.INDEX_SECOND_JOB;
import static seedu.job.testutil.TypicalIndexes.INDEX_THIRD_JOB;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.job.commons.core.index.Index;
import seedu.job.logic.jobcommands.UpdateJobCommand;
import seedu.job.logic.jobcommands.UpdateJobCommand.UpdateJobDescriptor;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.testutil.UpdateJobDescriptorBuilder;

/**
 * Contains tests for UpdateCommandParser.
 */
public class UpdateCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateJobCommand.MESSAGE_USAGE);

    private UpdateCommandParser parser = new UpdateCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, PREFIX_NAME + "Google", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", UpdateJobCommand.MESSAGE_NOT_UPDATED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + PREFIX_NAME + "Google", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + PREFIX_NAME + "Google", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid status
        assertParseFailure(parser, "1 " + PREFIX_STATUS + "INVALID_STATUS",
                "Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED");

        // invalid deadline format
        assertParseFailure(parser, "1 " + PREFIX_DEADLINE + "2025-13-01",
                "Invalid deadline format. Expected format: yyyy-MM-ddTHH:mm");

        assertParseFailure(parser, "1 " + PREFIX_DEADLINE + "invalid-date",
                "Invalid deadline format. Expected format: yyyy-MM-ddTHH:mm");

        // past deadline
        assertParseFailure(parser, "1 " + PREFIX_DEADLINE + "2020-01-01T00:00",
                "Deadline cannot be in the past. Please provide a future date and time.");
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_JOB;
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_NAME + "Meta "
                + PREFIX_ROLE + "Senior Engineer "
                + PREFIX_STATUS + "INPROGRESS "
                + PREFIX_DEADLINE + "2026-01-15T18:00 "
                + PREFIX_TAG + "urgent";

        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder()
                .withCompanyName("Meta")
                .withRole("Senior Engineer")
                .withStatus(JobApplication.Status.INPROGRESS)
                .withDeadline(LocalDateTime.of(2026, 1, 15, 18, 0))
                .withTags("urgent")
                .build();

        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_JOB;
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_STATUS + "REJECTED "
                + PREFIX_DEADLINE + "2025-12-31T23:59";

        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder()
                .withStatus(JobApplication.Status.REJECTED)
                .withDeadline(LocalDateTime.of(2025, 12, 31, 23, 59))
                .build();

        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // company name
        Index targetIndex = INDEX_THIRD_JOB;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "Amazon";
        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder().withCompanyName("Amazon").build();
        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // role
        userInput = targetIndex.getOneBased() + " " + PREFIX_ROLE + "Data Scientist";
        descriptor = new UpdateJobDescriptorBuilder().withRole("Data Scientist").build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // status
        userInput = targetIndex.getOneBased() + " " + PREFIX_STATUS + "APPLIED";
        descriptor = new UpdateJobDescriptorBuilder().withStatus(JobApplication.Status.APPLIED).build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // deadline
        userInput = targetIndex.getOneBased() + " " + PREFIX_DEADLINE + "2026-03-15T12:00";
        descriptor = new UpdateJobDescriptorBuilder()
                .withDeadline(LocalDateTime.of(2026, 3, 15, 12, 0)).build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + " " + PREFIX_TAG + "remote";
        descriptor = new UpdateJobDescriptorBuilder().withTags("remote").build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // Multiple company names - should fail due to duplicate prefix validation
        Index targetIndex = INDEX_FIRST_JOB;
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_NAME + "Google "
                + PREFIX_NAME + "Meta";

        assertParseFailure(parser, userInput, "Multiple values specified for the following single-valued field(s): n/");

        // Multiple roles
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_ROLE + "SWE "
                + PREFIX_ROLE + "Engineer";

        assertParseFailure(parser, userInput, "Multiple values specified for the following single-valued field(s): r/");

        // Multiple statuses
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_STATUS + "APPLIED "
                + PREFIX_STATUS + "REJECTED";

        assertParseFailure(parser, userInput, "Multiple values specified for the following single-valued field(s): s/");

        // Multiple deadlines
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_DEADLINE + "2025-12-31T23:59 "
                + PREFIX_DEADLINE + "2026-01-01T00:00";

        assertParseFailure(parser, userInput, "Multiple values specified for the following single-valued field(s): d/");
    }

    @Test
    public void parse_multipleTags_success() {
        Index targetIndex = INDEX_FIRST_JOB;
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_TAG + "urgent "
                + PREFIX_TAG + "remote "
                + PREFIX_TAG + "fulltime";

        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder()
                .withTags("urgent", "remote", "fulltime")
                .build();

        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tooManyTags_failure() {
        Index targetIndex = INDEX_FIRST_JOB;
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_TAG + "tag1 "
                + PREFIX_TAG + "tag2 "
                + PREFIX_TAG + "tag3 "
                + PREFIX_TAG + "tag4";

        assertParseFailure(parser, userInput, "Maximum number of tags per application is: 3");
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_JOB;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_TAG;

        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder().withTags().build();
        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_statusCaseInsensitive_success() {
        Index targetIndex = INDEX_FIRST_JOB;

        // lowercase
        String userInput = targetIndex.getOneBased() + " " + PREFIX_STATUS + "applied";
        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder()
                .withStatus(JobApplication.Status.APPLIED).build();
        UpdateJobCommand expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // mixed case
        userInput = targetIndex.getOneBased() + " " + PREFIX_STATUS + "InProgress";
        descriptor = new UpdateJobDescriptorBuilder()
                .withStatus(JobApplication.Status.INPROGRESS).build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // uppercase
        userInput = targetIndex.getOneBased() + " " + PREFIX_STATUS + "REJECTED";
        descriptor = new UpdateJobDescriptorBuilder()
                .withStatus(JobApplication.Status.REJECTED).build();
        expectedCommand = new UpdateJobCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
