package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.UserPrefs;
import seedu.job.model.tag.Tag;

/**
 * Contains unit tests for FilterCommand.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        JobBook jobBook = new JobBook();

        JobApplication app1 = new JobApplication(
                "Google",
                "Software Engineer",
                LocalDateTime.of(2025, 12, 31, 23, 59),
                JobApplication.Status.APPLIED,
                new HashSet<>()
        );

        JobApplication app2 = new JobApplication(
                "Meta",
                "Backend Developer",
                LocalDateTime.of(2025, 11, 30, 23, 59),
                JobApplication.Status.INPROGRESS,
                new HashSet<>()
        );

        JobApplication app3 = new JobApplication(
                "Amazon",
                "Frontend Engineer",
                LocalDateTime.of(2025, 10, 15, 23, 59),
                JobApplication.Status.REJECTED,
                new HashSet<>()
        );

        jobBook.addApplication(app1);
        jobBook.addApplication(app2);
        jobBook.addApplication(app3);

        model = new ModelManager(jobBook, new UserPrefs());
        expectedModel = new ModelManager(jobBook, new UserPrefs());
    }

    @Test
    public void execute_showAllPredicate_success() {
        Predicate<JobApplication> showAllPredicate = app -> true;
        FilterCommand command = new FilterCommand(showAllPredicate);

        expectedModel.updateFilteredJobApplicationList(showAllPredicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList().size(),
                model.getFilteredApplicationList().size());
        assertEquals(3, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByRole_success() {
        Predicate<JobApplication> predicate = app ->
                app.getRole().toLowerCase().contains("engineer");
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList().size(),
                model.getFilteredApplicationList().size());
        assertEquals(2, model.getFilteredApplicationList().size()); // Software Engineer and Frontend Engineer
    }

    @Test
    public void execute_filterByStatus_success() {
        Predicate<JobApplication> predicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED);
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList().size(),
                model.getFilteredApplicationList().size());
        assertEquals(1, model.getFilteredApplicationList().size()); // Only Google application
    }

    @Test
    public void execute_filterByTag_success() {
        JobBook jobApplicationBook = new JobBook();

        HashSet<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("backend"));

        HashSet<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("frontend"));

        JobApplication app1 = new JobApplication(
                "Google",
                "Software Engineer",
                LocalDateTime.of(2025, 12, 31, 23, 59),
                JobApplication.Status.APPLIED,
                tags1
        );

        JobApplication app2 = new JobApplication(
                "Meta",
                "Developer",
                LocalDateTime.of(2025, 11, 30, 23, 59),
                JobApplication.Status.APPLIED,
                tags2
        );

        jobApplicationBook.addApplication(app1);
        jobApplicationBook.addApplication(app2);

        Model tagModel = new ModelManager(jobApplicationBook, new UserPrefs());

        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("backend"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(tagModel);

        assertEquals(1, tagModel.getFilteredApplicationList().size()); // Only Google with backend tag
    }

    @Test
    public void equals() {
        Predicate<JobApplication> firstPredicate = app ->
                app.getRole().toLowerCase().contains("engineer");
        Predicate<JobApplication> secondPredicate = app ->
                app.getRole().toLowerCase().contains("developer");

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);

        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        assertFalse(filterFirstCommand.equals(1));

        assertFalse(filterFirstCommand.equals(null));

        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void toString_validPredicate_success() {
        Predicate<JobApplication> predicate = app -> true;
        FilterCommand command = new FilterCommand(predicate);

        assertTrue(command.toString().contains("predicate"));
    }
}
