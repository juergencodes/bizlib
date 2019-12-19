package de.mathit.repository.enhancer;

public interface EnhancerStrategy<T> {

  boolean canEnhance(String name);

  String fieldName();

  Class<?> fieldType();

  Object enhance(T entity);

}