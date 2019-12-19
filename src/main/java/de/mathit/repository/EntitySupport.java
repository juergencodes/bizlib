package de.mathit.repository;

/**
 * Support class which provides convenience functions based on a given instance of {@link
 * EntityMetadata}.
 */
public abstract class EntitySupport implements Entity {

  private final String name;

  public EntitySupport(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public <T> void set(String name, T value) {
    throw new UnsupportedOperationException();
  }

}