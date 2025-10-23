package seedu.job.model.jobapplication;

import java.util.function.Predicate;

/**
 * A predicate that tests whether a JobApplication's role contains a specified keyword.
 * The search is case-insensitive and matches partial role names.
 */
public class RoleContainsKeywordPredicate implements Predicate<JobApplication> {
    private final String keyword;

    /**
     * Constructs a RoleContainsKeywordPredicate with the specified keyword.
     * The keyword is converted to lowercase for case-insensitive matching.
     *
     * @param keyword the keyword to search for in job application roles
     */
    public RoleContainsKeywordPredicate(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    /**
     * Tests whether the given JobApplication's role contains the keyword.
     * The search is case-insensitive and matches if the role contains the keyword as a substring.
     *
     * @param jobApplication the JobApplication to test
     * @return {@code true} if the job application's role contains the keyword (case-insensitive),
     *         {@code false} otherwise
     */
    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getRole().toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Compares this RoleContainsKeywordPredicate with another object for equality.
     * Two RoleContainsKeywordPredicates are considered equal if they have the same keyword.
     *
     * @param other the object to compare with
     * @return {@code true} if the given object is a RoleContainsKeywordPredicate with the same keyword,
     *         {@code false} otherwise
     */
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
