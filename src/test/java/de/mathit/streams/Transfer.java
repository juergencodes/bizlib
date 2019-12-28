package de.mathit.streams;

import java.math.BigDecimal;

public class Transfer {

  private final int day;
  private final int month;
  private final String accountFrom;
  private final String accountTo;
  private final BigDecimal amount;

  public Transfer(final int day, final int month, final String accountFrom, final String accountTo,
      final BigDecimal amount) {
    this.day = day;
    this.month = month;
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.amount = amount;
  }

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
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

}