package seedu.address.model.jobapplication;

import java.util.function.Predicate;

public class TagsContainKeywordPredicate implements Predicate<JobApplication> {
    private final String keyword;

    public TagsContainKeywordPredicate(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public boolean test(JobApplication jobApplication) {
        return jobApplication.getTags().stream()
                .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword));
    }

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
