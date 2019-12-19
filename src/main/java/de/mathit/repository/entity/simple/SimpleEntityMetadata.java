package de.mathit.repository.entity.simple;

import de.mathit.repository.EntityMetadata;
import de.mathit.repository.FieldMetadata;

/**
 * Java bean implementation of {@link EntityMetadata}.
 */
public class SimpleEntityMetadata implements EntityMetadata {

  private final String name;
  private final Iterable<FieldMetadata> fieldIterable;

  public SimpleEntityMetadata(String name, Iterable<FieldMetadata> fields) {
    super();
    this.name = name;
    this.fieldIterable = fields;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Iterable<FieldMetadata> fields() {
    return fieldIterable;
  }

}