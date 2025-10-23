package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.UserPrefs;
import seedu.job.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for SaveCommand.
 */
public class SaveCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
        expectedModel = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void execute_save_success() {
        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertEquals("Saved job applications successfully!", commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_saveFlagsSetCorrectly() {
        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertFalse(commandResult.isShowHelp());
        assertFalse(commandResult.isExit());
    }

    @Test
    public void execute_modelUnchanged() {
        SaveCommand saveCommand = new SaveCommand();
        saveCommand.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList(), model.getFilteredApplicationList());
    }

    @Test
    public void execute_emptyModel_success() {
        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertEquals("Saved job applications successfully!", commandResult.getFeedbackToUser());
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_modelWithApplications_success() {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        model.addJobApplication(job1);
        model.addJobApplication(job2);

        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertEquals("Saved job applications successfully!", commandResult.getFeedbackToUser());
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_multipleExecutions_success() {
        SaveCommand saveCommand = new SaveCommand();

        CommandResult result1 = saveCommand.execute(model);
        CommandResult result2 = saveCommand.execute(model);
        CommandResult result3 = saveCommand.execute(model);

        assertEquals(result1.getFeedbackToUser(), result2.getFeedbackToUser());
        assertEquals(result2.getFeedbackToUser(), result3.getFeedbackToUser());
        assertEquals("Saved job applications successfully!", result1.getFeedbackToUser());
    }

    @Test
    public void execute_afterAddingApplications_modelPreserved() {
        JobApplication job = new JobApplicationBuilder().withCompanyName("Google").build();
        model.addJobApplication(job);

        int sizeBeforeSave = model.getFilteredApplicationList().size();

        SaveCommand saveCommand = new SaveCommand();
        saveCommand.execute(model);

        assertEquals(sizeBeforeSave, model.getFilteredApplicationList().size());
        assertTrue(model.hasApplication(job));
    }

    @Test
    public void execute_afterDeletingApplications_modelPreserved() {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        model.addJobApplication(job1);
        model.addJobApplication(job2);
        model.deleteJobApplication(job1);

        SaveCommand saveCommand = new SaveCommand();
        saveCommand.execute(model);

        assertEquals(1, model.getFilteredApplicationList().size());
        assertFalse(model.hasApplication(job1));
        assertTrue(model.hasApplication(job2));
    }

    @Test
    public void execute_withDifferentStatuses_success() {
        JobApplication appliedJob = new JobApplicationBuilder()
                .withStatus(JobApplication.Status.APPLIED)
                .build();
        JobApplication inProgressJob = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withStatus(JobApplication.Status.INPROGRESS)
                .build();

        model.addJobApplication(appliedJob);
        model.addJobApplication(inProgressJob);

        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertEquals("Saved job applications successfully!", commandResult.getFeedbackToUser());
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_withTags_success() {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java", "spring")
                .build();
        model.addJobApplication(jobWithTags);

        SaveCommand saveCommand = new SaveCommand();
        CommandResult commandResult = saveCommand.execute(model);

        assertEquals("Saved job applications successfully!", commandResult.getFeedbackToUser());
        assertEquals(3, model.getFilteredApplicationList().get(0).getTags().size());
    }

    @Test
    public void toString_success() {
        SaveCommand saveCommand = new SaveCommand();
        String result = saveCommand.toString();

        assertTrue(result != null);
    }
}
