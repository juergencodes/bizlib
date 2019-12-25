package de.mathit.graph;

import de.mathit.graph.SimpleGraph.SimpleEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
  }



}