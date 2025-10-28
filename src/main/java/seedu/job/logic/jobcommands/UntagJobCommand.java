package seedu.job.logic.jobcommands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.job.commons.core.index.Index;
import seedu.job.commons.util.ToStringBuilder;
import seedu.job.logic.JobMessages;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.tag.Tag;

/**
 * Removes tags from a job application.
 */
public class UntagJobCommand extends Command {

    public static final String MESSAGE_TAG_REMOVAL_SUCCESS = "Tags have been removed!";
    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_MISSING_TAG = "Cannot remove tags: One or more of the specified tags "
            + "do not exist on this application. "
            + "Please check the existing tags and try again.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes tags from a job application.\n"
            + "Parameters: index_name t/tag t/tag t/tag\n"
            + "Example: " + COMMAND_WORD + " 1 t/Intern t/Summer t/on-site";

    protected final Index targetIndex;
    private final Set<Tag> tagsToRemove;

    /**
     * Creates an UntagJobCommand to add the specified {@code JobApplication}
     */
    public UntagJobCommand(Index targetIndex, Set<Tag> tags) {
        requireNonNull(targetIndex);
        requireNonNull(tags);
        this.targetIndex = targetIndex;
        this.tagsToRemove = tags;
    }

    @Override
    public CommandResult execute(Model model) throws JobCommandException {
        requireNonNull(model);

        List<JobApplication> lastShownList = model.getFilteredApplicationList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new JobCommandException(JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX);
        }

        JobApplication jobToUntag = lastShownList.get(targetIndex.getZeroBased());

        if (!jobToUntag.canRemoveProvidedTags(tagsToRemove)) {
            throw new JobCommandException(MESSAGE_MISSING_TAG);
        }

        JobApplication untaggedJob = createUntaggedJob(jobToUntag, tagsToRemove);

        model.setRecentlyModifiedApplication(jobToUntag);
        model.setJobApplication(jobToUntag, untaggedJob);

        return new CommandResult(String.format(MESSAGE_TAG_REMOVAL_SUCCESS, JobMessages.format(untaggedJob)));
    }

    /**
     * Creates and returns a {@code JobApplication} with the details of {@code jobToUntag}
     * with specified {@code tagsToRemove} removed.
     */
    private static JobApplication createUntaggedJob(JobApplication jobToUntag, Set<Tag> tagsToRemove) {
        assert jobToUntag != null;

        Set<Tag> updatedTags = new HashSet<>(jobToUntag.getTags());
        updatedTags.removeAll(tagsToRemove);

        return new JobApplication(
            jobToUntag.getCompanyName(),
            jobToUntag.getRole(),
            jobToUntag.getDeadline(),
            jobToUntag.getStatus(),
            updatedTags
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagJobCommand)) {
            return false;
        }

        UntagJobCommand otherTagCommand = (UntagJobCommand) other;
        boolean areTagsEqual = this.tagsToRemove.equals(otherTagCommand.tagsToRemove);
        boolean isIndexSame = targetIndex.equals(otherTagCommand.targetIndex);

        return areTagsEqual && isIndexSame;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex.getZeroBased())
                .add("tags", tagsToRemove.toString())
                .toString();
    }
}
