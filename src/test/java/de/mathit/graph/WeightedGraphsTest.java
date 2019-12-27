package de.mathit.graph;

import de.mathit.graph.WeightedGraph.WeightedEdge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
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
 * Test {@link WeightedGraphs}.
 */
@RunWith(Parameterized.class)
public class WeightedGraphsTest {

  @Parameterized.Parameters
  public static Graph[] graphs() {
    final Set<Integer> nodes = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        .collect(Collectors.toSet());
    final Function<Integer, Map<Integer, Integer>> function = x -> {
      final Map<Integer, Integer> successors = new HashMap<>();
      if (nodes.contains(x * 2)) {
        successors.put(x * 2, x);
      }
      return successors;
    };
    final WeightedGraph<Integer, Integer> graph0 = WeightedGraphs.adjacentFunction(1, function);

    final WeightedGraph<Integer, Integer> graph1 = WeightedGraphs
        .adjacentFunction((Supplier<Stream<Integer>>) () -> nodes.stream(), function);

    return new Graph[]{graph0, graph1};
  }

  private final WeightedGraph<Integer, Integer> graph;

  public WeightedGraphsTest(final WeightedGraph<Integer, Integer> graph) {
    this.graph = graph;
  }

  @Test
  public void nodes() {
    final Set<Integer> nodes = graph.nodes().collect(Collectors.toSet());
    assertTrue("Expected nodes missing.", nodes.containsAll(Arrays.asList(1, 2, 4, 8, 16)));

    nodes.removeAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
    assertTrue("Found unexpected nodes.", nodes.isEmpty());
  }

  @Test
  public void edges() {
    assertEdge(1, 2, 1);
    assertEdge(2, 4, 2);
    assertEdge(3, 6, 3);
    assertEdge(4, 8, 4);
    assertEdge(5, 10, 5);
    assertEdge(6, 12, 6);
    assertEdge(7, 14, 7);
    assertEdge(8, 16, 8);
  }

  private void assertEdge(final Integer n1, final Integer n2, final Integer expectedWeight) {
    final Optional<WeightedEdge<Integer, Integer>> edgeOptional = graph.edge(n1, n2);
    assertTrue("Expected an edge.", edgeOptional.isPresent());
    final WeightedEdge<Integer, Integer> edge = edgeOptional.get();
    assertEquals("Wrong from.", n1, edge.getFrom());
    assertEquals("Wrong to.", n2, edge.getTo());
    assertEquals("Wrong weight.", expectedWeight, edge.getWeight());
  }

  @Test
  public void noEdge() {
    assertNoEdge(1, 3);
    assertNoEdge(9, 18);
    assertNoEdge(100, 100);
  }

  private void assertNoEdge(final Integer n1, final Integer n2) {
    assertFalse("Expected no edge.", graph.edge(n1, n2).isPresent());
  }

  @Test
  public void outs() {
    assertOuts(1, 2);
    assertOuts(9);
  }

  private void assertOuts(final Integer n, final Integer... expectedOuts) {
    graph.outs(n).forEach(e -> assertEquals("Wrong from.", n, e.getFrom()));
    final Set<Integer> outs = graph.outs(n).map(e -> e.getTo()).collect(Collectors.toSet());
    assertEquals("Wrong outs.", new HashSet<>(Arrays.asList(expectedOuts)), outs);
  }

}