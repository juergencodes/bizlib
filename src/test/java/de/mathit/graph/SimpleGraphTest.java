package de.mathit.graph;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleGraphTest {

  @Test
  public void adjacentFunction() {
    final Supplier<Stream<Character>> nodes = () -> Stream.of('a', 'b', 'c', 'd');
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

    assertEquals("Wrong set of leafs.", Collections.singleton('d'),
        graph.leafs().collect(Collectors.toSet()));

  }

}