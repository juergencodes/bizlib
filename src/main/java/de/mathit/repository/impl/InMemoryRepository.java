package de.mathit.repository.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mathit.repository.Entity;
import de.mathit.repository.EntityMetadata;
import de.mathit.repository.EntityRepository;
import de.mathit.repository.FullQuery;
import de.mathit.repository.Query;

public class InMemoryRepository implements EntityRepository {

  private final Iterable<EntityMetadata> metadata;
  private final Map<String, List<Entity>> entities = new HashMap<String, List<Entity>>();

  public InMemoryRepository(Iterable<EntityMetadata> metadata) {
    super();
    this.metadata = metadata;
  }

  @Override
  public Iterable<EntityMetadata> metadata() {
    return metadata;
  }

  public void add(final Entity entity) {
    getList(entity.getName()).add(entity);
  }

  private List getList(String name) {
    if (!entities.containsKey(name)) {
      synchronized (this) {
        if (!entities.containsKey(name)) {
          entities.put(name, new LinkedList<Entity>());
        }
      }
    }
    return entities.get(name);
  }

  @Override
  public List<Entity> query(Query query) {
    if (query instanceof FullQuery) {
      final List list = getList(((FullQuery) query).name());
      if (list != null) {
        return list;
      }

    }
    return Collections.emptyList();
  }

}