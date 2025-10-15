package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.logic.jobcommands.AddJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.jobapplication.JobApplication;

/**
 * Parses input arguments and creates a new AddJobCommand object
 */
public class AddCommandParser implements JobParser<AddJobCommand> {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Parses the given {@code String} of arguments in the context of the AddJobCommand
     * and returns an AddJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddJobCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }

        String[] argParts = trimmedArgs.split("\\s+");

        if (argParts.length < 4) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }

        try {
            String companyName = argParts[0];
            String role = argParts[1];
            LocalDateTime deadline = LocalDateTime.parse(argParts[2], DATETIME_FORMATTER);
            JobApplication.Status status = JobApplication.Status.valueOf(argParts[3].toUpperCase());

            JobApplication application = new JobApplication(companyName, role, deadline, status);
            return new AddJobCommand(application);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid deadline format. Expected format: yyyy-MM-ddTHH:mm", e);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED", e);
        }
    }
}
