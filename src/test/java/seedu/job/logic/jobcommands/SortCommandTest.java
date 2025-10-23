package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.job.commons.core.GuiSettings;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ReadOnlyJobBook;
import seedu.job.model.jobapplication.ReadOnlyUserPrefs;
import seedu.job.model.jobapplication.sort.SortField;
import seedu.job.model.jobapplication.sort.SortOrder;

/**
 * Contains unit tests for {@link SortCommand}.
 */
public class SortCommandTest {

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(null, SortOrder.ASC));
    }

    @Test
    public void constructor_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(SortField.COMPANY, null));
    }

    @Test
    public void execute_callsModelAndReturnsMessage() throws JobCommandException {
        // Arrange
        SortField field = SortField.COMPANY;
        SortOrder order = SortOrder.DESC;
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
        SortCommand a = new SortCommand(SortField.DEADLINE, SortOrder.ASC);
        SortCommand b = new SortCommand(SortField.DEADLINE, SortOrder.ASC);
        SortCommand c = new SortCommand(SortField.COMPANY, SortOrder.ASC);
        SortCommand d = new SortCommand(SortField.DEADLINE, SortOrder.DESC);

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
        SortOrder order = SortOrder.ASC;
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
            // allow silently
        }
    }
}
