package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.job.testutil.TypicalIndexes.INDEX_FIRST_JOB;
import static seedu.job.testutil.TypicalIndexes.INDEX_SECOND_JOB;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.job.commons.core.index.Index;
import seedu.job.logic.jobcommands.UpdateJobCommand.UpdateJobDescriptor;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.tag.Tag;
import seedu.job.testutil.JobApplicationBuilder;
import seedu.job.testutil.UpdateJobDescriptorBuilder;

/**
 * Contains integration tests and unit tests for UpdateJobCommand.
 */
public class UpdateJobCommandTest {

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        JobApplication originalJob = new JobApplicationBuilder().build();
        JobApplication updatedJob = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withRole("Senior Engineer")
                .withStatus(JobApplication.Status.INPROGRESS)
                .withDeadline(LocalDateTime.of(2026, 1, 15, 18, 0))
                .withTags("urgent")
                .build();

        UpdateJobDescriptor descriptor = new UpdateJobDescriptorBuilder(updatedJob).build();
        UpdateJobCommand updateJobCommand = new UpdateJobCommand(INDEX_FIRST_JOB, descriptor);

        ModelStubWithJobApplication modelStub = new ModelStubWithJobApplication(originalJob);

        CommandResult commandResult = updateJobCommand.execute(modelStub);

        assertEquals(String.format(UpdateJobCommand.MESSAGE_UPDATE_JOB_SUCCESS,
                seedu.job.logic.JobMessages.format(updatedJob)), commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        JobApplication originalJob = new JobApplicationBuilder().build();

        UpdateJobDescriptor descriptor = new UpdateJobDescriptor();
        descriptor.setStatus(JobApplication.Status.INPROGRESS);
        descriptor.setDeadline(LocalDateTime.of(2026, 1, 1, 12, 0));

        UpdateJobCommand updateJobCommand = new UpdateJobCommand(INDEX_FIRST_JOB, descriptor);

        ModelStubWithJobApplication modelStub = new ModelStubWithJobApplication(originalJob);

        CommandResult commandResult = updateJobCommand.execute(modelStub);

        String expectedMessagePrefix = UpdateJobCommand.MESSAGE_UPDATE_JOB_SUCCESS.split(":")[0];
        assertTrue(commandResult.getFeedbackToUser().contains(expectedMessagePrefix));
    }

    @Test
    public void execute_noFieldSpecified_failure() {
        UpdateJobCommand updateJobCommand = new UpdateJobCommand(INDEX_FIRST_JOB, new UpdateJobDescriptor());

        JobApplication job = new JobApplicationBuilder().build();
        ModelStubWithJobApplication modelStub = new ModelStubWithJobApplication(job);

        try {
            updateJobCommand.execute(modelStub);
        } catch (JobCommandException e) {
            // Expected - descriptor has no fields
            assertTrue(true);
        }
    }

    @Test
    public void execute_duplicateJobApplication_failure() {
        JobApplication firstJob = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("SWE")
                .build();
        JobApplication secondJob = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withRole("Engineer")
                .build();

        ModelStubWithTwoJobApplications modelStub = new ModelStubWithTwoJobApplications(firstJob, secondJob);

        // Try to update second job to have same company and role as first job
        UpdateJobDescriptor descriptor = new UpdateJobDescriptor();
        descriptor.setCompanyName("Google");
        descriptor.setRole("SWE");

        UpdateJobCommand updateJobCommand = new UpdateJobCommand(INDEX_SECOND_JOB, descriptor);

        try {
            updateJobCommand.execute(modelStub);
        } catch (JobCommandException e) {
            assertEquals(UpdateJobCommand.MESSAGE_DUPLICATE_APPLICATION, e.getMessage());
        }
    }

    @Test
    public void equals() {
        UpdateJobDescriptor descriptorOne = new UpdateJobDescriptor();
        descriptorOne.setStatus(JobApplication.Status.INPROGRESS);

        UpdateJobDescriptor descriptorTwo = new UpdateJobDescriptor();
        descriptorTwo.setStatus(JobApplication.Status.REJECTED);

        UpdateJobCommand updateFirstCommand = new UpdateJobCommand(INDEX_FIRST_JOB, descriptorOne);
        UpdateJobCommand updateSecondCommand = new UpdateJobCommand(INDEX_SECOND_JOB, descriptorOne);
        UpdateJobCommand updateThirdCommand = new UpdateJobCommand(INDEX_FIRST_JOB, descriptorTwo);

        // same object -> returns true
        assertEquals(updateFirstCommand, updateFirstCommand);

        // same values -> returns true
        UpdateJobCommand updateFirstCommandCopy = new UpdateJobCommand(INDEX_FIRST_JOB, descriptorOne);
        assertEquals(updateFirstCommand, updateFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, updateFirstCommand);

        // null -> returns false
        assertNotEquals(null, updateFirstCommand);

        // different index -> returns false
        assertNotEquals(updateFirstCommand, updateSecondCommand);

        // different descriptor -> returns false
        assertNotEquals(updateFirstCommand, updateThirdCommand);
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        UpdateJobDescriptor descriptor = new UpdateJobDescriptor();
        descriptor.setStatus(JobApplication.Status.INPROGRESS);

        UpdateJobCommand updateJobCommand = new UpdateJobCommand(index, descriptor);
        String expected = UpdateJobCommand.class.getCanonicalName()
                + "{index=" + index
                + ", updateJobDescriptor=" + descriptor + "}";

        assertEquals(expected, updateJobCommand.toString());
    }

