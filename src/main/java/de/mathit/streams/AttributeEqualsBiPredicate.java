package de.mathit.streams;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * {@link BiPredicate} implementation that allows to compare two objects of same type by passing any
 * number of functions that return an attribute of that object.
 *
 * @param <E> Type
 */
public class AttributeEqualsBiPredicate<E> implements BiPredicate<E, E> {

  private List<Function<E, Object>> attributes = new LinkedList<>();

  public AttributeEqualsBiPredicate<E> addAttribute(final Function<E, Object> attribute) {
    attributes.add(attribute);
    return this;
  }

  @Override
  public boolean test(final E entity1, final E entity2) {
    return attributes.stream().allMatch(a -> Objects.equals(a.apply(entity1), a.apply(entity2)));
  }

}