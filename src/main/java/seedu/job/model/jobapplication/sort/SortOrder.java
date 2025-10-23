package seedu.job.model.jobapplication.sort;

/**
 * Sort order for job application sorting.
 */
public enum SortOrder { ASC, DESC;

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
        case "asc": return ASC;
        case "desc": return DESC;
        default: throw new IllegalArgumentException("Unknown sort order: " + s);
        }
    }
}
