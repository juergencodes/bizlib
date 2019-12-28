package de.mathit.repository.support;

import de.mathit.repository.Query;
import de.mathit.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class NestedRepository<T> implements Repository<T> {

  private Repository<T> nestedRepository;

  public NestedRepository(final Repository<T> nestedRepository) {
    if (nestedRepository == null) {
      throw new IllegalArgumentException("Given repository is null.");
    }
    this.nestedRepository = nestedRepository;
  }

  @Override
  public final List<T> query(final Query query) {
    return postProcessEntities(postProcess(nestedRepository.query(preProcess(query))));
  }

  protected Query preProcess(final Query query) {
    return query;
  }

  protected List<T> postProcess(final List<T> list) {
    return list;
  }

  private List<T> postProcessEntities(final List<T> list) {
    if (list == null) {
      return list;
    }
    final List<T> result = new ArrayList<T>(list.size());
    for (final T entry : list) {
      result.add(postProcessEntity(entry));
    }
    return result;
  }

  protected T postProcessEntity(final T entity) {
    return entity;
  }

}