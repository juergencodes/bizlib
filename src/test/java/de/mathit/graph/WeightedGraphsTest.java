package de.mathit.graph;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WeightedGraphsTest {

  @Test
  public void adjacentFunction() {
    final Set<Integer> nodes = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        .collect(Collectors.toSet());
    final Function<Integer, Map<Integer, Integer>> function = x -> {
      final Map<Integer, Integer> successors = new HashMap<>();
      if (nodes.contains(x * 2)) {
        successors.put(x * 2, x);
      }
      return successors;
    };
    final WeightedGraph<Integer, Integer> graph = WeightedGraphs
        .adjacentFunction((Supplier<Stream<Integer>>) () -> nodes.stream(), function);

    assertEquals("Wrong result.", Integer.valueOf(1), graph.getWeight(1, 2).get());
    assertEquals("Wrong result.", Integer.valueOf(3), graph.getWeight(3, 6).get());
    assertEquals("Wrong result.", Integer.valueOf(8), graph.getWeight(8, 16).get());
    assertFalse("Expected no edge.", graph.getWeight(1, 3).isPresent());
    assertFalse("Expected no edge.", graph.getWeight(9, 18).isPresent());
    assertFalse("Expected no edge.", graph.getWeight(100, 100).isPresent());

    assertEquals("Wrong size of ins.", 1, graph.ins(16).count());
    assertEquals("Wrong size of ins.", 0, graph.ins(15).count());

    final Map<Integer, Long> inDegrees = graph.inDegrees();
    assertEquals("Wrong in degree.", Long.valueOf(1), inDegrees.get(16));
    assertEquals("Wrong in degree.", Long.valueOf(0), inDegrees.get(15));

    assertEquals("Wrong size of outs.", 1, graph.outs(1).count());
    assertEquals("Wrong size of outs.", 0, graph.outs(9).count());

    final Map<Integer, Long> outDegrees = graph.outDegrees();
    assertEquals("Wrong out degree.", Long.valueOf(1), outDegrees.get(1));
    assertEquals("Wrong out degree.", Long.valueOf(0), outDegrees.get(9));

    assertFalse("Expected 8 not to be leaf.", graph.isLeaf(8));
    assertTrue("Expected 9 to be leaf.", graph.isLeaf(9));

    assertEquals("Wrong set of leafs.", new HashSet<>(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16)),
        graph.leafs().collect(Collectors.toSet()));

    assertEquals("Wrong topological sort.",
        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
        graph.topologicalSort());
  }

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