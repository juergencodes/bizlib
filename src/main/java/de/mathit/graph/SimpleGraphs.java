package de.mathit.graph;

import de.mathit.graph.SimpleGraph.SimpleEdge;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleGraphs {

  public static <N> SimpleGraph<N> adjacentFunction(final Supplier<Stream<N>> nodes,
      final Function<N, Collection<N>> adjacentFunction) {
    return new SimpleGraph<N>() {

      @Override
      public Stream<N> nodes() {
        return nodes.get();
      }

      // Faster by directly accessing adjacent function
      @Override
      public Stream<SimpleEdge<N>> outs(final N node) {
        return adjacentFunction.apply(node).stream().map(e -> createEdge(node, e));
      }

      @Override
      public Optional<SimpleEdge<N>> edge(final N n1, final N n2) {
        return Optional.ofNullable(adjacentFunction.apply(n1))
            .map(c -> c.contains(n2) ? createEdge(n1, n2) : null);
      }
    };
  }

  private static <N, W> SimpleEdge<N> createEdge(final N from, N to) {
    return new SimpleEdge<N>() {
      @Override
      public N getFrom() {
        return from;
      }

      @Override
      public N getTo() {
        return to;
      }

      @Override
      public String toString() {
        return String.format("Edge[%s -> %s]", from, to);
      }
    };
  }

}