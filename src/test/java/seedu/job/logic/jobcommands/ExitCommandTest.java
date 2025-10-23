package seedu.job.logic.jobcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.job.model.jobapplication.JobBook;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;
import seedu.job.model.jobapplication.UserPrefs;

/**
 * Contains unit tests for ExitCommand.
 */
public class ExitCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new JobBook(), new UserPrefs());
        expectedModel = new ModelManager(new JobBook(), new UserPrefs());
    }

    @Test
    public void execute_exit_success() {
        ExitCommand exitCommand = new ExitCommand();
        CommandResult commandResult = exitCommand.execute(model);

        assertEquals(ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT, commandResult.getFeedbackToUser());
        assertTrue(commandResult.isExit());
    }

    @Test
    public void execute_exitFlagsSetCorrectly() {
        ExitCommand exitCommand = new ExitCommand();
        CommandResult commandResult = exitCommand.execute(model);

        assertFalse(commandResult.isShowHelp());
        assertTrue(commandResult.isExit());
    }

    @Test
    public void execute_modelUnchanged() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList(), model.getFilteredApplicationList());
    }

    @Test
    public void execute_multipleExecutions_success() {
        ExitCommand exitCommand = new ExitCommand();

        CommandResult result1 = exitCommand.execute(model);
        CommandResult result2 = exitCommand.execute(model);
        CommandResult result3 = exitCommand.execute(model);

        assertEquals(result1.getFeedbackToUser(), result2.getFeedbackToUser());
        assertEquals(result2.getFeedbackToUser(), result3.getFeedbackToUser());
        assertTrue(result1.isExit());
        assertTrue(result2.isExit());
        assertTrue(result3.isExit());
    }

    @Test
    public void execute_withExistingApplications_modelUnchanged() {
        model = new ModelManager(new JobBook(), new UserPrefs());
        expectedModel = new ModelManager(new JobBook(), new UserPrefs());

        ExitCommand exitCommand = new ExitCommand();
        exitCommand.execute(model);

        assertEquals(expectedModel.getFilteredApplicationList().size(),
                model.getFilteredApplicationList().size());
    }

    @Test
    public void toString_success() {
        ExitCommand exitCommand = new ExitCommand();
        String result = exitCommand.toString();

        assertTrue(result != null);
    }
}
