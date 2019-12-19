package de.mathit.repository.entity.simple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mathit.repository.EntitySupport;

public class SimpleEntity extends EntitySupport {

  private final Map<String, Object> values = new HashMap<String, Object>();

  SimpleEntity(final String name) {
    super(name);
  }

  <T> void add(final String name, final T value) {
    values.put(name, value);
  }

  // @Override
  public List<String> fieldNames() {
    return new LinkedList<String>(values.keySet());
  }

  @Override
  public <T> T get(String name) {
    return values.containsKey(name) ? (T) values.get(name) : null;
  }

}