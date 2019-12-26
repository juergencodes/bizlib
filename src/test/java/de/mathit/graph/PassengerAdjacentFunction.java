package de.mathit.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static de.mathit.graph.Passenger.Cabbage;
import static de.mathit.graph.Passenger.Ferryman;
import static de.mathit.graph.Passenger.Goat;
import static de.mathit.graph.Passenger.Wolf;

public class PassengerAdjacentFunction implements
    Function<Set<Passenger>, Map<Set<Passenger>, String>> {

  private static Predicate<Collection<Passenger>> IS_VALID = c -> {
    if (c.contains(Ferryman)) {
      return true;
    }
    if (c.contains(Wolf) && c.contains(Goat)) {
      return false;
    }
    if (c.contains(Goat) && c.contains(Cabbage)) {
      return false;
    }
    return true;
  };


  @Override
  public Map<Set<Passenger>, String> apply(final Set<Passenger> s) {
    final Map<Set<Passenger>, String> result = new HashMap<>();
    if (s.containsAll(Arrays.asList(Passenger.values()))) {
      return result;
    }

    if (s.contains(Ferryman)) {
      for (final Passenger p : s) {
        final Set<Passenger> newTo = new HashSet<>();
        newTo.addAll(s);
        final Set<Passenger> passengers = new HashSet<>();
        passengers.add(p);
        if (!Ferryman.equals(p)) {
          passengers.add(Ferryman);
        }
        newTo.removeAll(passengers);

        if (isValid(newTo)) {
          result.put(newTo, String.format("%s back", passengers));
        }
      }
    } else {

      final Set<Passenger> from = new HashSet<>(Arrays.asList(Passenger.values()));
      from.removeAll(s);
      for (final Passenger p : from) {
        final Set<Passenger> newTo = new HashSet<>();
        newTo.addAll(s);
        final Set<Passenger> passengers = new HashSet<>();
        passengers.add(p);
        if (!Ferryman.equals(p)) {
          passengers.add(Ferryman);
        }
        newTo.addAll(passengers);

        if (isValid(newTo)) {
          result.put(newTo, String.format("%s forth", passengers));
        }
      }
    }

    return result;
  }

  private boolean isValid(final Set<Passenger> s) {
    final Set<Passenger> from = new HashSet<>(Arrays.asList(Passenger.values()));
    from.removeAll(s);
    return IS_VALID.test(from) && IS_VALID.test(s);
  }

}