package seedu.job.logic.parser;

import static seedu.job.logic.JobMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
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
                ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_STATUS, PREFIX_DEADLINE);

        String preamble = argMultimap.getPreamble().trim();

        // Check if user wants to remove the existing filter
        if (preamble.equalsIgnoreCase("none")) {
            return new FilterCommand(PREDICATE_SHOW_ALL_APPLICATIONS);
        }

        if (preamble.isEmpty()) {
            // Count how many filter flags are present
            int flagCount = countPresentFlags(argMultimap);

            // Reject if multiple flags are provided
            if (flagCount > 1) {
                throw new ParseException("Filter command accepts only one filter flag at a time. "
                        + "Please use only one of: t/, s/, or d/");
            }

            // Identify the flag to filter by the correct field
            if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
                return createTagFilter(argMultimap);
            } else if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
                return createStatusFilter(argMultimap);
            } else if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
                return createDeadlineFilter(argMultimap);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    /**
     * Counts how many filter flags are present in the argument map.
     *
     * @param argMultimap The argument map to check
     * @return The number of filter flags present
     */
    private int countPresentFlags(ArgumentMultimap argMultimap) {
        int flagCount = 0;
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            flagCount++;
        }
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            flagCount++;
        }
        if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
            flagCount++;
        }
        return flagCount;
    }

    /**
     * Creates a FilterCommand that filters by tags.
     *
     * @param argMultimap The argument map containing the tag keyword
     * @return A FilterCommand with a TagsContainKeywordPredicate
     * @throws ParseException If duplicate prefixes are found
     */
    private FilterCommand createTagFilter(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TAG);
        String keyword = argMultimap.getValue(PREFIX_TAG).get();
        if (keyword.length() == 0) {
            throw new ParseException("Invalid empty tag for filter.");
        }
        TagsContainKeywordPredicate predicate = new TagsContainKeywordPredicate(keyword.toLowerCase());
        return new FilterCommand(predicate);
    }

    /**
     * Creates a FilterCommand that filters by status.
     *
     * @param argMultimap The argument map containing the status keyword
     * @return A FilterCommand with a StatusMatchesKeywordPredicate
     * @throws ParseException If duplicate prefixes are found or status is invalid
     */
    private FilterCommand createStatusFilter(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS);
        String statusStr = argMultimap.getValue(PREFIX_STATUS).get();

        try {
            JobApplication.Status status = JobApplication.Status.valueOf(statusStr.toUpperCase());
            StatusMatchesKeywordPredicate predicate = new StatusMatchesKeywordPredicate(status);
            return new FilterCommand(predicate);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED", e);
        }
    }

    /**
     * Creates a FilterCommand that filters by deadline.
     *
     * @param argMultimap The argument map containing the deadline date
     * @return A FilterCommand with a DeadlinePredicate
     * @throws ParseException If duplicate prefixes are found or date format is invalid
     */
    private FilterCommand createDeadlineFilter(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DEADLINE);
        String dateStr = argMultimap.getValue(PREFIX_DEADLINE).get();

        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ParseException("Invalid date format. Expected format: yyyy-MM-dd");
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            DeadlinePredicate predicate = new DeadlinePredicate(date);
            return new FilterCommand(predicate);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date. Please enter a valid date in yyyy-MM-dd format.", e);
        }
    }
}
