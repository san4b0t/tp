package seedu.address.logic.jobcommands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.jobcommands.exceptions.JobCommandException;
import seedu.address.model.jobapplication.JobApplication;
import seedu.address.model.jobapplication.Model;

/**
 * Represents a command that deletes a job identified by the index number of the job in the list.
 */

public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Sorts the Application identified \n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SORT_APPLICATION_SUCCESS = "Sorted Application";

    public SortCommand() {}

    @Override
    public CommandResult execute(Model model) throws JobCommandException {
        requireNonNull(model);
        List<JobApplication> lastShownList = model.getFilteredApplicationList();

        model.sortJobApplication();
        return new CommandResult(MESSAGE_SORT_APPLICATION_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherDeleteCommand = (SortCommand) other;
        return true; //maybe add sortBy later
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }
}

