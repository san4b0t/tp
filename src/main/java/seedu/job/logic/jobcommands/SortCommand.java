package seedu.job.logic.jobcommands;

import static java.util.Objects.requireNonNull;

import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.sort.SortField;
import seedu.job.model.jobapplication.sort.SortOrder;

/**
 * Represents a command that deletes a job identified by the index number of the job in the list.
 */

public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts applications by a field.\n"
        + "Parameters: FIELD [ORDER]\n"
        + "  FIELD: deadline | company | role\n"
        + "  ORDER (optional): asc | desc (default: asc)\n"
        + "Examples:\n"
        + "  sort deadline\n"
        + "  sort company desc\n"
        + "  sort role asc";

    public static final String MESSAGE_SORT_APPLICATION_SUCCESS = "Sorted Application by %s (%s).";

    private final SortField field;
    private final SortOrder order;

    /**
     * Constructs a SortCommand.
     *
     * @param field sort field (deadline/company/role).
     * @param order sort order (asc/desc).
     */
    public SortCommand(SortField field, SortOrder order) {
        this.field = requireNonNull(field);
        this.order = requireNonNull(order);
    }

    @Override
    public CommandResult execute(Model model) throws JobCommandException {
        requireNonNull(model);
        model.sortJobApplication(this.field, this.order);
        return new CommandResult(String.format(MESSAGE_SORT_APPLICATION_SUCCESS, field.name().toLowerCase(),
            order.name().toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortCommand)) {
            return false;
        }
        SortCommand o = (SortCommand) other;
        return field == o.field && order == o.order;
    }

    @Override
    public String toString() {
        return SortCommand.class.getCanonicalName()
            + "{field=" + field
            + ", order=" + order
            + "}";
    }
}

