package de.mathit.repository.enhancer;

import de.mathit.repository.EntityMetadata;
import de.mathit.repository.FieldMetadata;
import de.mathit.repository.entity.simple.SimpleFieldMetadata;

import java.util.LinkedList;
import java.util.List;

public class EnhancedEntityMetadata implements EntityMetadata {

  private final String name;
  private final List<FieldMetadata> fields = new LinkedList<>();

  public EnhancedEntityMetadata(final EntityMetadata metadata, final String fieldName,
      final Class<?> fieldType) {
    this.name = metadata.name();
    for (final FieldMetadata fieldMetadata : metadata.fields()) {
      fields.add(fieldMetadata);
    }
    fields.add(SimpleFieldMetadata.create(fieldName, fieldType, false));

  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Iterable<FieldMetadata> fields() {
    return fields;
  }

}
