package seedu.job.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.job.commons.core.GuiSettings;
import seedu.job.logic.jobcommands.CommandResult;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.logic.parser.exceptions.ParseException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.ReadOnlyJobBook;

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
     * @see seedu.job.model.Model#getAddressBook()
     */
    ReadOnlyJobBook getJobBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<JobApplication> getFilteredApplicationList();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<JobApplication> getFilteredApplicationsList();

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
