package de.mathit.repository;

public interface FieldMetadata {

  String name();

  Class<?> type();

  boolean writable();

}