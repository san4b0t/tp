package seedu.address.model.jobapplication.exceptions;

/**
 * Signals that the operation will result in duplicate JobApplications (JobApplications are
 * considered duplicates if they have the same identity).
 */
public class DuplicateJobApplicationException extends RuntimeException {
    public DuplicateJobApplicationException() {
        super("Operation would result in duplicate persons");
    }
}
