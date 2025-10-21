package seedu.address.model.jobapplication;

import java.time.LocalDate;
import java.util.function.Predicate;

public class DeadlinePredicate implements Predicate<JobApplication> {
    private final LocalDate date;

    public DeadlinePredicate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getDeadline().toLocalDate().equals(date);
    }

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
