package de.mathit.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A graph consists of a set N of nodes and a relation on the subset of VxV. This interface serves
 * as basis for all other graphs. Though, it can be used directly in any implementation, for the
 * sake of simplicity, it is recommend to use one of the child interfaces.
 *
 * @param <N> Type of nodes. Make sure to implement {@link #hashCode() and {@link #equals(Object)}}
 *            properly, as algorithms may rely on adding nodes to a {@link java.util.HashSet} or
 *            alike.
 * @param <E> Type of edges.
 */
public interface Graph<N, E> {

  /**
   * Get the nodes as a stream. Note: Since creation of a {@link Stream} may be expensive,
   * algorithms should not call this more often, but rather call once and collect the result.
   *
   * @return {@link Stream} of nodes
   */
  Stream<N> nodes();

  /**
   * Get the edge between two nodes, if it exists.
   *
   * @param n1 First node
   * @param n2 Second node
   * @return {@link Optional} for a potentially existing edge
   */
  Optional<E> edge(N n1, N n2);

  /**
   * More rigid implementation of {@link #edge}, in case the caller know that the edge exists. In
   * case this is not true, a runtime exception is thrown.
   *
   * @param n1 First node
   * @param n2 Second node
   * @return The
   * @throws IllegalArgumentException in case there is no edge
   */
  default E edgeOrError(final N n1, final N n2) {
    return edge(n1, n2)
        .orElseThrow(() -> new IllegalArgumentException("No edge from " + n1 + " to " + n2 + "."));
  }

  /**
   * Report all the nodes that point torwards a certain node as a {@link Stream}.
   *
   * @param node The node
   * @return In node
   */
  default Stream<E> ins(final N node) {
    return nodes().map(n -> edge(n, node)).filter(Optional::isPresent).map(Optional::get);
  }

  /**
   * Report all the nodes that point from a certain node as a {@link Stream}.
   *
   * @param node The node
   * @return Out node
   */
  default Stream<E> outs(final N node) {
    return nodes().map(n -> edge(node, n)).filter(Optional::isPresent).map(Optional::get);
  }

  /**
   * Reports as a {@link Map} the in degree of all nodes, i.e. the number of nodes that point
   * towards a node.
   *
   * @return {@link Map} of in degrees
   */
  default Map<N, Long> inDegrees() {
    return nodes().collect(Collectors.toMap(n -> n, n -> ins(n).count()));
  }

  /**
   * Reports as a {@link Map} the out degree of all nodes, i.e. the number of nodes that point from
   * a node.
   *
   * @return {@link Map} of out degrees
   */
  default Map<N, Long> outDegrees() {
    return nodes().collect(Collectors.toMap(n -> n, n -> outs(n).count()));
  }

  /**
   * Determine if a certain node is a leaf, i.e. has no successors.
   *
   * @param node The node
   * @return <code>true</code> if node is a leaf, <code>false</code> else
   */
  default boolean isLeaf(final N node) {
    return outs(node).count() == 0;
  }

  /**
   * Reports all leaf nodes as a {@link Stream}.
   *
   * @return All leaf nodes
   */
  default Stream<N> leafs() {
    return nodes().filter(n -> isLeaf(n));
  }

  /**
   * Execute the dijkstra's algorithm to find the shortest paths from one certain node to all other
   * nodes in the graph. In case no path exists, no path will be returned in the result.
   *
   * @param node     Start node
   * @param distance Distance function
   * @return Map of all (shortest) paths from the given start node
   */
  default Map<N, List<E>> dijkstra(final N node, final Function<E, Integer> distance) {
    final Map<N, Integer> distances = new HashMap<>();
    final Map<N, N> predecessors = new HashMap<>();
    distances.put(node, 0);
    final Set<N> remainingNodes = nodes().collect(Collectors.toSet());

    while (!remainingNodes.isEmpty()) {
      Integer smallestDistance = null;
      N nodeWithSmallestDistance = null;
      for (final N n : remainingNodes) {
        if (distances.containsKey(n) && (smallestDistance == null
            || distances.get(n) < smallestDistance)) {
          smallestDistance = distances.get(n);
          nodeWithSmallestDistance = n;
        }
      }
      remainingNodes.remove(nodeWithSmallestDistance);

      if (nodeWithSmallestDistance == null) {
        break;
      }
      final N u = nodeWithSmallestDistance;
      final Integer distanceToU = smallestDistance;
      nodes().filter(v -> remainingNodes.contains(v)).filter(v -> edge(u, v).isPresent())
          .forEach(v -> {
            final Integer alternativ = distanceToU + distance.apply(edgeOrError(u, v));
            if (!distances.containsKey(v) || alternativ < distances.get(v)) {
              distances.put(v, alternativ);
              predecessors.put(v, u);
            }
          });
    }

    final Map<N, List<E>> paths = new HashMap<>();
    nodes().filter(n -> !n.equals(node)).forEach(n -> {
      final List<E> path = new LinkedList<>();
      N u = n;
      while (predecessors.containsKey(u)) {
        path.add(0, edgeOrError(predecessors.get(u), u));
        u = predecessors.get(u);
      }
      if (path.size() > 0) {
        paths.put(n, path);
      }
    });

    return paths;
  }

  /**
   * Return a topological sort of the graph, in case one exists or <code>null</code> else.
   *
   * @return Topological sort or <code>null</code>
   */
  default List<N> topologicalSort() {
    final Map<N, Long> inDegrees = inDegrees();
    Optional<N> zeroNode;
    final List<N> result = new LinkedList<>();
    final Set<N> visited = new HashSet<>();
    do {
      zeroNode = inDegrees.entrySet().stream().filter(e -> !visited.contains(e.getKey()))
          .filter(e -> e.getValue() == 0).findAny().map(e -> e.getKey());
      if (zeroNode.isPresent()) {
        final N theZeroNode = zeroNode.get();
        visited.add(theZeroNode);
        result.add(theZeroNode);
        nodes().filter(n -> edge(theZeroNode, n).isPresent())
            .forEach(n -> inDegrees.put(n, inDegrees.get(n) - 1));
      }
    } while (zeroNode.isPresent());
    return result.isEmpty() ? null : result;
  }

  /**
   * Reports if the graph has a cycle.
   *
   * @return <code>true</code> if graph has a cycle, <code>false</code> else
   */
  default boolean hasCycle() {
    return topologicalSort() == null;
  }

}