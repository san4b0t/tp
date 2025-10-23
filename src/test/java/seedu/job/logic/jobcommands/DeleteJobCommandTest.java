package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.job.testutil.Assert.assertThrows;

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
import seedu.job.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for DeleteJobCommand.
 */
public class DeleteJobCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_deleteSuccessful() throws Exception {
        JobApplication jobToDelete = new JobApplicationBuilder().withCompanyName("Google").build();
        model.addJobApplication(jobToDelete);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(1));
        CommandResult commandResult = deleteCommand.execute(model);

        assertEquals(String.format(DeleteJobCommand.MESSAGE_DELETE_APPLICATION_SUCCESS,
                        JobMessages.format(jobToDelete)),
                commandResult.getFeedbackToUser());
        assertFalse(model.hasApplication(jobToDelete));
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        JobApplication jobApplication = new JobApplicationBuilder().build();
        model.addJobApplication(jobApplication);

        Index outOfBoundIndex = Index.fromOneBased(2);
        DeleteJobCommand deleteCommand = new DeleteJobCommand(outOfBoundIndex);

        assertThrows(JobCommandException.class,
                JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_validIndexEmptyList_throwsCommandException() {
        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(1));

        assertThrows(JobCommandException.class,
                JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_deleteFirstOfMultiple_deleteSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        JobApplication job3 = new JobApplicationBuilder().withCompanyName("Amazon").build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);
        model.addJobApplication(job3);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(1));
        deleteCommand.execute(model);

        assertFalse(model.hasApplication(job1));
        assertTrue(model.hasApplication(job2));
        assertTrue(model.hasApplication(job3));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_deleteMiddleOfMultiple_deleteSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        JobApplication job3 = new JobApplicationBuilder().withCompanyName("Amazon").build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);
        model.addJobApplication(job3);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(2));
        deleteCommand.execute(model);

        assertTrue(model.hasApplication(job1));
        assertFalse(model.hasApplication(job2));
        assertTrue(model.hasApplication(job3));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_deleteLastOfMultiple_deleteSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        JobApplication job3 = new JobApplicationBuilder().withCompanyName("Amazon").build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);
        model.addJobApplication(job3);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(3));
        deleteCommand.execute(model);

        assertTrue(model.hasApplication(job1));
        assertTrue(model.hasApplication(job2));
        assertFalse(model.hasApplication(job3));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_deleteMultipleSequentially_deleteSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        JobApplication job3 = new JobApplicationBuilder().withCompanyName("Amazon").build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);
        model.addJobApplication(job3);

        new DeleteJobCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(2, model.getFilteredApplicationList().size());

        new DeleteJobCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(1, model.getFilteredApplicationList().size());

        new DeleteJobCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_indexBeyondListSize_throwsCommandException() {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(5));

        assertThrows(JobCommandException.class,
                JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_deleteJobWithTags_deleteSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java", "spring")
                .build();
        model.addJobApplication(jobWithTags);

        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(1));
        deleteCommand.execute(model);

        assertFalse(model.hasApplication(jobWithTags));
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_deleteJobWithDifferentStatuses_deleteSuccessful() throws Exception {
        JobApplication appliedJob = new JobApplicationBuilder()
                .withStatus(JobApplication.Status.APPLIED)
                .build();
        JobApplication inProgressJob = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withStatus(JobApplication.Status.INPROGRESS)
                .build();
        JobApplication rejectedJob = new JobApplicationBuilder()
                .withCompanyName("Amazon")
                .withStatus(JobApplication.Status.REJECTED)
                .build();

        model.addJobApplication(appliedJob);
        model.addJobApplication(inProgressJob);
        model.addJobApplication(rejectedJob);

        new DeleteJobCommand(Index.fromOneBased(2)).execute(model);

        assertTrue(model.hasApplication(appliedJob));
        assertFalse(model.hasApplication(inProgressJob));
        assertTrue(model.hasApplication(rejectedJob));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void equals() {
        DeleteJobCommand deleteFirstCommand = new DeleteJobCommand(Index.fromOneBased(1));
        DeleteJobCommand deleteSecondCommand = new DeleteJobCommand(Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteJobCommand deleteFirstCommandCopy = new DeleteJobCommand(Index.fromOneBased(1));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toString_validIndex_success() {
        DeleteJobCommand deleteCommand = new DeleteJobCommand(Index.fromOneBased(1));

        assertTrue(deleteCommand.toString().contains("targetIndex"));
    }
}
