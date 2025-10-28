package seedu.job.model.jobapplication.sort;

/**
 * Sort order for job application sorting.
 */
public enum SortOrder { ASCENDING, DESCENDING;

    /**
     * Parses a string into a {@link SortOrder}.
     *
     * @param s input such as "asc" or "desc" (case-insensitive).
     * @return corresponding SortOrder.
     * @throws IllegalArgumentException if the value is not recognised.
     */
    public static SortOrder from(String s) {
        String k = s.trim().toLowerCase();
        switch (k) {
        case "asc", "ascending" -> { return ASCENDING; }
        case "desc", "descending" -> { return DESCENDING; }
        default -> throw new IllegalArgumentException("Unknown sort order: " + s);
        }
    }
}
