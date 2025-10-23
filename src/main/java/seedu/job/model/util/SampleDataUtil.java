package seedu.job.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.job.model.tag.Tag;

/** Contains utility methods for populating {@code AddressBook} with sample data. */
public class SampleDataUtil {
    /** Returns a tag set containing the list of strings given. */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
