package de.mathit.graph;

import java.util.Set;

public class PassengerState {

  private final Set<Passenger> from;
  private final Set<Passenger> to;


  public PassengerState(final Set<Passenger> from, final Set<Passenger> to) {
    this.from = from;
    this.to = to;
  }

  public Set<Passenger> getFrom() {
    return from;
  }

  public Set<Passenger> getTo() {
    return to;
  }

  @Override
  public String toString() {
    return String.format("%s | %s", from, to);
  }

  @Override
  public int hashCode() {
    return 3 + from.hashCode() + 17 * to.hashCode();
  }

  @Override
  public boolean equals(final Object object) {
    if (!(object instanceof PassengerState)) {
      return false;
    }
    final PassengerState passengerState = (PassengerState) object;
    return passengerState.from.equals(from) && passengerState.to.equals(to);
  }

}