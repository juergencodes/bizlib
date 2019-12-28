package de.mathit.graph;

import de.mathit.graph.WeightedGraph.WeightedEdge;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WeightedGraphs {

  /**
   * Create a new {@link WeightedGraph} for a (not yet) known set of nodes and a function (the
   * adjacent function) that returns the successors of any given node and the weight. Initially, the
   * function will be used to explore all allowed nodes. In case you need to restrict the nodes,
   * please use {@link #adjacentFunction(Supplier, Function)} instead.
   *
   * @param startNode        Start node for exploring all nodes
   * @param adjacentFunction The adjacent function
   * @param <N>              Node type
   * @return {@link SimpleGraph} instance
   */
  public static <N, W> WeightedGraph<N, W> adjacentFunction(final N startNode,
      final Function<N, Map<N, W>> adjacentFunction) {

    final Set<N> allNodes = new HashSet<>();
    final Set<N> remaining = new HashSet<>();
    remaining.add(startNode);
    while (!remaining.isEmpty()) {
      final N next = remaining.iterator().next();
      remaining.remove(next);
      if (allNodes.add(next)) {
        remaining.addAll(adjacentFunction.apply(next).keySet());
      }
    }
    return adjacentFunction((Supplier<Stream<N>>) () -> allNodes.stream(), adjacentFunction);
  }


  /**
   * Create a new {@link WeightedGraph} for a known (or limited) set of nodes and a function (the
   * adjacent function) that returns the successors of any given node and the weight.
   *
   * @param nodes            Supplier of nodes (will be called multiple times when executing
   *                         algorithms)
   * @param adjacentFunction The adjacent function
   * @param <N>              Node type
   * @return {@link SimpleGraph} instance
   */
  public static <N, W> WeightedGraph<N, W> adjacentFunction(final Supplier<Stream<N>> nodes,
      final Function<N, Map<N, W>> adjacentFunction) {
    return new WeightedGraph<N, W>() {

      @Override
      public Stream<N> nodes() {
        return nodes.get();
      }

      // Faster by directly accessing adjacent function
      @Override
      public Stream<WeightedEdge<N, W>> outs(final N node) {
        return adjacentFunction.apply(node).entrySet().stream()
            .map(e -> createEdge(node, e.getKey(), e.getValue()));
      }

      @Override
      public Optional<WeightedEdge<N, W>> edge(final N n1, final N n2) {
        return Optional.ofNullable(adjacentFunction.apply(n1)).map(m -> m.get(n2))
            .map(w -> createEdge(n1, n2, w));
      }
    };
  }

  private static <N, W> WeightedEdge<N, W> createEdge(final N from, final N to, final W weight) {
    return new WeightedEdge<N, W>() {
      @Override
      public N getFrom() {
        return from;
      }

      @Override
      public N getTo() {
        return to;
      }

      @Override
      public W getWeight() {
        return weight;
      }

      @Override
      public String toString() {
        return String.format("Edge[%s -> %s (%s)]", from, to, weight);
      }
    };
  }

}