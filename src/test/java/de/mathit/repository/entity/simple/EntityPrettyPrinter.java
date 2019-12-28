package de.mathit.repository.entity.simple;

import de.mathit.repository.Entity;
import de.mathit.repository.EntityMetadata;
import de.mathit.repository.FieldMetadata;

public class EntityPrettyPrinter {

  public static String print(final Entity entity, final EntityMetadata entityMetadata) {
    final StringBuilder builder = new StringBuilder();
    builder.append(entityMetadata.name()).append("[");
    String delimiter = "";
    for (final FieldMetadata field : entityMetadata.fields()) {
      final String name = field.name();
      final Class<?> type = field.type();
      final Object value = entity.get(name);
      builder.append(delimiter);
      delimiter = ", ";
      builder.append(name).append("(").append(type.getSimpleName()).append("):").append(value);
    }
    builder.append("]");
    return builder.toString();
  }

}