package seedu.address.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.jobcommands.CommandResult;
import seedu.address.logic.jobcommands.exceptions.JobCommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.jobapplication.JobApplication;
import seedu.address.model.jobapplication.ReadOnlyJobBook;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws JobCommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws JobCommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyJobBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<JobApplication> getFilteredApplicationList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getJobBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
