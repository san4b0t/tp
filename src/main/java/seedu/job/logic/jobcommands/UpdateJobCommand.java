package seedu.job.logic.jobcommands;

import static java.util.Objects.requireNonNull;
import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.job.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.job.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.job.model.jobapplication.Model.PREDICATE_SHOW_ALL_APPLICATIONS;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.job.commons.core.index.Index;
import seedu.job.commons.util.CollectionUtil;
import seedu.job.commons.util.ToStringBuilder;
import seedu.job.logic.JobMessages;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.tag.Tag;

/**
 * Updates the details of an existing job application in the job book.
 */
public class UpdateJobCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the details of the job application "
            + "identified by the index number used in the displayed job application list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "COMPANY_NAME] "
            + "[" + PREFIX_ROLE + "ROLE] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_DEADLINE + "DEADLINE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_STATUS + "INPROGRESS "
            + PREFIX_DEADLINE + "2025-12-31T23:59";

    public static final String MESSAGE_UPDATE_JOB_SUCCESS = "Updated Job Application: %1$s";
    public static final String MESSAGE_NOT_UPDATED = "At least one field to update must be provided.";
    public static final String MESSAGE_DUPLICATE_APPLICATION =
            "This job application already exists in the job book.";

    private final Index index;
    private final UpdateJobDescriptor updateJobDescriptor;

    /**
     * Creates an UpdateJobCommand to update the job application at the specified {@code index}
     * with the details in {@code updateJobDescriptor}.
     *
     * @param index of the job application in the filtered job application list to update
     * @param updateJobDescriptor details to update the job application with
     */
    public UpdateJobCommand(Index index, UpdateJobDescriptor updateJobDescriptor) {
        requireNonNull(index);
        requireNonNull(updateJobDescriptor);

        this.index = index;
        this.updateJobDescriptor = new UpdateJobDescriptor(updateJobDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws JobCommandException {
        requireNonNull(model);
        List<JobApplication> lastShownList = model.getFilteredApplicationList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new JobCommandException(JobMessages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX);
        }

        JobApplication jobToUpdate = lastShownList.get(index.getZeroBased());
        JobApplication updatedJob = createUpdatedJob(jobToUpdate, updateJobDescriptor);

        if (!jobToUpdate.isSameJobApplication(updatedJob) && model.hasApplication(updatedJob)) {
            throw new JobCommandException(MESSAGE_DUPLICATE_APPLICATION);
        }

        model.setJobApplication(jobToUpdate, updatedJob);
        model.updateFilteredJobApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
        return new CommandResult(String.format(MESSAGE_UPDATE_JOB_SUCCESS, JobMessages.format(updatedJob)));
    }

    /**
     * Creates and returns a {@code JobApplication} with the details of {@code jobToUpdate}
     * updated with {@code updateJobDescriptor}.
     */
    private static JobApplication createUpdatedJob(JobApplication jobToUpdate,
                                                    UpdateJobDescriptor updateJobDescriptor) {
        assert jobToUpdate != null;

        String updatedCompanyName = updateJobDescriptor.getCompanyName().orElse(jobToUpdate.getCompanyName());
        String updatedRole = updateJobDescriptor.getRole().orElse(jobToUpdate.getRole());
        LocalDateTime updatedDeadline = updateJobDescriptor.getDeadline().orElse(jobToUpdate.getDeadline());
        JobApplication.Status updatedStatus = updateJobDescriptor.getStatus().orElse(jobToUpdate.getStatus());
        Set<Tag> updatedTags = updateJobDescriptor.getTags().orElse(jobToUpdate.getTags());

        return new JobApplication(updatedCompanyName, updatedRole, updatedDeadline, updatedStatus, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateJobCommand)) {
            return false;
        }

        UpdateJobCommand otherUpdateCommand = (UpdateJobCommand) other;
        return index.equals(otherUpdateCommand.index)
                && updateJobDescriptor.equals(otherUpdateCommand.updateJobDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("updateJobDescriptor", updateJobDescriptor)
                .toString();
    }

    /**
     * Stores the details to update the job application with. Each non-empty field value will replace the
     * corresponding field value of the job application.
     */
    public static class UpdateJobDescriptor {
        private String companyName;
        private String role;
        private LocalDateTime deadline;
        private JobApplication.Status status;
        private Set<Tag> tags;

        public UpdateJobDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public UpdateJobDescriptor(UpdateJobDescriptor toCopy) {
            setCompanyName(toCopy.companyName);
            setRole(toCopy.role);
            setDeadline(toCopy.deadline);
            setStatus(toCopy.status);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is updated.
         */
        public boolean isAnyFieldUpdated() {
            return CollectionUtil.isAnyNonNull(companyName, role, deadline, status, tags);
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public Optional<String> getCompanyName() {
            return Optional.ofNullable(companyName);
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Optional<String> getRole() {
            return Optional.ofNullable(role);
        }

        public void setDeadline(LocalDateTime deadline) {
            this.deadline = deadline;
        }

        public Optional<LocalDateTime> getDeadline() {
            return Optional.ofNullable(deadline);
        }

        public void setStatus(JobApplication.Status status) {
            this.status = status;
        }

        public Optional<JobApplication.Status> getStatus() {
            return Optional.ofNullable(status);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(new HashSet<>(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof UpdateJobDescriptor)) {
                return false;
            }

            UpdateJobDescriptor otherDescriptor = (UpdateJobDescriptor) other;
            return Objects.equals(companyName, otherDescriptor.companyName)
                    && Objects.equals(role, otherDescriptor.role)
                    && Objects.equals(deadline, otherDescriptor.deadline)
                    && Objects.equals(status, otherDescriptor.status)
                    && Objects.equals(tags, otherDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("companyName", companyName)
                    .add("role", role)
                    .add("deadline", deadline)
                    .add("status", status)
                    .add("tags", tags)
                    .toString();
        }
    }
}
