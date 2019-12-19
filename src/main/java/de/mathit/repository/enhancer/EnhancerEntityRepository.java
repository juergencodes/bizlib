package de.mathit.repository.enhancer;

import java.util.LinkedList;
import java.util.List;

import de.mathit.repository.Entity;
import de.mathit.repository.EntityMetadata;
import de.mathit.repository.EntityRepository;
import de.mathit.repository.support.NestedRepository;

public class EnhancerEntityRepository extends NestedRepository<Entity>
    implements EntityRepository {

  // TODO create better solution, not breaking up the iterator
  final List<EntityMetadata> metadata;
  // TODO allow only one here, for multipe create chain of responsibility
  private EnhancerStrategy<Entity> enhancerStrategy;

  public EnhancerEntityRepository(final EntityRepository nestedRepository,
      final EnhancerStrategy<Entity> enhancerStrategy) {
    super(nestedRepository);
    this.enhancerStrategy = enhancerStrategy;
    this.metadata = new LinkedList<EntityMetadata>();
    for (final EntityMetadata m : nestedRepository.metadata()) {
      if (enhancerStrategy.canEnhance(m.name())) {
        this.metadata.add(new EnhancedEntityMetadata(m,
            enhancerStrategy.fieldName(), enhancerStrategy
            .fieldType()));
      } else {
        this.metadata.add(m);
      }
    }
  }

  @Override
  public Iterable<EntityMetadata> metadata() {
    return metadata;
  }

  @Override
  protected Entity postProcessEntity(Entity entity) {
    if (enhancerStrategy.canEnhance(entity.getName())) {
      final Object value = enhancerStrategy.enhance(entity);
      entity.set(enhancerStrategy.fieldName(), value);
    }
    return entity;
  }

}
