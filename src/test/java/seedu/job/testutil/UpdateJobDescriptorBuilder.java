package seedu.job.testutil;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.job.logic.jobcommands.UpdateJobCommand.UpdateJobDescriptor;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.tag.Tag;

/**
 * A utility class to help with building UpdateJobDescriptor objects.
 */
public class UpdateJobDescriptorBuilder {

    private UpdateJobDescriptor descriptor;

    public UpdateJobDescriptorBuilder() {
        descriptor = new UpdateJobDescriptor();
    }

    public UpdateJobDescriptorBuilder(UpdateJobDescriptor descriptor) {
        this.descriptor = new UpdateJobDescriptor(descriptor);
    }

    /**
     * Returns an {@code UpdateJobDescriptor} with fields containing {@code jobApplication}'s details
     */
    public UpdateJobDescriptorBuilder(JobApplication jobApplication) {
        descriptor = new UpdateJobDescriptor();
        descriptor.setCompanyName(jobApplication.getCompanyName());
        descriptor.setRole(jobApplication.getRole());
        descriptor.setDeadline(jobApplication.getDeadline());
        descriptor.setStatus(jobApplication.getStatus());
        descriptor.setTags(jobApplication.getTags());
    }

    /**
     * Sets the company name of the {@code UpdateJobDescriptor} that we are building.
     */
    public UpdateJobDescriptorBuilder withCompanyName(String companyName) {
        descriptor.setCompanyName(companyName);
        return this;
    }

    /**
     * Sets the role of the {@code UpdateJobDescriptor} that we are building.
     */
    public UpdateJobDescriptorBuilder withRole(String role) {
        descriptor.setRole(role);
        return this;
    }

    /**
     * Sets the deadline of the {@code UpdateJobDescriptor} that we are building.
     */
    public UpdateJobDescriptorBuilder withDeadline(LocalDateTime deadline) {
        descriptor.setDeadline(deadline);
        return this;
    }

    /**
     * Sets the status of the {@code UpdateJobDescriptor} that we are building.
     */
    public UpdateJobDescriptorBuilder withStatus(JobApplication.Status status) {
        descriptor.setStatus(status);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code UpdateJobDescriptor}
     * that we are building.
     */
    public UpdateJobDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public UpdateJobDescriptor build() {
        return descriptor;
    }
}
