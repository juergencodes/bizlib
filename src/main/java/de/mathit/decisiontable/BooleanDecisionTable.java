package de.mathit.decisiontable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Specialized version of {@link DecisionTable} that accepts {@link Boolean} as output. With this
 * limitation it can be tested if facts match any rule and hence an implementation of this serves as
 * {@link Predicate} for facts.
 *
 * @param <R> Rule type
 * @param <F> Fact type
 */
public interface BooleanDecisionTable<R, F> extends DecisionTable<R, F, Boolean>, Predicate<F> {

  @Override
  default boolean test(final F facts) {
    final Optional<Boolean> output = first(facts);
    return output.isPresent() && output.get();
  }


  @SafeVarargs
  static <R, F> BooleanDecisionTable<R, F> flex(final BiPredicate<R, F> matcher, final R... rules) {
    return BooleanDecisionTable.flex(matcher, Arrays.asList(rules));
  }

  static <R, F> BooleanDecisionTable<R, F> flex(final BiPredicate<R, F> matcher,
      final Iterable<R> rules) {
    return new BooleanDecisionTable<R, F>() {
      @Override
      public Iterable<R> rules() {
        return rules;
      }

      @Override
      public BiPredicate<R, F> matcher() {
        return matcher;
      }

      @Override
      public BiFunction<R, F, Boolean> finalizer() {
        return (r, f) -> Boolean.TRUE;
      }
    };
  }

}