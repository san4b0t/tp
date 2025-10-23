package seedu.job.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.job.commons.exceptions.DataLoadingException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.ReadOnlyUserPrefs;
import seedu.job.model.jobapplication.UserPrefs;

/**
 * API of the Storage component for HustleHub.
 */
public interface DataStorage extends JobApplicationStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getDataFilePath();

    @Override
    List<JobApplication> readDataFile() throws DataLoadingException;

    @Override
    void saveJobApplicationData(List<JobApplication> jobApplicationList) throws IOException;

}

