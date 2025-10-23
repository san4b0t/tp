package seedu.job.model.jobapplication;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.job.commons.util.StringUtil;
import seedu.job.commons.util.ToStringBuilder;

/**
 * Tests that a {@code JobApplication}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<JobApplication> {
    private final List<String> keywords;


    /**
     * Constructs a {@code NameContainsKeywordsPredicate} with the specified keywords.
     * Empty strings in the keyword list are filtered out.
     *
     * @param keywords The list of keywords to match against job application names.
     *                 Empty strings are automatically removed from this list.
     */
    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords.stream()
                                .filter(w -> !w.isEmpty())
                                .collect(Collectors.toList());
    }

    @Override
    public boolean test(JobApplication jobApplication) {
        String company = jobApplication.getCompanyName();
        return keywords.stream().anyMatch(
            keyword -> StringUtil.containsWordIgnoreCase(company, keyword)
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
