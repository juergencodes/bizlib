package de.mathit.streams;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Test {@link MyCollectors#groupAndAggregate} with {@link Person}:
 */
public class MyCollectorsGroupAndAggregate2Test {

  @Test
  public void sumAge1() {
    final List<Person> persons = createPersons();

    // Group by name and sum age
    final BiPredicate<Person, Person> predicate = (x, y) -> x.getName().equals(y.getName());
    final Function<Person, Integer> grouper = p -> p.getAge();
    final BiFunction<Person, Integer, Integer> combiner = (p, a) -> (p.getAge() + a);
    final BiFunction<Person, Integer, Person> aggregator = (p, a) -> new Person(p.getName(), a);

    final List<Person> list = persons.stream()
        .collect(MyCollectors.groupAndAggregate(predicate, grouper, combiner, aggregator));
    assertEquals("Wrong number of persons.", 3, list.size());
    assertNameAndAge(list.get(0), "Max", 9);
    assertNameAndAge(list.get(1), "Peter", 3);
    assertNameAndAge(list.get(2), "Marlies", 5);
  }

  @Test
  public void sumAge2() {
    final List<Person> persons = createPersons();

    // Group by name and sum age
    final BiPredicate<Person, Person> predicate = (x, y) -> x.getName().equals(y.getName());
    final Function<Person, Person> grouper = p -> new Person(p.getName(), p.getAge());
    final BiFunction<Person, Person, Person> combiner = (p, q) -> new Person(p.getName(),
        p.getAge() + q.getAge());

    final List<Person> list = persons.stream()
        .collect(MyCollectors.groupAndAggregate(predicate, grouper, combiner));
    assertEquals("Wrong number of persons.", 3, list.size());
    assertNameAndAge(list.get(0), "Max", 9);
    assertNameAndAge(list.get(1), "Peter", 3);
    assertNameAndAge(list.get(2), "Marlies", 5);
  }

  private void assertNameAndAge(final Person person, final String expectedName,
      final int expectedAge) {
    assertEquals("Wrong name.", expectedName, person.getName());
    assertEquals("Wrong age.", expectedAge, person.getAge());
  }

  private List<Person> createPersons() {
    final List<Person> persons = new LinkedList<>();
    persons.add(new Person("Max", 5));
    persons.add(new Person("Peter", 3));
    persons.add(new Person("Marlies", 5));
    persons.add(new Person("Max", 4));
    return persons;
  }


}