package seedu.address.logic.commands;

import static seedu.address.logic.jobcommands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import seedu.address.model.jobapplication.Model;
import seedu.address.model.jobapplication.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        //assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
