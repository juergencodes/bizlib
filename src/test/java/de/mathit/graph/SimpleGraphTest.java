package de.mathit.graph;

import de.mathit.graph.SimpleGraph.SimpleEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleGraphTest {

  @Test
  public void adjacentFunction() {
    final Supplier<Stream<Character>> nodes = () -> Stream.of('a', 'b', 'c', 'd', 'e');
    final Function<Character, Collection<Character>> function = c -> {
      switch (c) {
        case 'a':
          return Arrays.asList('b');
        case 'b':
          return Arrays.asList('c');
        case 'c':
          return Arrays.asList('d');
        default:
          return Collections.emptyList();
      }
    };

    final SimpleGraph<Character> graph = SimpleGraphs.adjacentFunction(nodes, function);

    assertTrue("Expected an edge.", graph.edge('a', 'b').isPresent());
    assertFalse("Expected an edge.", graph.edge('a', 'c').isPresent());

    assertEquals("Wrong size of outs.", 1, graph.outs('a').count());
    assertEquals("Wrong size of outs.", 0, graph.outs('d').count());

    assertEquals("Wrong size of ins.", 0, graph.ins('a').count());
    assertEquals("Wrong size of ins.", 1, graph.ins('d').count());

    assertEquals("Wrong set of leafs.", new HashSet<>(Arrays.asList('d', 'e')),
        graph.leafs().collect(Collectors.toSet()));

    final Map<Character, List<SimpleEdge<Character>>> paths = graph.dijkstra('a');
    assertEquals("Wrong length of path.", 1, paths.get('b').size());
    assertEquals("Wrong length of path.", 2, paths.get('c').size());
    assertEquals("Wrong length of path.", 3, paths.get('d').size());
    assertNull("Expected no path.", paths.get('e'));

    assertEquals("Wrong topological sort.", Arrays.asList('a', 'b', 'c', 'd', 'e'),
        graph.topologicalSort());
  }

  @Test
  public void adjecentFunctionCycle() {
    final Supplier<Stream<Integer>> nodes = () -> Stream.of(1, 2, 3);
    final Function<Integer, Collection<Integer>> functionNoCycle = c -> {
      switch (c) {
        case 1:
          return Arrays.asList(2);
        case 2:
          return Arrays.asList(3);
        default:
          return Collections.emptyList();
      }
    };
    final Function<Integer, Collection<Integer>> functionCycle = c -> {
      switch (c) {
        case 1:
          return Arrays.asList(2);
        case 2:
          return Arrays.asList(3);
        case 3:
          return Arrays.asList(1);
        default:
          return Collections.emptyList();
      }
    };
    assertFalse("Expected no cycle.", SimpleGraphs.adjacentFunction(1, functionNoCycle).hasCycle());
    assertTrue("Expected a cycle.", SimpleGraphs.adjacentFunction(1, functionCycle).hasCycle());
  }


}