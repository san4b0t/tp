package seedu.job.testutil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.JobApplication.Status;
import seedu.job.model.tag.Tag;
import seedu.job.model.util.SampleDataUtil;

/**
 * A utility class to help with building JobApplication objects.
 */
public class JobApplicationBuilder {

    public static final String DEFAULT_COMPANY_NAME = "Google Inc";
    public static final String DEFAULT_ROLE = "Software Engineer";
    public static final LocalDateTime DEFAULT_DEADLINE = LocalDateTime.of(2025, 12, 31, 23, 59);
    public static final Status DEFAULT_STATUS = Status.APPLIED;

    private String companyName;
    private String role;
    private LocalDateTime deadline;
    private Status status;
    private Set<Tag> tags;

    /**
     * Creates a {@code JobApplicationBuilder} with the default details.
     */
    public JobApplicationBuilder() {
        companyName = DEFAULT_COMPANY_NAME;
        role = DEFAULT_ROLE;
        deadline = DEFAULT_DEADLINE;
        status = DEFAULT_STATUS;
        tags = new HashSet<>();
    }

    /**
     * Initializes the JobApplicationBuilder with the data of {@code jobApplicationToCopy}.
     */
    public JobApplicationBuilder(JobApplication jobApplicationToCopy) {
        companyName = jobApplicationToCopy.getCompanyName();
        role = jobApplicationToCopy.getRole();
        deadline = jobApplicationToCopy.getDeadline();
        status = jobApplicationToCopy.getStatus();
        tags = new HashSet<>(jobApplicationToCopy.getTags());
    }

    /**
     * Sets the {@code companyName} of the {@code JobApplication} that we are building.
     */
    public JobApplicationBuilder withCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    /**
     * Sets the {@code role} of the {@code JobApplication} that we are building.
     */
    public JobApplicationBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * Sets the {@code deadline} of the {@code JobApplication} that we are building.
     */
    public JobApplicationBuilder withDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    /**
     * Sets the {@code status} of the {@code JobApplication} that we are building.
     */
    public JobApplicationBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code JobApplication}
     * that we are building.
     */
    public JobApplicationBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Builds and returns the {@code JobApplication}.
     */
    public JobApplication build() {
        return new JobApplication(companyName, role, deadline, status, tags);
    }
}
