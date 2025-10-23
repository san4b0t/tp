package seedu.address.model.jobapplication;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.jobapplication.exceptions.DuplicateJobApplicationException;
import seedu.address.model.jobapplication.exceptions.JobApplicationNotFoundException;
import seedu.address.model.jobapplication.sort.SortField;
import seedu.address.model.jobapplication.sort.SortOrder;


/**
 * A list of Applications that enforces uniqueness between its elements and does not allow nulls.
 * An Application is considered unique by comparing using {@code Application#isSameApplication(Application)}. As such,
 * adding and updating of Applications uses Application#isSameApplication(Application) for equality so as to ensure that
 * the Application being added or updated is unique in terms of identity in the UniqueApplicationList. However, the
 * removal of an Application uses Application#equals(Object) so as to ensure that the Application with exactly the same
 * fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see JobApplication#isSameJobApplication(JobApplication)
 */
public class UniqueApplicationList implements Iterable<JobApplication> {

    private final ObservableList<JobApplication> internalList = FXCollections.observableArrayList();
    private final ObservableList<JobApplication> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent Application as the given argument.
     */
    public boolean contains(JobApplication toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameJobApplication);
    }

    /**
     * Adds a Application to the list.
     * The Application must not already exist in the list.
     */
    public void add(JobApplication toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateJobApplicationException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the Application {@code target} in the list with {@code editedApplication}.
     * {@code target} must exist in the list.
     * The Application identity of {@code editedApplication} must not be the same as another existing Application in the
     * list.
     */
    public void setJobApplication(JobApplication target, JobApplication editedApplication) {
        requireAllNonNull(target, editedApplication);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new JobApplicationNotFoundException();
        }

        if (!target.isSameJobApplication(editedApplication) && contains(editedApplication)) {
            throw new DuplicateJobApplicationException();
        }

        internalList.set(index, editedApplication);
    }

    /**
     * Removes the equivalent Application from the list.
     * The Application must exist in the list.
     */
    public void remove(JobApplication toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new JobApplicationNotFoundException();
        }
    }

    /**
     * Sorts the entire application list based on application deadline.
     */
    public void sortApplication(SortField field, SortOrder order) {
        Comparator<JobApplication> cmp = comparatorFor(field);
        if (order == SortOrder.DESC) {
            cmp = cmp.reversed();
        }
        FXCollections.sort(internalList, cmp);
    }

    private static Comparator<JobApplication> comparatorFor(SortField field) {
        Comparator<JobApplication> byDeadline = Comparator.comparing(
            UniqueApplicationList::deadlineOrNull,
            Comparator.nullsLast(Comparator.naturalOrder())
        );
        Comparator<JobApplication> byCompany = Comparator.comparing(
            ja -> safe(ja.getCompanyName()), String.CASE_INSENSITIVE_ORDER
        );
        Comparator<JobApplication> byRole = Comparator.comparing(
            ja -> safe(ja.getRole()), String.CASE_INSENSITIVE_ORDER
        );

        return switch (field) {
        case DEADLINE -> byDeadline.thenComparing(byCompany).thenComparing(byRole);
        case COMPANY -> byCompany.thenComparing(byRole).thenComparing(byDeadline);
        case ROLE -> byRole.thenComparing(byCompany).thenComparing(byDeadline);
        };
    }

    private static LocalDateTime deadlineOrNull(JobApplication a) {
        // Adjust if your deadline type differs (e.g. String → LocalDate.parse).
        return a.getDeadline();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    public void setJobApplications(UniqueApplicationList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code Applications}.
     * {@code Applications} must not contain duplicate Applications.
     */
    public void setJobApplications(List<JobApplication> applications) {
        requireAllNonNull(applications);
        if (!applicationsAreUnique(applications)) {
            throw new DuplicateJobApplicationException();
        }

        internalList.setAll(applications);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<JobApplication> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<JobApplication> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueApplicationList)) {
            return false;
        }

        UniqueApplicationList otherUniqueApplicationList = (UniqueApplicationList) other;
        return internalList.equals(otherUniqueApplicationList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code Applications} contains only unique Applications.
     */
    private boolean applicationsAreUnique(List<JobApplication> applications) {
        for (int i = 0; i < applications.size() - 1; i++) {
            for (int j = i + 1; j < applications.size(); j++) {
                if (applications.get(i).isSameJobApplication(applications.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
