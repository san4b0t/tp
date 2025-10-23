package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.job.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.job.commons.core.index.Index;
import seedu.job.logic.JobMessages;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.UserPrefs;
import seedu.job.model.tag.Tag;
import seedu.job.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for UntagJobCommand.
 */
public class UntagJobCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("backend"));
        assertThrows(NullPointerException.class, () -> new UntagJobCommand(null, tags));
    }

    @Test
    public void constructor_nullTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UntagJobCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_validIndexAndTag_untagSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);
        CommandResult commandResult = untagCommand.execute(model);

        assertEquals(UntagJobCommand.MESSAGE_TAG_REMOVAL_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(1, model.getFilteredApplicationList().get(0).getTags().size());
        assertFalse(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("backend")));
        assertTrue(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("java")));
    }

    @Test
    public void execute_removeMultipleTags_untagSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java", "spring")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));
        tagsToRemove.add(new Tag("java"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);
        untagCommand.execute(model);

        assertEquals(1, model.getFilteredApplicationList().get(0).getTags().size());
        assertTrue(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("spring")));
        assertFalse(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("backend")));
        assertFalse(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("java")));
    }

    @Test
    public void execute_removeAllTags_untagSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));
        tagsToRemove.add(new Tag("java"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);
        untagCommand.execute(model);

        assertEquals(0, model.getFilteredApplicationList().get(0).getTags().size());
    }

    @Test
    public void execute_tagDoesNotExist_throwsCommandException() {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("frontend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);

        assertThrows(JobCommandException.class,
                UntagJobCommand.MESSAGE_MISSING_TAG, () -> untagCommand.execute(model));
    }

    @Test
    public void execute_someTagsDoNotExist_throwsCommandException() {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));
        tagsToRemove.add(new Tag("python"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);

        assertThrows(JobCommandException.class,
                UntagJobCommand.MESSAGE_MISSING_TAG, () -> untagCommand.execute(model));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(2), tagsToRemove);

        assertThrows(JobCommandException.class,
                JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX, () -> untagCommand.execute(model));
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);

        assertThrows(JobCommandException.class,
                JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX, () -> untagCommand.execute(model));
    }

    @Test
    public void execute_jobWithNoTags_throwsCommandException() {
        JobApplication jobWithoutTags = new JobApplicationBuilder().build();
        model.addJobApplication(jobWithoutTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);

        assertThrows(JobCommandException.class,
                UntagJobCommand.MESSAGE_MISSING_TAG, () -> untagCommand.execute(model));
    }

    @Test
    public void execute_untagFromMultipleJobs_success() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withTags("backend", "java")
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withTags("frontend", "react")
                .build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);

        Set<Tag> tagsToRemove1 = new HashSet<>();
        tagsToRemove1.add(new Tag("backend"));

        Set<Tag> tagsToRemove2 = new HashSet<>();
        tagsToRemove2.add(new Tag("frontend"));

        new UntagJobCommand(Index.fromOneBased(1), tagsToRemove1).execute(model);
        new UntagJobCommand(Index.fromOneBased(2), tagsToRemove2).execute(model);

        assertEquals(1, model.getFilteredApplicationList().get(0).getTags().size());
        assertEquals(1, model.getFilteredApplicationList().get(1).getTags().size());
        assertTrue(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("java")));
        assertTrue(model.getFilteredApplicationList().get(1).getTags().contains(new Tag("react")));
    }

    @Test
    public void execute_untagSameTagTwice_throwsCommandException() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("backend"));

        UntagJobCommand untagCommand1 = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);
        untagCommand1.execute(model);

        UntagJobCommand untagCommand2 = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);

        assertThrows(JobCommandException.class,
                UntagJobCommand.MESSAGE_MISSING_TAG, () -> untagCommand2.execute(model));
    }

    @Test
    public void execute_specialCharacterTags_untagSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("back-end", "java.spring", "web_dev")
                .build();
        model.addJobApplication(jobWithTags);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("back-end"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tagsToRemove);
        untagCommand.execute(model);

        assertEquals(2, model.getFilteredApplicationList().get(0).getTags().size());
        assertFalse(model.getFilteredApplicationList().get(0).getTags().contains(new Tag("back-end")));
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("backend"));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("frontend"));

        UntagJobCommand untagFirstCommand = new UntagJobCommand(Index.fromOneBased(1), tags1);
        UntagJobCommand untagSecondCommand = new UntagJobCommand(Index.fromOneBased(2), tags1);
        UntagJobCommand untagDifferentTagsCommand = new UntagJobCommand(Index.fromOneBased(1), tags2);

        assertTrue(untagFirstCommand.equals(untagFirstCommand));

        UntagJobCommand untagFirstCommandCopy = new UntagJobCommand(Index.fromOneBased(1), tags1);
        assertTrue(untagFirstCommand.equals(untagFirstCommandCopy));

        assertFalse(untagFirstCommand.equals(1));

        assertFalse(untagFirstCommand.equals(null));

        assertFalse(untagFirstCommand.equals(untagSecondCommand));

        assertFalse(untagFirstCommand.equals(untagDifferentTagsCommand));
    }

    @Test
    public void toString_validCommand_success() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("backend"));

        UntagJobCommand untagCommand = new UntagJobCommand(Index.fromOneBased(1), tags);

        assertTrue(untagCommand.toString().contains("targetIndex"));
        assertTrue(untagCommand.toString().contains("tags"));
    }
}
