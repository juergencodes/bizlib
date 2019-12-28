package de.mathit.streams;

import java.math.BigDecimal;

public class Balance {

  private final String accountFrom;
  private final String accountTo;
  private BigDecimal amount;

  public Balance(final String accountFrom, final String accountTo, final BigDecimal amount) {
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.amount = amount;
  }

  public String getAccountFrom() {
    return accountFrom;
  }

  public String getAccountTo() {
    return accountTo;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

}