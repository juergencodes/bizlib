package de.mathit.repository;

public interface EntityMetadata {

  String name();

  Iterable<FieldMetadata> fields();

}