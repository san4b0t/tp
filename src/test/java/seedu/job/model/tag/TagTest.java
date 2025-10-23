package seedu.job.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.job.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_singleWordTags_success() {

        String validTagName = "2025";
        Tag tag = new Tag(validTagName);
        assertEquals(tag.toString(), "[" + validTagName + "]");

    }

    @Test
    public void constructor_invalidTags_throwsIllegalArgumentException() {

        // Disallow 2 words
        String invalidTagName = "6 months";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));

        // Disallow >2 special characters
        String invalidTagName2 = "python_v_1.3";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName2));

    }

    @Test
    public void equals_caseSensitivity() {
        Tag lowerCase = new Tag("java");
        Tag upperCase = new Tag("JAVA");
        Tag mixedCase = new Tag("Java");

        assertNotEquals(lowerCase, upperCase); // or assertEquals if case-insensitive
    }

    @Test
    public void isValidTagName_validInputs_returnsTrue() {

        // Allow a mix of numbers, some special symbols and alphabets
        assertTrue(Tag.isValidTagName("6-Month"));

        // Allow a maximum of 2 characters
        assertTrue(Tag.isValidTagName("Python_v3.12"));

        // Allow a mix of numbers and alphabets
        assertTrue(Tag.isValidTagName("Java17"));

        // Allow a mix of alphabets and symbols
        assertTrue(Tag.isValidTagName("C#"));
        assertTrue(Tag.isValidTagName("C++"));

        // Allow a purely alphabetical word
        assertTrue(Tag.isValidTagName("FinTech"));

    }

    @Test
    public void isValidTagName_invalidInputs_returnsFalse() {
        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("two words"));
        assertFalse(Tag.isValidTagName("python_v_3.12"));
        assertFalse(Tag.isValidTagName(" leadingSpace"));
        assertFalse(Tag.isValidTagName("trailingSpace "));
        assertFalse(Tag.isValidTagName("LongLongLongLongLongLongLongLong"));
    }

    @Test
    public void constructor_emptyTag_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

}
