package de.mathit.graph;

import de.mathit.graph.WeightedGraph.WeightedEdge;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WeightedGraphs {

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

  private static <N, W> WeightedEdge<N, W> createEdge(final N from, N to,
      final W weight) {
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