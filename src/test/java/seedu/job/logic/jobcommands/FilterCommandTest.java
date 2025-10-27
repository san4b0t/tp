package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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

        // Add sample job applications with various roles, statuses, deadlines, and tags
        HashSet<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("backend"));
        tags1.add(new Tag("java"));

        HashSet<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("frontend"));
        tags2.add(new Tag("react"));

        HashSet<Tag> tags3 = new HashSet<>();
        tags3.add(new Tag("fullstack"));

        HashSet<Tag> tags4 = new HashSet<>();
        tags4.add(new Tag("backend-engineer"));

        HashSet<Tag> tags5 = new HashSet<>();
        tags5.add(new Tag("data"));

        JobApplication app1 = new JobApplication(
                "Google",
                "Software Engineer",
                LocalDateTime.of(2025, 12, 31, 23, 59),
                JobApplication.Status.APPLIED,
                tags1
        );

        JobApplication app2 = new JobApplication(
                "Meta",
                "Backend Developer",
                LocalDateTime.of(2025, 11, 30, 18, 30),
                JobApplication.Status.INPROGRESS,
                tags2
        );

        JobApplication app3 = new JobApplication(
                "Amazon",
                "Frontend Engineer",
                LocalDateTime.of(2025, 10, 15, 9, 0),
                JobApplication.Status.REJECTED,
                tags3
        );

        JobApplication app4 = new JobApplication(
                "Apple",
                "Machine Learning Engineer",
                LocalDateTime.of(2025, 12, 31, 10, 0),
                JobApplication.Status.APPLIED,
                tags4
        );

        JobApplication app5 = new JobApplication(
                "Netflix",
                "Senior Software Developer",
                LocalDateTime.of(2025, 11, 30, 23, 59),
                JobApplication.Status.INPROGRESS,
                tags5
        );

        jobBook.addApplication(app1);
        jobBook.addApplication(app2);
        jobBook.addApplication(app3);
        jobBook.addApplication(app4);
        jobBook.addApplication(app5);

        model = new ModelManager(jobBook, new UserPrefs());
        expectedModel = new ModelManager(jobBook, new UserPrefs());
    }

    @Test
    public void execute_showAllPredicate_success() {
        Predicate<JobApplication> showAllPredicate = app -> true;
        FilterCommand command = new FilterCommand(showAllPredicate);

        expectedModel.updateFilteredJobApplicationList(showAllPredicate);

        CommandResult result = command.execute(model);

        assertEquals(5, model.getFilteredApplicationList().size());
    }

    // ============== Tag Filtering Tests ==============

    @Test
    public void execute_filterByTagExactMatch_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("backend"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Google (has "backend"), Apple (has "backend-engineer")
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByTagContainsKeyword_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("engineer"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Apple (has "backend-engineer")
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Apple", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    @Test
    public void execute_filterByTagPartialWord_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("front"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Meta (has "frontend")
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Meta", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    @Test
    public void execute_filterByTagCaseInsensitive_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("java"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Google (has "java")
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Google", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    @Test
    public void execute_filterByTagNoMatch_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("python"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByTagMultipleMatches_success() {
        Predicate<JobApplication> predicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("stack"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Amazon (has "fullstack")
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Amazon", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    // ============== Status Filtering Tests ==============

    @Test
    public void execute_filterByStatusApplied_success() {
        Predicate<JobApplication> predicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Google, Apple
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByStatusInProgress_success() {
        Predicate<JobApplication> predicate = app ->
                app.getStatus().equals(JobApplication.Status.INPROGRESS);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Meta, Netflix
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByStatusRejected_success() {
        Predicate<JobApplication> predicate = app ->
                app.getStatus().equals(JobApplication.Status.REJECTED);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Amazon
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Amazon", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    // ============== Deadline Filtering Tests ==============

    @Test
    public void execute_filterByDeadlineExactDate_success() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        Predicate<JobApplication> predicate = app ->
                app.getDeadline().toLocalDate().equals(date);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Google and Apple (both have deadline on 2025-12-31 but different times)
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByDeadlineDifferentDate_success() {
        LocalDate date = LocalDate.of(2025, 11, 30);
        Predicate<JobApplication> predicate = app ->
                app.getDeadline().toLocalDate().equals(date);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Meta and Netflix (both have deadline on 2025-11-30 but different times)
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByDeadlineNoMatch_success() {
        LocalDate date = LocalDate.of(2026, 1, 1);
        Predicate<JobApplication> predicate = app ->
                app.getDeadline().toLocalDate().equals(date);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByDeadlineIgnoresTime_success() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        Predicate<JobApplication> predicate = app ->
                app.getDeadline().toLocalDate().equals(date);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match both Google (23:59) and Apple (10:00) on same date
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterByDeadlineSingleMatch_success() {
        LocalDate date = LocalDate.of(2025, 10, 15);
        Predicate<JobApplication> predicate = app ->
                app.getDeadline().toLocalDate().equals(date);
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        // Should match: Amazon only
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Amazon", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    // ============== Combined Filter Tests ==============

    @Test
    public void execute_filterNoResults_success() {
        Predicate<JobApplication> predicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED)
                        && app.getTags().stream().anyMatch(tag -> tag.tagName.toLowerCase().contains("nonexistent"));
        FilterCommand command = new FilterCommand(predicate);

        CommandResult result = command.execute(model);

        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_filterAfterFilter_success() {
        // First filter by APPLIED status
        Predicate<JobApplication> firstPredicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED);
        FilterCommand firstCommand = new FilterCommand(firstPredicate);
        firstCommand.execute(model);

        assertEquals(2, model.getFilteredApplicationList().size());

        // Then filter by tag "backend"
        Predicate<JobApplication> secondPredicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("backend"));
        FilterCommand secondCommand = new FilterCommand(secondPredicate);
        secondCommand.execute(model);

        // Should match: Google and Apple (both APPLIED with backend-related tags)
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_resetFilterShowsAll_success() {
        // First filter
        Predicate<JobApplication> filterPredicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED);
        FilterCommand filterCommand = new FilterCommand(filterPredicate);
        filterCommand.execute(model);

        assertEquals(2, model.getFilteredApplicationList().size());

        // Reset filter
        Predicate<JobApplication> showAllPredicate = app -> true;
        FilterCommand resetCommand = new FilterCommand(showAllPredicate);
        resetCommand.execute(model);

        assertEquals(5, model.getFilteredApplicationList().size());
    }

    // ============== Equals Tests ==============

    @Test
    public void equals() {
        Predicate<JobApplication> firstPredicate = app ->
                app.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains("backend"));
        Predicate<JobApplication> secondPredicate = app ->
                app.getStatus().equals(JobApplication.Status.APPLIED);

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void toString_validPredicate_success() {
        Predicate<JobApplication> predicate = app -> true;
        FilterCommand command = new FilterCommand(predicate);

        assertTrue(command.toString().contains("predicate"));
    }
}
