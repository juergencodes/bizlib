package de.mathit.repository;

public interface EntityRepository extends Repository<Entity> {

  Iterable<EntityMetadata> metadata();

}