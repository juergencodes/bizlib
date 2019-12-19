package de.mathit.decisiontable;

import static de.mathit.decisiontable.TestSupport.assertResult;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class CustomizableDecisionTableTest {

  @Test
  public void person() {
    final PersonDecisionTable decisionTable = new PersonDecisionTable();
    decisionTable.addRow("Peter", null, true);
    decisionTable.addRow("Max", "Meier", true);
    decisionTable.addRow(null, null, false);

    assertResult(decisionTable, new Person("Peter", "Meier"), true);
    assertResult(decisionTable, new Person("Max", "Meier"), true);
    assertResult(decisionTable, new Person("Max", "Huber"), false);
  }

  private static class Person {

    private final String firstName;
    private final String lastName;

    public Person(final String firstName, final String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

  }

  public class PersonDecisionTable extends CustomizableDecisionTable<Person, Boolean> {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String RESULT = "result";

    public PersonDecisionTable() {
      super((m, f) -> (Boolean) m.get(RESULT));

      addCondition(
          (r, f) -> r.get(FIRST_NAME) == null || r.get(FIRST_NAME).equals(f.getFirstName()));
      addCondition((r, f) -> r.get(LAST_NAME) == null || r.get(LAST_NAME).equals(f.getLastName()));
    }

    public void addRow(final String firstName, final String lastName, final boolean result) {
      addRow().parameter(FIRST_NAME, firstName).parameter(LAST_NAME, lastName)
          .parameter(RESULT, result).finish();
    }

  }

  @Test
  public void interval() {
    final IntervalDecisionTable decisionTable = new IntervalDecisionTable();
    decisionTable.addRow(40, 60, "boundaries1");
    decisionTable.addRow(1, 100, "boundaries2");
    decisionTable.addRow(null, null, "catchall");

    assertResult(decisionTable, 50, "boundaries1");
    assertResult(decisionTable, 61, "boundaries2");
    assertResult(decisionTable, 0, "catchall");
  }

  public class IntervalDecisionTable extends CustomizableDecisionTable<Integer, String> {

    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String RESULT = "result";

    public IntervalDecisionTable() {
      super((m, f) -> (String) m.get(RESULT));

      addCondition((r, f) -> r.get(MIN) == null || f >= (Integer) r.get(MIN));
      addCondition((r, f) -> r.get(MAX) == null || f <= (Integer) r.get(MAX));
    }

    public void addRow(final Integer min, final Integer max, final String result) {
      final Map<String, Object> row = new HashMap<>();
      row.put(MIN, min);
      row.put(MAX, max);
      row.put(RESULT, result);
      addRow(row);
    }
  }

}