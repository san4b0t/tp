package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.jobapplication.JobApplication;

/**
 * Jackson-friendly version of {@link JobApplication}.
 */
public class SerializableJobApplication {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Job Application's %s field is missing!";

    private final String companyName;
    private final String role;
    private final String deadline;
    private final String status;

    /**
     * Constructs a {@code SerializableJobApplication} with the given job application details.
     */
    @JsonCreator
    public SerializableJobApplication(@JsonProperty("companyName") String companyName,
                                      @JsonProperty("role") String role,
                                      @JsonProperty("deadline") String deadline,
                                      @JsonProperty("status") String status) {
        this.companyName = companyName;
        this.role = role;
        this.deadline = deadline;
        this.status = status;
    }

    /**
     * Converts a given {@code JobApplication} into this class for Jackson use.
     */
    public SerializableJobApplication(JobApplication application) {
        companyName = application.getCompanyName();
        role = application.getRole();
        deadline = application.getDeadline().toString();
        status = application.getStatus().name();

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code JobApplication} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public JobApplication toModelType() throws IllegalValueException {

        if (companyName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Company Name"));
        }
        if (role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Role"));
        }
        if (deadline == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Deadline"));
        }
        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Status"));
        }

        return new JobApplication(companyName, role,
            LocalDateTime.parse(deadline), JobApplication.Status.valueOf(status));
    }

}
