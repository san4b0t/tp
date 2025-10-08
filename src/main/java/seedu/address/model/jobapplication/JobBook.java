package seedu.address.model.jobapplication;
import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class JobBook implements ReadOnlyJobBook {

    private final UniqueApplicationList applications;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        applications = new UniqueApplicationList();
    }

    public JobBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public JobBook(ReadOnlyJobBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setApplications(List<JobApplication> applications) {
        this.applications.setJobApplications(applications);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyJobBook newData) {
        requireNonNull(newData);

        setApplications(newData.getApplicationList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasJobApplication(JobApplication application) {
        requireNonNull(application);
        return applications.contains(application);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addApplication(JobApplication p) {
        applications.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setApplication(JobApplication target, JobApplication editedPerson) {
        requireNonNull(editedPerson);

        applications.setJobApplication(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeApplication(JobApplication key) {
        applications.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", applications)
                .toString();
    }

    @Override
    public ObservableList<JobApplication> getApplicationList() {
        return applications.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobBook)) {
            return false;
        }

        JobBook otherJobBook = (JobBook) other;
        return applications.equals(otherJobBook.applications);
    }

    @Override
    public int hashCode() {
        return applications.hashCode();
    }
}
