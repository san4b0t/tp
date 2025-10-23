package seedu.job.logic.commands;

import static seedu.job.logic.jobcommands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        //assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
