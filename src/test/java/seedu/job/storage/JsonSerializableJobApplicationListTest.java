package seedu.job.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.job.commons.exceptions.IllegalValueException;
import seedu.job.commons.util.JsonUtil;
import seedu.job.model.jobapplication.JobApplication;

public class JsonSerializableJobApplicationListTest {

    private static final String FOLDER_NAME = "JsonSerializableJobApplicationListTest";
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", FOLDER_NAME);
    private static final Path VALID_APPLICATIONS = TEST_DATA_FOLDER.resolve("validApplications.json");
    private static final Path DUPLICATE_APPLICATIONS = TEST_DATA_FOLDER.resolve("duplicateApplications.json");

    @Test
    public void toModelType_validApplications_success() throws Exception {
        JsonSerializableJobApplicationList dataFromFile = JsonUtil.readJsonFile(VALID_APPLICATIONS,
                JsonSerializableJobApplicationList.class).get();

        List<JobApplication> list = dataFromFile.toModelType();
        int expectedListSize = 3;
        assertEquals(expectedListSize, list.size());
    }

    @Test
    public void toModelType_duplicateApplications_throwsIllegalValueException() throws Exception {
        JsonSerializableJobApplicationList dataFromFile = JsonUtil.readJsonFile(DUPLICATE_APPLICATIONS,
                JsonSerializableJobApplicationList.class).get();

        IllegalValueException exception = assertThrows(IllegalValueException.class, dataFromFile::toModelType);
        assertEquals(JsonSerializableJobApplicationList.MESSAGE_DUPLICATE_APPLICATION, exception.getMessage());
    }
}
