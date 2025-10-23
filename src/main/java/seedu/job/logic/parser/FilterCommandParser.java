package seedu.job.logic.parser;

import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.job.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.job.model.jobapplication.Model.PREDICATE_SHOW_ALL_APPLICATIONS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.job.logic.jobcommands.FilterCommand;
import seedu.job.logic.parser.exceptions.ParseException;
import seedu.job.model.jobapplication.DeadlinePredicate;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.RoleContainsKeywordPredicate;
import seedu.job.model.jobapplication.StatusMatchesKeywordPredicate;
import seedu.job.model.jobapplication.TagsContainKeywordPredicate;


/**
 * Represents an object that parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements JobParser<FilterCommand> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_ROLE, PREFIX_STATUS, PREFIX_DEADLINE);

        String preamble = argMultimap.getPreamble().trim();

        // Check if user wants to remove the existing filter
        if (preamble.equalsIgnoreCase("none")) {
            return new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        }

        if (preamble.isEmpty()) {
            // Identify the flag to filter by the correct field
            if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
                argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TAG);
                String keyword = argMultimap.getValue(PREFIX_TAG).get();

                TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate(keyword.toLowerCase());

                return new FilterCommand(predicate);

            } else if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
                argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ROLE);
                String keyword = argMultimap.getValue(PREFIX_ROLE).get();

                RoleContainsKeywordPredicate predicate = new RoleContainsKeywordPredicate(keyword.toLowerCase());

                return new FilterCommand(predicate);

            } else if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
                argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS);
                String statusStr = argMultimap.getValue(PREFIX_STATUS).get();

                try {
                    JobApplication.Status status = JobApplication.Status.valueOf(statusStr.toUpperCase());
                    StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(status);

                    return new FilterCommand(predicate);

                } catch (IllegalArgumentException e) {
                    throw new ParseException("Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED", e);
                }
            } else if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
                argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DEADLINE);
                String dateStr = argMultimap.getValue(PREFIX_DEADLINE).get();

                try {
                    LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
                    DeadlinePredicate predicate = new DeadlinePredicate(date);

                    return new FilterCommand(predicate);

                } catch (DateTimeParseException e) {
                    throw new ParseException("Invalid date format. Expected format: yyyy-MM-dd", e);
                }
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
