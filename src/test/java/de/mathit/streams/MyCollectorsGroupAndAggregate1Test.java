package de.mathit.streams;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test {@link MyCollectors#groupAndAggregate} with {@link Transfer} and {@link Balance}.
 */
public class MyCollectorsGroupAndAggregate1Test {

  @Test
  public void test() {
    final List<Transfer> transfers = Arrays.asList(new Transfer(1, 2, "A", "B", BigDecimal.ONE),
        new Transfer(1, 2, "B", "C", BigDecimal.ONE), new Transfer(2, 3, "A", "B", BigDecimal.ONE));

    final List<Balance> balances = groupAndAggregate(transfers);
    assertNotNull("Balances are null.", balances);
    assertEquals("Wrong amount of balances.", 2, balances.size());
    assertBalance(balances.get(0), "A", "B", new BigDecimal("2"));
    assertBalance(balances.get(1), "B", "C", BigDecimal.ONE);
  }

  private void assertBalance(final Balance balance, final String expectedAccountFrom,
      final String expectedAccountTo, final BigDecimal expectedAmount) {
    assertEquals("Wrong account from.", expectedAccountFrom, balance.getAccountFrom());
    assertEquals("Wrong account to.", expectedAccountTo, balance.getAccountTo());
    assertEquals("Wrong amount.", expectedAmount, balance.getAmount());
  }

  private List<Balance> groupAndAggregate(final List<Transfer> transfers) {
    final AttributeEqualsBiPredicate<Transfer> predicate = new AttributeEqualsBiPredicate<>();
    predicate.addAttribute(t -> t.getAccountFrom()).addAttribute(t -> t.getAccountTo());

    final Function<Transfer, Balance> grouper = t -> new Balance(t.getAccountFrom(),
        t.getAccountTo(), t.getAmount());
    final BiFunction<Transfer, Balance, Balance> combiner = (t, b) -> new Balance(
        b.getAccountFrom(), t.getAccountTo(), t.getAmount().add(b.getAmount()));

    return transfers.stream().collect(MyCollectors.groupAndAggregate(predicate, grouper, combiner));
  }

}