package de.mathit.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static de.mathit.graph.Passenger.Ferryman;

public class PassengerAdjacentFunction implements
    Function<PassengerState, Map<PassengerState, String>> {

  private static final Function<Collection<Passenger>, Collection<Passenger>> WHO_CAN_MOVE = c -> {
    if (!c.contains(Ferryman)) {
      return Collections.emptyList();
    } else {
      return c;
    }
  };

  private static final Function<Passenger, Collection<Passenger>> WHO_MUST_FOLLOW = p -> {
    if (Ferryman.equals(p)) {
      return Collections.singleton(p);
    } else {
      final List<Passenger> list = new LinkedList<>();
      list.add(p);
      list.add(Ferryman);
      return list;
    }
  };

  private static Predicate<Collection<Passenger>> IS_VALID = c -> {
    if (c.contains(Ferryman)) {
      return true;
    }
    if (c.contains(Passenger.Wolf) && c.contains(Passenger.Goat)) {
      return false;
    }
    if (c.contains(Passenger.Goat) && c.contains(Passenger.Cabbage)) {
      return false;
    }
    return true;
  };


  @Override
  public Map<PassengerState, String> apply(final PassengerState s) {
    final Map<PassengerState, String> result = new HashMap<>();
    if (s.getFrom().isEmpty()) {
      return result;
    }
    for (final Passenger p : WHO_CAN_MOVE.apply(s.getFrom())) {
      final Set<Passenger> newFrom = new HashSet<>();
      newFrom.addAll(s.getFrom());
      final Set<Passenger> newTo = new HashSet<>();
      newTo.addAll(s.getTo());
      final Collection<Passenger> passengers = WHO_MUST_FOLLOW.apply(p);
      for (final Passenger t : passengers) {
        newFrom.remove(t);
        newTo.add(t);
      }
      if (IS_VALID.test(newFrom) && IS_VALID.test(newTo)) {
        result.put(new PassengerState(newFrom, newTo), String.format("%s forth", passengers));
      }
    }
    for (final Passenger p : WHO_CAN_MOVE.apply(s.getTo())) {
      final Set<Passenger> newFrom = new HashSet<>();
      newFrom.addAll(s.getFrom());
      final Set<Passenger> newTo = new HashSet<>();
      newTo.addAll(s.getTo());
      final Collection<Passenger> passengers = WHO_MUST_FOLLOW.apply(p);
      for (final Passenger t : passengers) {
        newTo.remove(t);
        newFrom.add(t);
      }
      if (IS_VALID.test(newFrom) && IS_VALID.test(newTo)) {
        result.put(new PassengerState(newFrom, newTo), String.format("%s back", passengers));
      }
    }
    return result;
  }

}