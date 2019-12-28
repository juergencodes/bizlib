package de.mathit.decisiontable;

import de.mathit.decisiontable.GenericDecisionTable.Row;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Implementation that offers builder methods to configure the instance. It is meant to be used
 * directly in the according business logic code as a direct datastructure. This usually makes sense
 * in case the logic is easy understandable and the rows are not complicated, because then
 * everything can be seen at one glance.
 *
 * @param <R> Row type
 * @param <F> Fact type
 * @param <O> Output type
 */
public class GenericDecisionTable<R, F, O> implements DecisionTable<Row<R, O>, F, O> {

  private final List<BiPredicate<R, F>> matchers = new LinkedList<>();
  private final List<Row<R, O>> rows = new LinkedList<>();

  public GenericDecisionTable<R, F, O> addCondition(final BiPredicate<R, F> matcher) {
    matchers.add(matcher);
    return this;
  }

  public GenericDecisionTable<R, F, O> addRow(final R input, final O output) {
    final Row<R, O> row = new Row<>();
    row.input = input;
    row.output = output;
    rows.add(row);
    return this;
  }

  @Override
  public Iterable<Row<R, O>> rules() {
    return rows;
  }

  @Override
  public BiPredicate<Row<R, O>, F> matcher() {
    return (r, f) -> matchers.stream().allMatch(m -> m.test(r.input, f));
  }

  @Override
  public BiFunction<Row<R, O>, F, O> finalizer() {
    return (r, f) -> r.output;
  }

  public static class Row<R, O> {

    private R input;
    private O output;
  }

}