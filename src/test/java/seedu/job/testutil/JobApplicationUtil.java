package seedu.job.testutil;

import static seedu.job.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.job.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.job.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.job.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.format.DateTimeFormatter;
// import java.util.Set;

import seedu.job.logic.commands.AddCommand;
// import seedu.job.logic.commands.EditCommand.EditJobApplicationDescriptor;
import seedu.job.model.jobapplication.JobApplication;
// import seedu.job.model.tag.Tag;

/**
 * A utility class for JobApplication.
 */
public class JobApplicationUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Returns an add command string for adding the {@code jobApplication}.
     */
    public static String getAddCommand(JobApplication jobApplication) {
        return AddCommand.COMMAND_WORD + " " + getJobApplicationDetails(jobApplication);
    }

    /**
     * Returns the part of command string for the given {@code jobApplication}'s details.
     */
    public static String getJobApplicationDetails(JobApplication jobApplication) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + jobApplication.getCompanyName() + " ");
        sb.append(PREFIX_ROLE + jobApplication.getRole() + " ");
        sb.append(PREFIX_DEADLINE + jobApplication.getDeadline().format(FORMATTER) + " ");
        sb.append(PREFIX_STATUS + jobApplication.getStatus().toString() + " ");
        jobApplication.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    //    /**
    //     * Returns the part of command string for the given {@code EditJobApplicationDescriptor}'s details.
    //     */
    //
    //    public static String getEditJobApplicationDescriptorDetails(EditJobApplicationDescriptor descriptor) {
    //        StringBuilder sb = new StringBuilder();
    //        descriptor.getCompanyName().ifPresent(companyName ->
    //            sb.append(PREFIX_NAME).append(companyName).append(" "));
    //        descriptor.getRole().ifPresent(role ->
    //            sb.append(PREFIX_ROLE).append(role).append(" "));
    //        descriptor.getDeadline().ifPresent(deadline ->
    //            sb.append(PREFIX_DEADLINE).append(deadline.format(FORMATTER)).append(" "));
    //        descriptor.getStatus().ifPresent(status ->
    //            sb.append(PREFIX_STATUS).append(status.toString()).append(" "));
    //        if (descriptor.getTags().isPresent()) {
    //            Set<Tag> tags = descriptor.getTags().get();
    //            if (tags.isEmpty()) {
    //                sb.append(PREFIX_TAG);
    //            } else {
    //                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
    //            }
    //        }
    //        return sb.toString();
    //     }
}
