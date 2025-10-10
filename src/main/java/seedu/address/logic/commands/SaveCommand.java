package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Saves the current state of data in HustleHub
 */
public class SaveCommand extends Command {

    public static final String COMMAND_WORD = "save";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from save");
    }
}
