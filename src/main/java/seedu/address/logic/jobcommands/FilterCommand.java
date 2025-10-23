package seedu.address.logic.jobcommands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.jobapplication.JobApplication;
import seedu.address.model.jobapplication.Model;

/**
 * Represents a command that filters and lists all job applications
 * whose role, status, deadline or tags matches the keyword.
 * Keyword matching is case-insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters all job applications by tags, role, status or application deadline "
            + "that is input as keyword with the appropriate t/, r/, s/ or d/ flag respectively (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Tag and role filters match if the keyword is contained in the field.\n"
            + "Deadline filter matches by date only (format: yyyy-MM-dd), ignoring time.\n"
            + "To remove filters and show all job applications, use: " + COMMAND_WORD + " none\n"
            + "Parameters: FLAG [KEYWORD]... OR none\n"
            + "Example: " + COMMAND_WORD + " t/engineer, " + COMMAND_WORD + " d/2025-12-31";

    private static Logger logger = Logger.getLogger("Filter");

    private final Predicate<JobApplication> predicate;

    /**
     * Creates a FilterCommand to filter job applications based on the given predicate.
     *
     * @param predicate The predicate to filter job applications
     */
    public FilterCommand(Predicate<JobApplication> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        logger.log(Level.INFO, "using predicate to filter from saved job applications");
        model.updateFilteredJobApplicationList(predicate);
        logger.log(Level.INFO, "end of filtering, return command result");
        return new CommandResult(
                String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW,
                        model.getFilteredApplicationList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
