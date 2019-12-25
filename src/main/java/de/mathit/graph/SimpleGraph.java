package de.mathit.graph;

/**
 * Simple graph that has no weighted edges. It simply consists of nodes and edges.
 *
 * @param <N> Node type.
 */
public interface SimpleGraph<N> extends Graph<N, SimpleGraph.SimpleEdge<N>> {

  interface SimpleEdge<N> extends Graph.Edge {

    N getFrom();

    N getTo();
  }
}