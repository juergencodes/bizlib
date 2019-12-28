package de.mathit.repository.entity.simple;

import de.mathit.repository.EntitySupport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleEntity extends EntitySupport {

  private final Map<String, Object> values = new HashMap<>();

  SimpleEntity(final String name) {
    super(name);
  }

  <T> void add(final String name, final T value) {
    values.put(name, value);
  }

  public List<String> fieldNames() {
    return new LinkedList<String>(values.keySet());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(final String name) {
    return values.containsKey(name) ? (T) values.get(name) : null;
  }

}