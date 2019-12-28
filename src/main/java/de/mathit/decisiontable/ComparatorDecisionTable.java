package de.mathit.decisiontable;

import java.util.Comparator;
import java.util.Optional;

/**
 * Specialized version of {@link GenericDecisionTable} that uses {@link Integer} as output to assign
 * every row a row number. The row number is not meant to be exposed to the outside world, but can
 * of course be, if required. Rather, it allows this class to act as {@link Comparator}.
 *
 * @param <R> Row type
 * @param <F> Fact type
 */
public class ComparatorDecisionTable<R, F> extends GenericDecisionTable<R, F, Integer> implements
    Comparator<F> {

  private int nextRowNum = 1;

  public ComparatorDecisionTable<R, F> addRow(final R row) {
    addRow(row, nextRowNum++);
    return this;
  }

  @Override
  public int compare(final F fact1, final F fact2) {
    final Optional<Integer> row1 = first(fact1);
    final Optional<Integer> row2 = first(fact2);
    if (!row1.isPresent() && !row2.isPresent()) {
      return 0;
    } else if (!row2.isPresent()) {
      return 1;
    } else if (!row1.isPresent()) {
      return -1;
    }
    return row1.get().compareTo(row2.get());
  }

}