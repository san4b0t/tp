package seedu.job.logic.parser;

import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.job.commons.core.index.Index;
import seedu.job.logic.jobcommands.DeleteJobCommand;
import seedu.job.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements JobParser<DeleteJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteJobCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteJobCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE), pe);
        }
    }

}
