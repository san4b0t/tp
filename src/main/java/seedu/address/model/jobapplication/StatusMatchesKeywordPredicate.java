package seedu.address.model.jobapplication;

import java.util.function.Predicate;

import seedu.address.model.jobapplication.JobApplication.Status;

public class StatusMatchesKeywordPredicate implements Predicate<JobApplication> {
    private final Status keyword;

    public StatusMatchesKeywordPredicate(Status keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getStatus().equals(keyword);
    }

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
