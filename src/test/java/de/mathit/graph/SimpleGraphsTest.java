package de.mathit.graph;

import de.mathit.graph.SimpleGraph.SimpleEdge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link SimpleGraphs}.
 */
@RunWith(Parameterized.class)
public class SimpleGraphsTest {


  @Parameterized.Parameters
  public static Graph[] graphs() {
    final Map<Character, Collection<Character>> map = new HashMap<>();
    map.put('a', Arrays.asList('b'));
    map.put('b', Arrays.asList('c'));
    map.put('c', Arrays.asList('d'));
    map.put('d', Arrays.asList());
    final SimpleGraph<Character> graph0 = SimpleGraphs.adjacentList(map);

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

    final SimpleGraph<Character> graph1 = SimpleGraphs.adjacentFunction('a', function);

    final Supplier<Stream<Character>> nodes = () -> Stream.of('a', 'b', 'c', 'd', 'e');
    final SimpleGraph<Character> graph2 = SimpleGraphs.adjacentFunction(nodes, function);

    return new Graph[]{graph0, graph1, graph2};
  }

  private final SimpleGraph<Character> graph;

  public SimpleGraphsTest(final SimpleGraph<Character> graph) {
    this.graph = graph;
  }

  @Test
  public void nodes() {
    final Set<Character> nodes = graph.nodes().collect(Collectors.toSet());
    assertTrue("Expected nodes missing.", nodes.containsAll(Arrays.asList('a', 'b', 'c', 'd')));

    nodes.removeAll(Arrays.asList('a', 'b', 'c', 'd', 'e'));
    assertTrue("Found unexpected nodes.", nodes.isEmpty());
  }

  @Test
  public void edge() {
    assertEdge('a', 'b');
    assertEdge('b', 'c');
    assertEdge('c', 'd');

    assertEquals("Wrong size of outs.", 1, graph.outs('a').count());
    assertEquals("Wrong size of outs.", 0, graph.outs('d').count());
  }

  private void assertEdge(final Character n1, final Character n2) {
    final Optional<SimpleEdge<Character>> edgeOptional = graph.edge(n1, n2);
    assertTrue("Expected an edge.", edgeOptional.isPresent());
    final SimpleEdge<Character> edge = edgeOptional.get();
    assertEquals("Wrong from.", n1, edge.getFrom());
    assertEquals("Wrong to.", n2, edge.getTo());
  }

  @Test
  public void noEdge() {
    assertNoEdge('a', 'c');
    assertNoEdge('c', 'a');
    assertNoEdge('d', 'd');
  }

  private void assertNoEdge(final Character n1, final Character n2) {
    assertFalse("Expected no edge.", graph.edge(n1, n2).isPresent());
  }

  @Test
  public void outs() {
    assertOuts('a', 'b');
    assertOuts('b', 'c');
    assertOuts('c', 'd');
    assertOuts('d');
    assertOuts('e');
    assertOuts('f');
  }

  private void assertOuts(final Character n, final Character... expectedOuts) {
    graph.outs(n).forEach(e -> assertEquals("Wrong from.", n, e.getFrom()));
    final Set<Character> outs = graph.outs(n).map(e -> e.getTo()).collect(Collectors.toSet());
    assertEquals("Wrong outs.", new HashSet<>(Arrays.asList(expectedOuts)), outs);
  }

}