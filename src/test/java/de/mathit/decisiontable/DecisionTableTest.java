package de.mathit.decisiontable;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static de.mathit.decisiontable.TestSupport.assertResult;

public class DecisionTableTest {

  private final static BiPredicate<Integer, String> LENGTH = (l, s) -> s.length() == l;

  @Test
  public void flex() {
    final DecisionTable<Integer, String, String> decisionTable = DecisionTable
        .flex(LENGTH, (r, f) -> String.format("'%s' has length %d", f, r),
            Arrays.asList(1, 2, 3, 4, 5));

    assertResult(decisionTable, "ab", "'ab' has length 2");
    assertResult(decisionTable, "hello", "'hello' has length 5");
  }

}