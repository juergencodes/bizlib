package de.mathit.repository.entity.simple;

import de.mathit.repository.FieldMetadata;

import java.util.LinkedList;
import java.util.List;

public class SimpleEntityMetadataBuilder {

  private String name;
  private final List<FieldMetadata> fields = new LinkedList<FieldMetadata>();

  private SimpleEntityMetadataBuilder() {
  }

  public static SimpleEntityMetadataBuilder create(final String name) {
    final SimpleEntityMetadataBuilder builder = new SimpleEntityMetadataBuilder();
    builder.name = name;
    return builder;
  }

  public SimpleEntityMetadataBuilder name(final String name) {
    this.name = name;
    return this;
  }

  public SimpleEntityMetadataBuilder field(final FieldMetadata field) {
    fields.add(field);
    return this;
  }

  public SimpleEntityMetadata build() {
    return new SimpleEntityMetadata(name, fields);
  }

}