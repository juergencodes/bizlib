package de.mathit.graph;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
      .adjacentFunction(() -> nodes.stream(), function);

    assertEquals("Wrong result.", Integer.valueOf(1), graph.getWeight(1, 2).get());
    assertEquals("Wrong result.", Integer.valueOf(3), graph.getWeight(3, 6).get());
    assertEquals("Wrong result.", Integer.valueOf(8), graph.getWeight(8, 16).get());
    assertFalse("Expected no edge.", graph.getWeight(1, 3).isPresent());
    assertFalse("Expected no edge.", graph.getWeight(9, 18).isPresent());
    assertFalse("Expected no edge.", graph.getWeight(100, 100).isPresent());

    assertEquals("Wrong size of outs.", 1, graph.outs(1).count());
    assertEquals("Wrong size of outs.", 0, graph.outs(9).count());

    assertEquals("Wrong size of ins.", 1, graph.ins(16).count());
    assertEquals("Wrong size of ins.", 0, graph.ins(15).count());

    assertFalse("Expected 8 not to be leaf.", graph.isLeaf(8));
    assertTrue("Expected 9 to be leaf.", graph.isLeaf(9));

    assertEquals("Wrong set of leafs.", new HashSet<>(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16)),
      graph.leafs().collect(Collectors.toSet()));
  }

}