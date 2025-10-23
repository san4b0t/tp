package seedu.job.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import seedu.job.commons.exceptions.DataLoadingException;
import seedu.job.model.jobapplication.JobApplication;

/**
 * Represents a storage for {@link JobApplication}.
 */
public interface JobApplicationStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getDataFilePath();

    /**
     * Returns a list of job applications
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    List<JobApplication> readDataFile() throws DataLoadingException;

    /**
     * @see #getDataFilePath()
     */
    List<JobApplication> readDataFile(Path filePath) throws DataLoadingException;

    /**
     * Saves the given list of job applications to the storage.
     * @param jobApplications can be empty
     * @throws IOException if there was any problem writing to the file.
     */
    void saveJobApplicationData(List<JobApplication> jobApplications) throws IOException;

    /**
     * @see #saveJobApplicationData(List)
     */
    void saveJobApplicationData(List<JobApplication> jobApplications, Path filePath) throws IOException;

}
