package seedu.job.model.jobapplication;

import java.util.function.Predicate;

/**
 * A predicate that tests whether a JobApplication contains tags with a keyword.
 * The search is case-insensitive and matches partial tag names.
 */
public class TagsContainKeywordPredicate implements Predicate<JobApplication> {
    private final String keyword;

    /**
     * Constructs a TagsContainKeywordPredicate with the specified keyword.
     * The keyword is converted to lowercase for case-insensitive matching.
     *
     * @param keyword the keyword to search for in job application tags
     */
    public TagsContainKeywordPredicate(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    /**
     * Tests whether the given JobApplication contains any tags that include the keyword.
     * The search is case-insensitive and matches if the tag name contains the keyword as a substring.
     *
     * @param jobApplication the JobApplication to test
     * @return {@code true} if at least one tag contains the keyword (case-insensitive),
     *         {@code false} otherwise
     */
    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getTags().stream()
                .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword));
    }

    /**
     * Compares this TagsContainKeywordPredicate with another object for equality.
     * Two TagsContainKeywordPredicates are considered equal if they have the same keyword.
     *
     * @param other the object to compare with
     * @return {@code true} if the given object is a TagsContainKeywordPredicate with the same keyword,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagsContainKeywordPredicate)) {
            return false;
        }
        TagsContainKeywordPredicate otherPredicate = (TagsContainKeywordPredicate) other;

        return keyword.equals(otherPredicate.keyword);
    }
}
