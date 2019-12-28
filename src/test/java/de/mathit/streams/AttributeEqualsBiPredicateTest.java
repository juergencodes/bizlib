package de.mathit.streams;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link AttributeEqualsBiPredicate}.
 */
public class AttributeEqualsBiPredicateTest {


  private Person max4 = new Person("Max", 4);
  private Person max5 = new Person("Max", 5);
  private Person peter4 = new Person("Peter", 4);
  private Person peter5 = new Person("Peter", 5);

  @Test
  public void test1() {
    final AttributeEqualsBiPredicate<Person> predicate = new AttributeEqualsBiPredicate<>();
    predicate.addAttribute(p -> p.getName());

    assertMatch(predicate, max4, max4);
    assertMatch(predicate, max4, max5);
    assertNoMatch(predicate, max4, peter4);
    assertNoMatch(predicate, max5, peter5);
    assertMatch(predicate, peter4, peter5);
  }

  @Test
  public void test2() {
    final AttributeEqualsBiPredicate<Person> predicate = new AttributeEqualsBiPredicate<>();
    predicate.addAttribute(p -> p.getAge());

    assertMatch(predicate, max4, max4);
    assertNoMatch(predicate, max4, max5);
    assertMatch(predicate, max4, peter4);
    assertMatch(predicate, max5, peter5);
    assertNoMatch(predicate, peter4, peter5);
  }

  @Test
  public void test3() {
    final AttributeEqualsBiPredicate<Person> predicate = new AttributeEqualsBiPredicate<>();
    predicate.addAttribute(p -> p.getAge());
    predicate.addAttribute(p -> p.getName());

    assertMatch(predicate, max4, max4);
    assertNoMatch(predicate, max4, max5);
    assertNoMatch(predicate, max4, peter4);
    assertNoMatch(predicate, max5, peter5);
    assertNoMatch(predicate, peter4, peter5);
  }


  private void assertMatch(final AttributeEqualsBiPredicate<Person> predicate, final Person person1,
      final Person person2) {
    assertTrue("Expected a match.", predicate.test(person1, person2));
    assertTrue("Expected a match.", predicate.test(person2, person1));
  }

  private void assertNoMatch(final AttributeEqualsBiPredicate<Person> predicate,
      final Person person1, final Person person2) {
    assertFalse("Expected no match.", predicate.test(person1, person2));
    assertFalse("Expected no match.", predicate.test(person2, person1));
  }

}