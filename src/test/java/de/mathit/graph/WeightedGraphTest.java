package de.mathit.graph;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link WeightedGraph}.
 */
public class WeightedGraphTest {

  private WeightedGraph<Integer, Integer> graph;

  @Before
  public void initGraph() {
    graph = new WeightedGraph<Integer, Integer>() {
      @Override
      public Stream<Integer> nodes() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
      }

      @Override
      public Optional<WeightedEdge<Integer, Integer>> edge(final Integer n1, final Integer n2) {
        if (contained(n1) && contained(n2) && n2 == n1 * 2) {
          return Optional.of(new WeightedEdge<Integer, Integer>() {

            @Override
            public Integer getFrom() {
              return n1;
            }

            @Override
            public Integer getTo() {
              return n2;
            }

            @Override
            public Integer getWeight() {
              return n2 - n1;
            }
          });
        }
        return Optional.empty();
      }

      private boolean contained(final Integer node) {
        return nodes().anyMatch(n -> n.equals(node));
      }

    };
  }

  @Test
  public void getWeight() {
    assertWeight(1, 2, 1);
    assertWeight(2, 4, 2);
    assertWeight(3, 6, 3);
    assertWeight(4, 8, 4);
    assertWeight(5, 10, 5);
    assertWeight(6, 12, 6);
    assertWeight(7, 14, 7);
    assertWeight(8, 16, 8);
  }

  private void assertWeight(final Integer n1, final Integer n2, final Integer expectedWeight) {
    final Optional<Integer> weightOptional = graph.weight(n1, n2);
    assertTrue("Expected a weight.", weightOptional.isPresent());
    assertEquals("Wrong weight.", expectedWeight, weightOptional.get());
  }

  @Test
  public void noWeight() {
    assertNoWeight(1, 3);
    assertNoWeight(9, 18);
    assertNoWeight(100, 100);
  }

  private void assertNoWeight(final Integer n1, final Integer n2) {
    assertFalse("Expected no edge.", graph.weight(n1, n2).isPresent());
  }

}