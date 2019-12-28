package de.mathit.decisiontable;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static de.mathit.decisiontable.TestSupport.assertResult;

public class GenericDecisionTableTest {

  @Test
  public void length() {
    final GenericDecisionTable<Integer, String, String> decisionTable = new GenericDecisionTable<>();

    decisionTable.addCondition((x, s) -> s.length() == x);

    decisionTable.addRow(1, "one");
    decisionTable.addRow(2, "two");
    decisionTable.addRow(3, "three");
    decisionTable.addRow(4, "four");
    decisionTable.addRow(5, "five");

    assertResult(decisionTable, "cd", "two");
    assertResult(decisionTable, "hello", "five");
  }

  @Test
  public void interval() {
    final GenericDecisionTable<Integer[], Integer, String> decisionTable = new GenericDecisionTable<>();

    decisionTable.addCondition((r, f) -> r[0] == null || f >= r[0])
        .addCondition((r, f) -> r[1] == null || f <= r[1]);

    decisionTable.addRow(new Integer[]{40, 60}, "boundaries1");
    decisionTable.addRow(new Integer[]{1, 100}, "boundaries2");
    decisionTable.addRow(new Integer[]{null, null}, "catchall");

    assertResult(decisionTable, 50, "boundaries1");
    assertResult(decisionTable, 61, "boundaries2");
    assertResult(decisionTable, 0, "catchall");
  }


  @Test
  public void intervalWithMaps() {
    final GenericDecisionTable<Map<String, Integer>, Integer, String> decisionTable = new GenericDecisionTable<>();

    decisionTable.addCondition((r, f) -> r.get("min") == null || f >= r.get("min"))
    .addCondition((r, f) -> r.get("max") == null || f <= r.get("max"));

    decisionTable.addRow(boundaries(40, 60), "boundaries1")
    .addRow(boundaries(1, 100), "boundaries2")
    .addRow(boundaries(null, null), "catchall");

    assertResult(decisionTable, 50, "boundaries1");
    assertResult(decisionTable, 61, "boundaries2");
    assertResult(decisionTable, 0, "catchall");
  }

  private Map<String, Integer> boundaries(final Integer min, final Integer max) {
    final Map<String, Integer> boundaries = new HashMap<>();
    boundaries.put("min", min);
    boundaries.put("max", max);
    return boundaries;
  }

}