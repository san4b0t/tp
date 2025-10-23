package seedu.job.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.job.logic.jobcommands.SortCommand;
import seedu.job.logic.parser.exceptions.ParseException;
import seedu.job.model.jobapplication.sort.SortField;
import seedu.job.model.jobapplication.sort.SortOrder;

/**
 * Parses input arguments and creates a new {@link SortCommand} object.
 *
 * Accepted forms:
 * <pre>
 *   sort deadline
 *   sort company desc
 *   sort role asc
 * </pre>
 */
public class SortCommandParser implements JobParser<SortCommand> {

    @Override
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmed.split("\\s+");
        // field [order]
        try {
            SortField field = SortField.from(parts[0]);
            SortOrder order = (parts.length >= 2) ? SortOrder.from(parts[1]) : SortOrder.ASC;
            return new SortCommand(field, order);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(ex.getMessage());
        }
    }
}
