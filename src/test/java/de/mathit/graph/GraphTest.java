package de.mathit.graph;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GraphTest {

  @Test
  public void ins() {
    final Graph<String, String> graph = graph("abcde", "ac", "bc", "cd");
    assertEdges(graph.ins("c"), "ac", "bc");
    assertEdges(graph.ins("d"), "cd");
    assertEdges(graph.ins("e"));
  }

  @Test
  public void outs() {
    final Graph<String, String> graph = graph("abcde", "ab", "ac", "cd");

    assertEdges(graph.outs("a"), "ab", "ac");
    assertEdges(graph.outs("c"), "cd");
    assertEdges(graph.outs("e"));
  }

  private void assertEdges(final Stream<String> edges, final String... expectedEdges) {
    final Set<String> edgesSet = edges.collect(Collectors.toSet());

    assertEquals("Wrong amount of edges.", expectedEdges.length, edgesSet.size());
    assertEquals("Missing edges.", set(expectedEdges), edgesSet);
  }

  @Test
  public void inDegrees() {
    final Graph<String, String> graph = graph("abcde", "ac", "bc", "cd");
    final Map<String, Long> inDegrees = graph.inDegrees();

    assertDegree(inDegrees, "c", 2L);
    assertDegree(inDegrees, "d", 1L);
    assertDegree(inDegrees, "e", 0L);
  }

  @Test
  public void outDegrees() {
    final Graph<String, String> graph = graph("abcde", "ab", "ac", "cd");
    final Map<String, Long> outDegrees = graph.outDegrees();

    assertDegree(outDegrees, "a", 2L);
    assertDegree(outDegrees, "c", 1L);
    assertDegree(outDegrees, "e", 0L);
  }

  private void assertDegree(final Map<String, Long> degrees, final String node,
      final Long expectedDegree) {
    assertEquals("Wrong degree.", expectedDegree, degrees.get(node));
  }

  @Test
  public void isLeaf() {
    final Graph<String, String> graph = graph("ab", "ab");
    assertFalse("Expected not to be leaf.", graph.isLeaf("a"));
    assertTrue("Expected to be leaf.", graph.isLeaf("b"));
  }

  @Test
  public void leafs() {
    final Graph<String, String> graph = graph("abc", "ab", "ac");
    assertEquals("Wrong leafs.", set("b", "c"), graph.leafs().collect(Collectors.toSet()));
  }

  @Test
  public void dijkstra() {
    final Graph<String, String> graph = graph("abcde", "ab", "bc", "cd");

    final Map<String, List<String>> paths = graph.dijkstra("a", e -> 1);
    assertEquals("Wrong path.", Arrays.asList("ab"), paths.get("b"));
    assertEquals("Wrong path.", Arrays.asList("ab", "bc"), paths.get("c"));
    assertEquals("Wrong path.", Arrays.asList("ab", "bc", "cd"), paths.get("d"));
    assertNull("Expected no path.", paths.get("e"));
  }

  @Test
  public void topologicalSort() {
    final Graph<String, String> graph = graph("abcdefghijklmnop", "ab", "bd", "cf", "dh", "ej",
        "fl", "gn", "hp");

    assertEquals("Wrong topological sort.", Arrays
            .asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"),
        graph.topologicalSort());
  }

  @Test
  public void hasCycle() {
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
    assertFalse("Expected no cycle.", graph("abc", "ab", "bc").hasCycle());
    assertTrue("Expected a cycle.", graph("abc", "ab", "bc", "ca").hasCycle());
  }


  private Graph<String, String> graph(final String nodes, final String... edges) {
    final List<String> nodesList = Arrays.asList(nodes.split(""));
    final List<String> edgesList = Arrays.asList(edges);
    return new Graph<String, String>() {
      @Override
      public Stream<String> nodes() {
        return nodesList.stream();
      }

      @Override
      public Optional<String> edge(final String n1, final String n2) {
        return Optional.ofNullable(edgesList.contains(n1 + n2) ? n1 + n2 : null);
      }
    };
  }

  private HashSet<String> set(final String... values) {
    return new HashSet<>(Arrays.asList(values));
  }


}