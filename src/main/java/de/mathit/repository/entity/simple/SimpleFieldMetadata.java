package de.mathit.repository.entity.simple;

import de.mathit.repository.FieldMetadata;

/**
 * Java bean implementation of {@link FieldMetadata}.
 */
public class SimpleFieldMetadata implements FieldMetadata {

  private final String name;
  private final Class<?> type;
  private final boolean writable;

  protected SimpleFieldMetadata(final String name, final Class<?> type,
      final boolean writable) {
    super();
    this.name = name;
    this.type = type;
    this.writable = writable;
  }

  public static SimpleFieldMetadata create(final String name,
      final Class<?> type, final boolean writable) {
    return new SimpleFieldMetadata(name, type, writable);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> type() {
    return type;
  }

  @Override
  public boolean writable() {
    return writable;
  }

}