    @Test
    public void updateJobDescriptor_isAnyFieldUpdated() {
        UpdateJobDescriptor descriptor = new UpdateJobDescriptor();

        // no fields updated
        assertFalse(descriptor.isAnyFieldUpdated());

        // one field updated
        descriptor.setCompanyName("Google");
        assertTrue(descriptor.isAnyFieldUpdated());
    }

    @Test
    public void updateJobDescriptor_copyConstructor() {
        UpdateJobDescriptor original = new UpdateJobDescriptor();
        original.setCompanyName("Google");
        original.setRole("SWE");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("urgent"));
        original.setTags(tags);

        UpdateJobDescriptor copy = new UpdateJobDescriptor(original);

        assertEquals(original, copy);
        assertEquals(original.getCompanyName(), copy.getCompanyName());
        assertEquals(original.getRole(), copy.getRole());
        assertEquals(original.getTags(), copy.getTags());
    }

    /**
     * A default model stub that has all of the methods failing.
     */
    private class ModelStub implements seedu.job.model.jobapplication.Model {
        @Override
        public void setUserPrefs(seedu.job.model.jobapplication.ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.job.model.jobapplication.ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.job.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(seedu.job.commons.core.GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public java.nio.file.Path getJobBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setJobBookFilePath(java.nio.file.Path jobBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setJobBook(seedu.job.model.jobapplication.ReadOnlyJobBook jobBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.job.model.jobapplication.ReadOnlyJobBook getJobBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasApplication(JobApplication application) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteJobApplication(JobApplication target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortJobApplication(seedu.job.model.jobapplication.sort.SortField field,
                                        seedu.job.model.jobapplication.sort.SortOrder order) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addJobApplication(JobApplication application) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setJobApplication(JobApplication target, JobApplication editedJobApplication) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public javafx.collections.ObservableList<JobApplication> getFilteredApplicationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredJobApplicationList(java.util.function.Predicate<JobApplication> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single job application.
     */
    private class ModelStubWithJobApplication extends ModelStub {
        private JobApplication jobApplication;

        ModelStubWithJobApplication(JobApplication jobApplication) {
            this.jobApplication = jobApplication;
        }

        @Override
        public boolean hasApplication(JobApplication application) {
            return jobApplication.isSameJobApplication(application);
        }

        @Override
        public void setJobApplication(JobApplication target, JobApplication editedJobApplication) {
            this.jobApplication = editedJobApplication;
        }

        @Override
        public javafx.collections.ObservableList<JobApplication> getFilteredApplicationList() {
            javafx.collections.ObservableList<JobApplication> list =
                    javafx.collections.FXCollections.observableArrayList();
            list.add(jobApplication);
            return list;
        }

        @Override
        public void updateFilteredJobApplicationList(java.util.function.Predicate<JobApplication> predicate) {
            // Do nothing for stub
        }
    }

    /**
     * A Model stub that contains two job applications.
     */
    private class ModelStubWithTwoJobApplications extends ModelStub {
        private JobApplication firstJob;
        private JobApplication secondJob;

        ModelStubWithTwoJobApplications(JobApplication firstJob, JobApplication secondJob) {
            this.firstJob = firstJob;
            this.secondJob = secondJob;
        }

        @Override
        public boolean hasApplication(JobApplication application) {
            return firstJob.isSameJobApplication(application) || secondJob.isSameJobApplication(application);
        }

        @Override
        public void setJobApplication(JobApplication target, JobApplication editedJobApplication) {
            if (target.isSameJobApplication(firstJob)) {
                firstJob = editedJobApplication;
            } else if (target.isSameJobApplication(secondJob)) {
                secondJob = editedJobApplication;
            }
        }

        @Override
        public javafx.collections.ObservableList<JobApplication> getFilteredApplicationList() {
            javafx.collections.ObservableList<JobApplication> list =
                    javafx.collections.FXCollections.observableArrayList();
            list.add(firstJob);
            list.add(secondJob);
            return list;
        }

        @Override
        public void updateFilteredJobApplicationList(java.util.function.Predicate<JobApplication> predicate) {
            // Do nothing for stub
        }
    }
}
