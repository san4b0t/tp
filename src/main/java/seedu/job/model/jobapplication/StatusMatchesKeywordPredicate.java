package seedu.job.model.jobapplication;

import java.util.function.Predicate;

import seedu.job.model.jobapplication.JobApplication.Status;

/**
 * A predicate that tests whether a JobApplication's status matches a specified status.
 * This predicate is used to filter job applications by their current status.
 */
public class StatusMatchesKeywordPredicate implements Predicate<JobApplication> {
    private final Status keyword;

    /**
     * Constructs a StatusMatchesKeywordPredicate with the specified status.
     *
     * @param keyword the Status to filter job applications by. Job applications with this
     *                status will match this predicate.
     */
    public StatusMatchesKeywordPredicate(Status keyword) {
        this.keyword = keyword;
    }

    /**
     * Tests whether the given JobApplication's status matches the predicate's status.
     *
     * @param jobApplication the JobApplication to test
     * @return {@code true} if the job application's status equals the predicate's status,
     *         {@code false} otherwise
     */
    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getStatus().equals(keyword);
    }

    /**
     * Compares this StatusMatchesKeywordPredicate with another object for equality.
     * Two StatusMatchesKeywordPredicates are considered equal if they have the same status.
     *
     * @param other the object to compare with
     * @return {@code true} if the given object is a StatusMatchesKeywordPredicate with the same status,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StatusMatchesKeywordPredicate)) {
            return false;
        }
        StatusMatchesKeywordPredicate otherPredicate = (StatusMatchesKeywordPredicate) other;

        return keyword.equals(otherPredicate.keyword);
    }
}
