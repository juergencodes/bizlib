package de.mathit.decisiontable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComparatorDecisionTableTest {

  @Test
  public void test() {
    final ComparatorDecisionTable<Integer, String> decisionTable = new ComparatorDecisionTable();

    decisionTable.addCondition((x, s) -> s.length() == x);

    decisionTable.addRow(1);
    decisionTable.addRow(2);
    decisionTable.addRow(3);

    assertLower(decisionTable, "a", "aa");
    assertGreater(decisionTable, "bbb", "b");
    assertLower(decisionTable, "cccc", "c"); // lower, because no row
    assertSame(decisionTable, "dd", "dd");

  }

  private <R, F> void assertLower(final ComparatorDecisionTable<R, F> decisionTable, final F fact1,
      final F fact2) {
    assertEquals("Expected to be lower.", -1, decisionTable.compare(fact1, fact2));
  }

  private <R, F> void assertGreater(final ComparatorDecisionTable<R, F> decisionTable,
      final F fact1, final F fact2) {
    assertEquals("Expected to be greater.", 1, decisionTable.compare(fact1, fact2));
  }

  private <R, F> void assertSame(final ComparatorDecisionTable<R, F> decisionTable, final F fact1,
      final F fact2) {
    assertEquals("Expected to be same.", 0, decisionTable.compare(fact1, fact2));
  }

}