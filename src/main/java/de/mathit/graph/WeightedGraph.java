package de.mathit.graph;

import java.util.Optional;

public interface WeightedGraph<N, W> extends Graph<N, WeightedGraph.WeightedEdge<N, W>> {

  default Optional<W> weight(N n1, N n2) {
    return edge(n1, n2).map(e -> e.getWeight());
  }

  interface WeightedEdge<N, W> {

    N getFrom();

    N getTo();

    W getWeight();

  }

}