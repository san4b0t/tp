package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.job.testutil.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.job.logic.JobMessages;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.UserPrefs;
import seedu.job.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for AddJobCommand.
 */
public class AddJobCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullJobApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddJobCommand(null));
    }

    @Test
    public void execute_jobApplicationAcceptedByModel_addSuccessful() throws Exception {
        JobApplication validJobApplication = new JobApplicationBuilder().build();
        AddJobCommand addJobCommand = new AddJobCommand(validJobApplication);

        CommandResult commandResult = addJobCommand.execute(model);

        assertEquals(String.format(AddJobCommand.MESSAGE_SUCCESS, JobMessages.format(validJobApplication)),
                commandResult.getFeedbackToUser());
        assertTrue(model.hasApplication(validJobApplication));
    }

    @Test
    public void execute_duplicateJobApplication_throwsCommandException() {
        JobApplication validJobApplication = new JobApplicationBuilder().build();
        model.addJobApplication(validJobApplication);
        AddJobCommand addJobCommand = new AddJobCommand(validJobApplication);

        assertThrows(JobCommandException.class,
                AddJobCommand.MESSAGE_DUPLICATE_APPLICATION, () -> addJobCommand.execute(model));
    }

    @Test
    public void execute_duplicateSameCompanySameRole_throwsCommandException() {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .withStatus(JobApplication.Status.APPLIED)
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .withStatus(JobApplication.Status.INPROGRESS)
                .withDeadline(LocalDateTime.of(2026, 1, 1, 12, 0))
                .build();

        model.addJobApplication(job1);
        AddJobCommand addJobCommand = new AddJobCommand(job2);

        assertThrows(JobCommandException.class,
                AddJobCommand.MESSAGE_DUPLICATE_APPLICATION, () -> addJobCommand.execute(model));
    }

    @Test
    public void execute_differentRoles_addSuccessful() throws Exception {
        JobApplication engineerJob = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .build();
        JobApplication developerJob = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withRole("Backend Developer")
                .build();

        new AddJobCommand(engineerJob).execute(model);
        new AddJobCommand(developerJob).execute(model);

        assertTrue(model.hasApplication(engineerJob));
        assertTrue(model.hasApplication(developerJob));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_differentStatuses_addSuccessful() throws Exception {
        JobApplication appliedJob = new JobApplicationBuilder()
                .withCompanyName("Google")
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

        new AddJobCommand(appliedJob).execute(model);
        new AddJobCommand(inProgressJob).execute(model);
        new AddJobCommand(rejectedJob).execute(model);

        assertEquals(3, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_withTags_addSuccessful() throws Exception {
        JobApplication jobWithTags = new JobApplicationBuilder()
                .withTags("backend", "java", "spring")
                .build();

        CommandResult commandResult = new AddJobCommand(jobWithTags).execute(model);

        assertTrue(model.hasApplication(jobWithTags));
        assertEquals(3, model.getFilteredApplicationList().get(0).getTags().size());
    }

    @Test
    public void execute_differentDeadlines_addSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withDeadline(LocalDateTime.of(2025, 12, 31, 23, 59))
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withDeadline(LocalDateTime.of(2025, 11, 30, 18, 0))
                .build();

        new AddJobCommand(job1).execute(model);
        new AddJobCommand(job2).execute(model);

        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_sameCompanyDifferentRole_addSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Product Manager")
                .build();

        new AddJobCommand(job1).execute(model);
        new AddJobCommand(job2).execute(model);

        assertTrue(model.hasApplication(job1));
        assertTrue(model.hasApplication(job2));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_sameRoleDifferentCompany_addSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withRole("Software Engineer")
                .build();

        new AddJobCommand(job1).execute(model);
        new AddJobCommand(job2).execute(model);

        assertTrue(model.hasApplication(job1));
        assertTrue(model.hasApplication(job2));
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_multipleJobsSequentially_addSuccessful() throws Exception {
        JobApplication job1 = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication job2 = new JobApplicationBuilder().withCompanyName("Meta").build();
        JobApplication job3 = new JobApplicationBuilder().withCompanyName("Amazon").build();

        new AddJobCommand(job1).execute(model);
        new AddJobCommand(job2).execute(model);
        new AddJobCommand(job3).execute(model);

        assertEquals(3, model.getFilteredApplicationList().size());
    }

    @Test
    public void equals() {
        JobApplication google = new JobApplicationBuilder().withCompanyName("Google").build();
        JobApplication meta = new JobApplicationBuilder().withCompanyName("Meta").build();
        AddJobCommand addGoogleCommand = new AddJobCommand(google);
        AddJobCommand addMetaCommand = new AddJobCommand(meta);

        assertTrue(addGoogleCommand.equals(addGoogleCommand));

        AddJobCommand addGoogleCommandCopy = new AddJobCommand(google);
        assertTrue(addGoogleCommand.equals(addGoogleCommandCopy));

        assertFalse(addGoogleCommand.equals(1));

        assertFalse(addGoogleCommand.equals(null));

        assertFalse(addGoogleCommand.equals(addMetaCommand));
    }

    @Test
    public void toString_validJobApplication_success() {
        JobApplication jobApplication = new JobApplicationBuilder().build();
        AddJobCommand addCommand = new AddJobCommand(jobApplication);

        assertTrue(addCommand.toString().contains("toAdd"));
    }
}
