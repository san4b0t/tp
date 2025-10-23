package seedu.job.model.jobapplication;

import java.nio.file.Path;

import seedu.job.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getJobBookFilePath();

}
