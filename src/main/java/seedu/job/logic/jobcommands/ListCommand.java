package seedu.job.logic.jobcommands;

import static java.util.Objects.requireNonNull;
import static seedu.job.model.jobapplication.Model.PREDICATE_SHOW_ALL_APPLICATIONS;

import seedu.job.logic.JobMessages;
import seedu.job.model.jobapplication.Model;

/**
 * Lists all job applications in the job list.
 * Equivalent to "filter none".
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all job applications.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
        model.setRecentlyModifiedApplication(null);
        return new CommandResult(
                String.format(JobMessages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW,
                        model.getFilteredApplicationList().size()));
    }
}
