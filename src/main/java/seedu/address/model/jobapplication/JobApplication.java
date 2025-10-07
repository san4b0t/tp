package seedu.address.model.jobapplication;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Job Application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class JobApplication {

    /**
     * Represents the status of a job application.
     */
    public enum Status {
        APPLIED,
        INPROGRESS,
        REJECTED
    }

    // Identity fields
    private final String companyName;
    private final String role;

    // Data fields
    private final LocalDateTime deadline;
    private final Status status;

    /**
     * Every field must be present and not null.
     */
    public JobApplication(String companyName, String role, LocalDateTime deadline, Status status) {
        requireAllNonNull(companyName, role, deadline, status);
        this.companyName = companyName;
        this.role = role;
        this.deadline = deadline;
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Returns true if both job applications have the same company name and role.
     * This defines a weaker notion of equality between two job applications.
     */
    public boolean isSameJobApplication(JobApplication otherJobApplication) {
        if (otherJobApplication == this) {
            return true;
        }

        return otherJobApplication != null
                && otherJobApplication.getCompanyName().equals(getCompanyName())
                && otherJobApplication.getRole().equals(getRole());
    }

    /**
     * Returns true if both job applications have the same identity and data fields.
     * This defines a stronger notion of equality between two job applications.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobApplication)) {
            return false;
        }

        JobApplication otherJobApplication = (JobApplication) other;
        return companyName.equals(otherJobApplication.companyName)
                && role.equals(otherJobApplication.role)
                && deadline.equals(otherJobApplication.deadline)
                && status.equals(otherJobApplication.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, role, deadline, status);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("companyName", companyName)
                .add("role", role)
                .add("deadline", deadline)
                .add("status", status)
                .toString();
    }
}