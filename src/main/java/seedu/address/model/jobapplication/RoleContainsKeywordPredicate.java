package seedu.address.model.jobapplication;

import java.util.function.Predicate;

public class RoleContainsKeywordPredicate implements Predicate<JobApplication> {
    private final String keyword;

    public RoleContainsKeywordPredicate(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getRole().toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RoleContainsKeywordPredicate)) {
            return false;
        }
        RoleContainsKeywordPredicate otherPredicate = (RoleContainsKeywordPredicate) other;

        return keyword.equals(otherPredicate.keyword);
    }
}
