package de.mathit.repository.support;

import java.util.ArrayList;
import java.util.List;

import de.mathit.repository.Query;
import de.mathit.repository.Repository;

public class NestedRepository<T> implements Repository<T> {

  private Repository<T> nestedRepository;

  public NestedRepository(Repository<T> nestedRepository) {
    if (nestedRepository == null) {
      throw new IllegalArgumentException("Given repository is null.");
    }
    this.nestedRepository = nestedRepository;
  }

  @Override
  public final List<T> query(Query query) {
    return postProcessEntities(postProcess(nestedRepository
        .query(preProcess(query))));
  }

  protected Query preProcess(final Query query) {
    return query;
  }

  protected List<T> postProcess(List<T> list) {
    return list;
  }

  private List<T> postProcessEntities(List<T> list) {
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