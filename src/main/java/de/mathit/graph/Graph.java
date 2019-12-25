package de.mathit.graph;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * A graph consists of a set N of nodes and a relation on the subset of VxV. This interface serves
 * as basis for all other graphs. Though, it can be used directly in any implementation, for the
 * sake of simplicity, it is recommend to use one of the child interfaces.
 *
 * @param <N> Type of nodes. Make sure to implement {@link #hashCode() and {@link #equals(Object)}}
 *            properly, as algorithms may rely on adding nodes to a {@link java.util.HashSet} or
 *            alike.
 * @param <E> Type of edges. Must extend marker interface {@link Edge}.
 */
public interface Graph<N, E extends Graph.Edge> {

  Stream<N> nodes();

  Optional<E> edge(N n1, N n2);

  default Stream<E> ins(N node) {
    return nodes().map(n -> edge(n, node)).filter(Optional::isPresent).map(Optional::get);
  }

  default Stream<E> outs(N node) {
    return nodes().map(n -> edge(node, n)).filter(Optional::isPresent).map(Optional::get);
  }

  default boolean isLeaf(N node) {
    return outs(node).count() == 0;
  }

  default Stream<N> leafs() {
    return nodes().filter(n -> isLeaf(n));
  }

  interface Edge {

  }

}