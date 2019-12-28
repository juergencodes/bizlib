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

  public static <V, G> Collector<V, List<GroupStore<V, G>>, List<G>> groupAndAggregate(
      final BiPredicate<V, V> predicate, final Function<V, G> grouper,
      final BiFunction<V, G, G> combiner) {
    final BiFunction<V, G, G> aggregator = (v, g) -> g;
    return groupAndAggregate(predicate, grouper, combiner, aggregator);
  }


  public static <V, G, A> Collector<V, List<GroupStore<V, G>>, List<A>> groupAndAggregate(
      final BiPredicate<V, V> predicate, final Function<V, G> grouper,
      final BiFunction<V, G, G> combiner, final BiFunction<V, G, A> aggregator) {
    final Supplier<List<GroupStore<V, G>>> supplier = LinkedList::new;
    final BiConsumer<List<GroupStore<V, G>>, V> accumulator = (x, a) -> {
      final Optional<GroupStore<V, G>> existing = x.stream()
          .filter(t -> predicate.test(t.example, a)).findFirst();
      if (existing.isPresent()) {
        final GroupStore<V, G> groupStore = existing.get();
        groupStore.group = combiner.apply(a, groupStore.group);
      } else {
        final GroupStore<V, G> groupStore = new GroupStore<>();
        groupStore.example = a;
        groupStore.group = grouper.apply(a);
        x.add(groupStore);
      }
    };
    final BinaryOperator<List<GroupStore<V, G>>> listCombiner = (x, y) -> {
      x.addAll(y);
      return x;
    };
    final Function<List<GroupStore<V, G>>, List<A>> finisher = x -> x.stream()
        .map(a -> aggregator.apply(a.example, a.group)).collect(Collectors.toList());

    return Collector.of(supplier, accumulator, listCombiner, finisher);
  }


  private static class GroupStore<V, G> {

    private V example;
    private G group;
  }

}