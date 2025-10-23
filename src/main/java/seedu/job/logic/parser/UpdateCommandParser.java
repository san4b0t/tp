package seedu.job.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.job.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.job.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.job.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.job.commons.core.index.Index;
import seedu.job.logic.jobcommands.UpdateJobCommand;
import seedu.job.logic.jobcommands.UpdateJobCommand.UpdateJobDescriptor;
import seedu.job.logic.parser.exceptions.ParseException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.tag.Tag;

/**
 * Parses input arguments and creates a new UpdateJobCommand object
 */
public class UpdateCommandParser implements JobParser<UpdateJobCommand> {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Parses the given {@code String} of arguments in the context of the UpdateJobCommand
     * and returns an UpdateJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UpdateJobCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ROLE, PREFIX_STATUS, PREFIX_DEADLINE, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UpdateJobCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ROLE, PREFIX_STATUS, PREFIX_DEADLINE);

        UpdateJobDescriptor updateJobDescriptor = new UpdateJobDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            updateJobDescriptor.setCompanyName(argMultimap.getValue(PREFIX_NAME).get());
        }
        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            updateJobDescriptor.setRole(argMultimap.getValue(PREFIX_ROLE).get());
        }
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            try {
                String statusStr = argMultimap.getValue(PREFIX_STATUS).get();
                JobApplication.Status status = JobApplication.Status.valueOf(statusStr.toUpperCase());
                updateJobDescriptor.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid status. Valid values are: APPLIED, INPROGRESS, REJECTED", e);
            }
        }
        if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
            try {
                String deadlineStr = argMultimap.getValue(PREFIX_DEADLINE).get();
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DATETIME_FORMATTER);
                ParserUtil.validateDeadlineNotInPast(deadline);
                updateJobDescriptor.setDeadline(deadline);
            } catch (DateTimeParseException e) {
                throw new ParseException("Invalid deadline format. Expected format: yyyy-MM-ddTHH:mm", e);
            }
        }

        parseTagsForUpdate(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(updateJobDescriptor::setTags);

        if (!updateJobDescriptor.isAnyFieldUpdated()) {
            throw new ParseException(UpdateJobCommand.MESSAGE_NOT_UPDATED);
        }

        return new UpdateJobCommand(index, updateJobDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForUpdate(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        Set<Tag> parsedTags = ParserUtil.parseTags(tagSet);

        if (parsedTags.size() > JobApplication.MAX_TAGS) {
            throw new ParseException("Maximum number of tags per application is: " + JobApplication.MAX_TAGS);
        }

        return Optional.of(parsedTags);
    }
}
