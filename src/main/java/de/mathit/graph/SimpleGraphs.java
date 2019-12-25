package de.mathit.graph;

import de.mathit.graph.SimpleGraph.SimpleEdge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleGraphs {

  /**
   * Create a new {@link SimpleGraph} for a (not yet) known set of nodes and a function (the
   * adjacent function) that returns the successors of any given node. Initially, the function will
   * be used to explore all allowed nodes. In case you need to restrict the nodes, please use {@link
   * #adjacentFunction(Supplier, Function)} instead.
   *
   * @param startNode        Start node for exploring all nodes
   * @param adjacentFunction The adjacent function
   * @param <N>              Node type
   * @return {@link SimpleGraph} instance
   */
  public static <N> SimpleGraph<N> adjacentFunction(final N startNode,
      final Function<N, Collection<N>> adjacentFunction) {

    final Set<N> allNodes = new HashSet<>();
    final Set<N> remaining = new HashSet<>();
    remaining.add(startNode);
    while (!remaining.isEmpty()) {
      final N next = remaining.iterator().next();
      remaining.remove(next);
      if (allNodes.add(next)) {
        remaining.addAll(adjacentFunction.apply(next));
      }
    }
    return adjacentFunction((Supplier<Stream<N>>) () -> allNodes.stream(), adjacentFunction);
  }

  /**
   * Create a new {@link SimpleGraph} for a known (or limited) set of nodes and a function (the
   * adjacent function) that returns the successors of any given node.
   *
   * @param nodes            Supplier of nodes (will be called multiple times when executing
   *                         algorithms)
   * @param adjacentFunction The adjacent function
   * @param <N>              Node type
   * @return {@link SimpleGraph} instance
   */
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