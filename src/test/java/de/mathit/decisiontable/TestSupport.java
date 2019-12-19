package de.mathit.decisiontable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

public class TestSupport {

  public static <R, F, O> void assertNoResult(final DecisionTable<R, F, O> decisionTable,
      final F facts) {
    final Optional<O> output = decisionTable.first(facts);
    assertNotNull("Output is null.", output);
    assertFalse("Expected no output.", output.isPresent());
  }

  public static <R, F, O> void assertResult(final DecisionTable<R, F, O> decisionTable,
      final F facts,
      final O expectedOutput) {
    final Optional<O> output = decisionTable.first(facts);
    assertNotNull("Output is null.", output);
    assertTrue("Expected an output.", output.isPresent());
    assertEquals("Wrong result.", expectedOutput, output.get());
  }

}