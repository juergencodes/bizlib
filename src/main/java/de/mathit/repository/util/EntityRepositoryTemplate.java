package de.mathit.repository.util;

import de.mathit.repository.EntityMetadata;
import de.mathit.repository.EntityRepository;

public class EntityRepositoryTemplate {

  private final EntityRepository entityRepository;

  public EntityRepositoryTemplate(final EntityRepository entityRepository) {
    super();
    if (entityRepository == null) {
      throw new IllegalArgumentException(
          "Given " + EntityRepository.class.getSimpleName() + " is null.");
    }
    this.entityRepository = entityRepository;
  }

  public EntityMetadata metadata(final String name) {
    for (final EntityMetadata metadata : entityRepository.metadata()) {
      if (metadata.name().equals(name)) {
        return metadata;
      }
    }
    throw new IllegalStateException("There is no metadata for entity name '" + name + "'.");
  }

}