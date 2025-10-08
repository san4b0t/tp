package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.jobapplication.JobApplication;

/**
 * Represents a list of job applications that can be serialized to JSON format.
 */
@JsonRootName(value = "jobApplications")
public class JsonSerializableJobApplicationList {

    public static final String MESSAGE_DUPLICATE_APPLICATION = "Duplicate applications have been detected.";

    private final List<SerializableJobApplication> applications = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableJobApplicationList} with the given job applications.
     */
    @JsonCreator
    public JsonSerializableJobApplicationList(@JsonProperty("jobApplications")
        List<SerializableJobApplication> jobApplications) {
        this.applications.addAll(jobApplications);
}

    /**
     * Converts this job application list into the model's {@code List<JobApplication>} object.
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public List<JobApplication> toModelType() throws IllegalValueException {
        List<JobApplication> listOfApplications = new ArrayList<>();
        for (SerializableJobApplication serializableApplication : applications) {
            JobApplication application = serializableApplication.toModelType();
            if (listOfApplications.contains(application)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_APPLICATION);
            }
            listOfApplications.add(application);
        }
        return listOfApplications;
    }
}
