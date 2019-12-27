package de.mathit.graph;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test {@link WeightedGraphs#adjacentFunction(Object, Function)} and {@link Graph#dijkstra(Object,
 * Function)}. As such, yes, it is a unit test, but it actually serves more as a kind of showcase.
 */
public class PassengerTest {

  @Test
  public void passenger() {

    final Set<Passenger> all = Stream.of(Passenger.values()).collect(Collectors.toSet());

    final Set<Passenger> startState = new HashSet<>();

    final WeightedGraph<Set<Passenger>, String> graph = WeightedGraphs
        .adjacentFunction(startState, new PassengerAdjacentFunction());

    final List<WeightedGraph.WeightedEdge<Set<Passenger>, String>> path = graph
        .dijkstra(startState, e -> 1).get(all);
    assertEquals("Wrong length of path.", 7, path.size());
    assertEquals("Wrong start state.", startState, path.get(0).getFrom());
    assertEquals("Wrong end state.", all, path.get(path.size() - 1).getTo());
    final Set<Set<Passenger>> allStates = graph.nodes().collect(Collectors.toSet());
    for (final WeightedGraph.WeightedEdge<Set<Passenger>, String> state : path) {
      if (!allStates.contains(state.getFrom()) || !allStates.contains(state.getTo())) {
        fail("Illegal state in path.");
      }
    }
    for (final WeightedGraph.WeightedEdge<Set<Passenger>, String> edge : path) {
      System.out.println(edge.getWeight() + "\t ------> " + edge.getTo() + " over there");
    }
  }

}