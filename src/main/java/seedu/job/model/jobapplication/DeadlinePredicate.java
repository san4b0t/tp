package seedu.job.model.jobapplication;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * A predicate that tests whether a JobApplication's deadline matches a specified date.
 * This predicate is used to filter job applications by their deadline date.
 */
public class DeadlinePredicate implements Predicate<JobApplication> {
    private final LocalDate date;

    /**
     * Constructs a DeadlinePredicate with the specified date.
     *
     * @param date the date to filter job applications by. Job applications with deadlines
     *             on this date will match this predicate.
     */
    public DeadlinePredicate(LocalDate date) {
        this.date = date;
    }

    /**
     * Tests whether the given JobApplication's deadline matches the predicate's date.
     *
     * @param jobApplication the JobApplication to test
     * @return {@code true} if the job application's deadline falls on the predicate's date,
     *         {@code false} otherwise
     */
    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getDeadline().toLocalDate().equals(date);
    }

    /**
     * Compares this DeadlinePredicate with another object for equality.
     * Two DeadlinePredicates are considered equal if they have the same date.
     *
     * @param other the object to compare with
     * @return {@code true} if the given object is a DeadlinePredicate with the same date,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeadlinePredicate)) {
            return false;
        }
        DeadlinePredicate otherPredicate = (DeadlinePredicate) other;

        return date.equals(otherPredicate.date);
    }
}
