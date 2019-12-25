package de.mathit.graph;

import java.util.List;
import java.util.Map;

/**
 * Simple graph that has no weighted edges. It simply consists of nodes and edges.
 *
 * @param <N> Node type.
 */
public interface SimpleGraph<N> extends Graph<N, SimpleGraph.SimpleEdge<N>> {

  default Map<N, List<SimpleEdge<N>>> dijkstra(N node) {
    return dijkstra(node, e -> 1);
  }

  interface SimpleEdge<N> extends Graph.Edge {

    N getFrom();

    N getTo();
  }
}