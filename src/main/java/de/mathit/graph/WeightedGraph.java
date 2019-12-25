package de.mathit.graph;

import java.util.Optional;

public interface WeightedGraph<N, W> extends Graph<N, WeightedGraph.WeightedEdge<N, W>> {

  default Optional<W> getWeight(N n1, N n2) {
    return edge(n1, n2).map(e -> e.getWeight());
  }

  interface WeightedEdge<N, W> extends Graph.Edge {

    N getFrom();

    N getTo();

    W getWeight();

  }

}