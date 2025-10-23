package seedu.job.model.jobapplication.sort;


/**
 * Sortable fields for job applications.
 */
public enum SortField {
    DEADLINE, COMPANY, ROLE;

    /**
     * Parses a string into a {@link SortField}.
     *
     * @param s input such as "deadline", "company", or "role" (case-insensitive).
     * @return corresponding SortField.
     * @throws IllegalArgumentException if the value is not recognised.
     */
    public static SortField from(String s) {
        String k = s.trim().toLowerCase();
        switch (k) {
        case "deadline": return DEADLINE;
        case "company": return COMPANY;
        case "role": return ROLE;
        default: throw new IllegalArgumentException("Unknown sort field: " + s);
        }
    }
}
