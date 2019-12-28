package de.mathit.decisiontable;

import org.junit.Test;

import static de.mathit.decisiontable.TestSupport.assertNoResult;
import static org.junit.Assert.assertTrue;

public class BooleanDecisionTableTest {

  @Test
  public void noRules() {
    assertNoResult(BooleanDecisionTable.flex((r, f) -> true), null);
  }

  @Test
  public void alwaysFalse() {
    final BooleanDecisionTable<Object, Object> decisionTable = BooleanDecisionTable
        .flex((r, f) -> false, "");
    assertNoResult(decisionTable, null);
  }

  @Test
  public void alwaysTrue() {
    final BooleanDecisionTable<Object, Object> decisionTable = BooleanDecisionTable
        .flex((r, f) -> true, "");
    assertMatch(decisionTable, null);
  }

  @Test
  public void checkSize() {
    final BooleanDecisionTable<Integer, Integer> decisionTable = BooleanDecisionTable
        .flex((t, v) -> v >= t, 7);
    assertMatch(decisionTable, 8);
    assertNoResult(decisionTable, 3);
  }

  @Test
  public void blackListing() {
    final BooleanDecisionTable<String, String> decisionTable = BooleanDecisionTable
        .flex((r, f) -> f.contains(r), "no", "yes", "maybe");
    assertMatch(decisionTable, "yes");
    assertMatch(decisionTable, "this contains no");
    assertMatch(decisionTable, "noyesmaybe");
    assertNoResult(decisionTable, "everything clear in here");
    assertNoResult(decisionTable, "CAPITAL IS HONORED: NO");
  }

  private <R, F> void assertMatch(final BooleanDecisionTable<R, F> decisionTable, final F facts) {
    assertTrue("Expected a match.", decisionTable.test(facts));
  }

}