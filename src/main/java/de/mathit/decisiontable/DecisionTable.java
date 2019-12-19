package de.mathit.decisiontable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface DecisionTable<R, F, O> {

  Iterable<R> rules();

  BiPredicate<R, F> matcher();

  BiFunction<R, F, O> finalizer();

  default boolean allMatch(final F facts) {
    return StreamSupport.stream(rules().spliterator(), false)
        .allMatch(r -> matcher().test(r, facts));
  }

  default Iterable<O> all(final F facts) {
    return StreamSupport.stream(rules().spliterator(), false).filter(r -> matcher().test(r, facts))
        .map(r -> finalizer().apply(r, facts))
        .collect(Collectors.toCollection(() -> new LinkedList<>()));
  }

  default Optional<O> first(final F facts) {
    final Iterator<O> iterator = all(facts).iterator();
    return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
  }

  static <R, F, O> DecisionTable<R, F, O> flex(final BiPredicate<R, F> matcher,
      final BiFunction<R, F, O> finalizer, final Iterable<R> rules) {
    return new DecisionTable<R, F, O>() {
      @Override
      public Iterable<R> rules() {
        return rules;
      }

      @Override
      public BiPredicate<R, F> matcher() {
        return matcher;
      }

      @Override
      public BiFunction<R, F, O> finalizer() {
        return finalizer;
      }
    };
  }

}