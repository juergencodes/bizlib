package de.mathit.streams;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Different implementations of {@link java.util.stream.Collector} to simplify working with
 * streams.
 */
public class MyCollectors {

  /**
   * Create a collector that groups elements and returns a list of groups.
   *
   * @param predicate {@link BiPredicate} that decides if two elements are considered equal (i.e.
   *                  belong to same group)
   * @param grouper   {@link Function} that converts from element to group
   * @param combiner  {@link BiFunction} that combines an E into an existing group G
   * @param <E>       Type of elements
   * @param <G>       Type of groups (may be same as E)
   * @return List of G
   */
  public static <E, G> Collector<E, List<GroupStore<E, G>>, List<G>> groupAndAggregate(
      final BiPredicate<E, E> predicate, final Function<E, G> grouper,
      final BiFunction<E, G, G> combiner) {
    final BiFunction<E, G, G> aggregator = (e, g) -> g;
    return groupAndAggregate(predicate, grouper, combiner, aggregator);
  }

  /**
   * Create a collector that groups elements and allows a final step (aggregator).
   *
   * @param predicate  {@link BiPredicate} that decides if two elements are considered equal (i.e.
   *                   belong to same group)
   * @param grouper    {@link Function} that converts from element to group
   * @param combiner   {@link BiFunction} that combines an E into an existing group G
   * @param aggregator {@link BiFunction} that performs the final operation to convert an E and G to
   *                   any wanted output format A
   * @param <E>        Type of elements
   * @param <G>        Type of groups (may be same as E)
   * @param <A>        Result type, constructed from E and G
   * @return List of A
   */
  public static <E, G, A> Collector<E, List<GroupStore<E, G>>, List<A>> groupAndAggregate(
      final BiPredicate<E, E> predicate, final Function<E, G> grouper,
      final BiFunction<E, G, G> combiner, final BiFunction<E, G, A> aggregator) {
    final Supplier<List<GroupStore<E, G>>> supplier = LinkedList::new;
    final BiConsumer<List<GroupStore<E, G>>, E> accumulator = (x, a) -> {
      final Optional<GroupStore<E, G>> existing = x.stream()
          .filter(t -> predicate.test(t.example, a)).findFirst();
      if (existing.isPresent()) {
        final GroupStore<E, G> groupStore = existing.get();
        groupStore.group = combiner.apply(a, groupStore.group);
      } else {
        final GroupStore<E, G> groupStore = new GroupStore<>();
        groupStore.example = a;
        groupStore.group = grouper.apply(a);
        x.add(groupStore);
      }
    };
    final BinaryOperator<List<GroupStore<E, G>>> listCombiner = (x, y) -> {
      x.addAll(y);
      return x;
    };
    final Function<List<GroupStore<E, G>>, List<A>> finisher = x -> x.stream()
        .map(a -> aggregator.apply(a.example, a.group)).collect(Collectors.toList());

    return Collector.of(supplier, accumulator, listCombiner, finisher);
  }


  private static class GroupStore<V, G> {

    private V example;
    private G group;
  }

}