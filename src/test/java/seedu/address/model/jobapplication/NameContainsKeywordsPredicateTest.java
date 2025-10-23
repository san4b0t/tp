package seedu.address.model.jobapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobApplicationBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy =
                new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_companyNameContainsKeywords_returnsTrue() {
        // One keyword
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Google"));
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Google Inc").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Google", "Microsoft"));
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Google Inc").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Microsoft", "Meta"));
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Meta Platforms").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("gOOgle", "MiCrOsOfT"));
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Google Inc").build()));
    }

    @Test
    public void test_companyNameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new JobApplicationBuilder().withCompanyName("Google").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Microsoft"));
        assertFalse(predicate.test(new JobApplicationBuilder().withCompanyName("Google Inc").build()));

        // Keywords match role but not company name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Engineer", "Developer"));
        assertFalse(predicate.test(new JobApplicationBuilder().withCompanyName("Google")
                .withRole("Software Engineer").build()));
    }

    @Test
    public void test_emptyKeywordsFiltered() {
        // Empty strings should be filtered out
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Arrays.asList("Google", "", "Microsoft", ""));

        // Should match "Google" even though empty strings were in the list
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Google Inc").build()));

        // Should match "Microsoft" even though empty strings were in the list
        assertTrue(predicate.test(new JobApplicationBuilder().withCompanyName("Microsoft Corp").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
