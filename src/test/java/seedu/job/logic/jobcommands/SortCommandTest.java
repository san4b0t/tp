package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.job.model.jobapplication.sort.SortField.DEADLINE;
import static seedu.job.model.jobapplication.sort.SortField.ROLE;
import static seedu.job.model.jobapplication.sort.SortOrder.ASCENDING;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.job.commons.core.GuiSettings;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.ReadOnlyJobBook;
import seedu.job.model.jobapplication.ReadOnlyUserPrefs;
import seedu.job.model.jobapplication.UserPrefs;
import seedu.job.model.jobapplication.sort.SortField;
import seedu.job.model.jobapplication.sort.SortOrder;
import seedu.job.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for {@link SortCommand}.
 */
public class SortCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void executeSortByDeadlineAscendingSuccess() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withDeadline(LocalDateTime.of(2025, 12, 1, 12, 0))
                .build();
        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withDeadline(LocalDateTime.of(2025, 11, 1, 12, 0))
                .build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);

        SortCommand sortCommand = new SortCommand(DEADLINE, ASCENDING);
        sortCommand.execute(model);

        assertEquals(job2, model.getFilteredApplicationList().get(0)); // earlier date first
        assertEquals(job1, model.getFilteredApplicationList().get(1));
    }


    @Test
    public void executeSortByRoleStableSortSuccess() throws Exception {
        JobApplication job1 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .build();

        JobApplication job2 = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Product Manager")
                .build();

        model.addJobApplication(job1);
        model.addJobApplication(job2);

        SortCommand sortCommand = new SortCommand(ROLE, ASCENDING);
        sortCommand.execute(model);

        assertEquals(job2, model.getFilteredApplicationList().get(0));
        assertEquals(job1, model.getFilteredApplicationList().get(1));
    }

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(null, ASCENDING));
    }

    @Test
    public void constructor_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(SortField.COMPANY, null));
    }

    @Test
    public void execute_callsModelAndReturnsMessage() throws JobCommandException {
        // Arrange
        SortField field = SortField.COMPANY;
        SortOrder order = SortOrder.DESCENDING;
        SortCommand cmd = new SortCommand(field, order);
        ModelStubRecordingSort model = new ModelStubRecordingSort();

        // Act
        CommandResult result = cmd.execute(model);

        // Assert
        assertEquals(field, model.lastField);
        assertEquals(order, model.lastOrder);
        assertEquals(
            String.format(SortCommand.MESSAGE_SORT_APPLICATION_SUCCESS,
                field.name().toLowerCase(), order.name().toLowerCase()),
            result.getFeedbackToUser()
        );
    }

    @Test
    public void equals() {
        SortCommand a = new SortCommand(DEADLINE, ASCENDING);
        SortCommand b = new SortCommand(DEADLINE, ASCENDING);
        SortCommand c = new SortCommand(SortField.COMPANY, ASCENDING);
        SortCommand d = new SortCommand(DEADLINE, SortOrder.DESCENDING);

        // Same values -> true
        assertEquals(a, b);

        // Same object -> true
        assertEquals(a, a);

        // Null -> false
        assertNotEquals(a, null);

        // Different type -> false
        assertNotEquals(a, "not a command");

        // Different field -> false
        assertNotEquals(a, c);

        // Different order -> false
        assertNotEquals(a, d);
    }

    @Test
    public void toStringMethod() {
        SortField field = SortField.ROLE;
        SortOrder order = ASCENDING;
        SortCommand cmd = new SortCommand(field, order);

        String expected = SortCommand.class.getCanonicalName()
            + "{field=" + field
            + ", order=" + order
            + "}";

        assertEquals(expected, cmd.toString());
    }

    // --------------------------------------------------------------------------------------------
    // Model stubs
    // --------------------------------------------------------------------------------------------

    /**
     * Default Model stub that throws for all methods.
     */
    private static class ModelStub implements Model {
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }
        @Override public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public Path getJobBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void setJobBookFilePath(Path jobBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void setJobBook(ReadOnlyJobBook jobBook) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public ReadOnlyJobBook getJobBook() {
            throw new AssertionError("This method should not be called.");
        }
        @Override public boolean hasApplication(JobApplication application) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void deleteJobApplication(JobApplication target) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void sortJobApplication(SortField field, SortOrder order) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void addJobApplication(JobApplication application) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void setJobApplication(JobApplication target, JobApplication editedJobApplication) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public ObservableList<JobApplication> getFilteredApplicationList() {
            throw new AssertionError("This method should not be called.");
        }
        @Override public void updateFilteredJobApplicationList(Predicate<JobApplication> predicate) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void setRecentlyModifiedApplication(JobApplication application) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public JobApplication getRecentlyModifiedApplication() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * Model stub that records the last sort field/order and provides a harmless filtered list.
     */
    private static class ModelStubRecordingSort extends ModelStub {
        private final FilteredList<JobApplication> list;
        private SortField lastField = null;
        private SortOrder lastOrder = null;

        ModelStubRecordingSort() {
            JobBook empty = new JobBook();
            this.list = new FilteredList<>(empty.getApplicationList());
        }

        @Override
        public void sortJobApplication(SortField field, SortOrder order) {
            this.lastField = field;
            this.lastOrder = order;
            // no-op on the list; behavior is verified via recorded values and message
        }

        @Override
        public ObservableList<JobApplication> getFilteredApplicationList() {
            return list;
        }

        @Override
        public void updateFilteredJobApplicationList(Predicate<JobApplication> predicate) {
        }

        @Override
        public void setRecentlyModifiedApplication(JobApplication application) {
        }
    }
}
