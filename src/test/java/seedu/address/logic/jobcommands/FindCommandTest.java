package seedu.address.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.jobapplication.JobApplication;
import seedu.address.model.jobapplication.JobBook;
import seedu.address.model.jobapplication.Model;
import seedu.address.model.jobapplication.ModelManager;
import seedu.address.model.jobapplication.NameContainsKeywordsPredicate;
import seedu.address.model.jobapplication.UserPrefs;
import seedu.address.testutil.JobApplicationBuilder;

/**
 * Contains unit tests for FindCommand.
 */
public class FindCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        JobBook jobApplicationBook = new JobBook();

        JobApplication google = new JobApplicationBuilder()
                .withCompanyName("Google")
                .withRole("Software Engineer")
                .build();

        JobApplication meta = new JobApplicationBuilder()
                .withCompanyName("Meta")
                .withRole("Backend Developer")
                .build();

        JobApplication amazon = new JobApplicationBuilder()
                .withCompanyName("Amazon")
                .withRole("Frontend Engineer")
                .build();

        JobApplication tiktok = new JobApplicationBuilder()
                .withCompanyName("TikTok")
                .withRole("Data Scientist")
                .build();

        JobApplication janeStreet = new JobApplicationBuilder()
                .withCompanyName("Jane Street")
                .withRole("Quantitative Trader")
                .build();

        JobApplication hrt = new JobApplicationBuilder()
                .withCompanyName("HRT")
                .withRole("Software Developer")
                .build();

        jobApplicationBook.addApplication(google);
        jobApplicationBook.addApplication(meta);
        jobApplicationBook.addApplication(amazon);
        jobApplicationBook.addApplication(tiktok);
        jobApplicationBook.addApplication(janeStreet);
        jobApplicationBook.addApplication(hrt);

        model = new ModelManager(jobApplicationBook, new UserPrefs());
        expectedModel = new ModelManager(jobApplicationBook, new UserPrefs());
    }

    @Test
    public void execute_zeroKeywords_noJobApplicationFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredApplicationList(), model.getFilteredApplicationList());
    }

    @Test
    public void execute_singleKeyword_multipleJobApplicationsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Street");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredApplicationList(), model.getFilteredApplicationList());
    }

    @Test
    public void execute_multipleKeywords_multipleJobApplicationsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("TikTok Jane HRT");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredApplicationList(), model.getFilteredApplicationList());
        assertEquals(3, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_singleKeywordExactMatch_oneJobApplicationFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Google");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Google", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    @Test
    public void execute_keywordCaseInsensitive_jobApplicationsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("google");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getFilteredApplicationList().size());
        assertEquals("Google", model.getFilteredApplicationList().get(0).getCompanyName());
    }

    @Test
    public void execute_partialKeyword_noJobApplicationFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate("Goog");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_nonExistentKeyword_noJobApplicationFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate("Netflix");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(0, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_multipleKeywordsSomeMatch_matchingJobApplicationsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate predicate = preparePredicate("Google Netflix Meta");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(2, model.getFilteredApplicationList().size());
    }

    @Test
    public void execute_duplicateKeywords_jobApplicationsFoundOnce() {
        String expectedMessage = String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Google Google");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredJobApplicationList(predicate);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getFilteredApplicationList().size());
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Google"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Meta"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void toString_validPredicate_success() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Google");
        FindCommand command = new FindCommand(predicate);

        assertTrue(command.toString().contains("predicate"));
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
