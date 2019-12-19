package de.mathit.decisiontable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Customizable implementation using a map as row. It is designed to be used in subclasses that
 * provide specific methods to encapsulate the matching logic and provide specific methods to create
 * rows. As such it can easily be used as {@link Function}
 *
 * @param <F> Facts type
 * @param <O> Output type
 */
public class CustomizableDecisionTable<F, O> implements DecisionTable<Map<String, Object>, F, O>,
    Function<F, O> {

  private final List<BiPredicate<Map<String, Object>, F>> matchers = new LinkedList<>();
  private final List<Map<String, Object>> rows = new LinkedList<>();
  private final BiFunction<Map<String, Object>, F, O> finalizer;

  public CustomizableDecisionTable(
      BiFunction<Map<String, Object>, F, O> finalizer) {
    this.finalizer = finalizer;
  }

  @Override
  public Iterable<Map<String, Object>> rules() {
    return rows;
  }

  @Override
  public BiPredicate<Map<String, Object>, F> matcher() {
    return (r, f) -> matchers.stream().allMatch(m -> m.test(r, f));
  }

  @Override
  public BiFunction<Map<String, Object>, F, O> finalizer() {
    return finalizer;
  }

  protected CustomizableDecisionTable<F, O> addCondition(
      final BiPredicate<Map<String, Object>, F> matcher) {
    matchers.add(matcher);
    return this;
  }

  protected CustomizableDecisionTable<F, O> addRow(final Map<String, Object> row) {
    rows.add(row);
    return this;
  }

  protected RowBuilder addRow() {
    return new RowBuilder(this);
  }

  @Override
  public O apply(final F facts) {
    return first(facts).orElse(null);
  }

  public class RowBuilder {

    private final CustomizableDecisionTable<F, O> table;
    private final Map<String, Object> row = new HashMap<>();

    private RowBuilder(final CustomizableDecisionTable<F, O> table) {
      this.table = table;
    }

    public RowBuilder parameter(final String name, final Object value) {
      row.put(name, value);
      return this;
    }

    public void finish() {
      table.addRow(row);
    }

  }

}