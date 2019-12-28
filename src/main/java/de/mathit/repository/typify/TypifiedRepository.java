package de.mathit.repository.typify;

import de.mathit.repository.Entity;
import de.mathit.repository.EntityRepository;
import de.mathit.repository.Query;
import de.mathit.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public abstract class TypifiedRepository<T> implements Repository<T> {

  private EntityRepository entityRepository;
  private Class<T> clazz;

  public TypifiedRepository(final EntityRepository entityRepository, final Class<T> clazz) {
    this.entityRepository = entityRepository;
    this.clazz = clazz;
  }

  @Override
  public List<T> query(final Query query) {
    final List<Entity> entities = entityRepository.query(query);
    if (entities == null) {
      return null;
    }
    final List<T> result = new ArrayList<T>(entities.size());
    for (final Entity entity : entities) {
      result.add(convert(entity));
    }
    return result;
  }

  protected abstract T convert(Entity entity);

}