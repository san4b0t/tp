package seedu.job.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.job.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in JobApplication.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    // Maximum tag length based on UI constraints and usability requirements
    public static final int MAX_TAG_LENGTH = 30;

    public static final String MESSAGE_CONSTRAINTS = "Tags should be a single word with at most 2 special characters"
            + "(-, ., @, #, _) and cannot exceed " + MAX_TAG_LENGTH + " characters";
    public static final String VALIDATION_REGEX =
            "^[a-zA-Z0-9]*[\\-.@#_+]?[a-zA-Z0-9]*[\\-.@#_+]?[a-zA-Z0-9]*$";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX) && !test.isEmpty() && test.length() < MAX_TAG_LENGTH;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
