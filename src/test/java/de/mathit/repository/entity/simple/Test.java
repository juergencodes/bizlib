package de.mathit.repository.entity.simple;

import static de.mathit.repository.entity.simple.SimpleFieldMetadata.create;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.mathit.repository.Entity;
import de.mathit.repository.EntityMetadata;
import de.mathit.repository.EntityRepository;
import de.mathit.repository.FullQuery;
import de.mathit.repository.enhancer.EnhancerEntityRepository;
import de.mathit.repository.entity.beanwrapper.BeanEntityMetadataFactory;
import de.mathit.repository.entity.beanwrapper.BeanWrapperEntity;
import de.mathit.repository.impl.InMemoryRepository;
import de.mathit.repository.util.EntityRepositoryTemplate;

public class Test {

  public static void main(String[] args) {
    final EntityMetadata personMetadata = BeanEntityMetadataFactory
        .generateMetadata(Person.class);
    final EntityMetadata dogMetadata = SimpleEntityMetadataBuilder
        .create("Dog").field(create("name", String.class, false))
        .field(create("color", Color.class, false)).build();

    final EntityRepository repository = createRepository(
        Arrays.asList(personMetadata, dogMetadata),
        person("Hans", "Müller", createDate(7, 11, 1960)),
        person("Rüdiger", "Huber", createDate(3, 5, 1961)),
        dog("Bello", Color.BLACK));

    queryAndPrint(repository, personMetadata.name());
    queryAndPrint(repository, dogMetadata.name());

  }

  private static Date createDate(final int day, final int month,
      final int year) {
    final Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    return calendar.getTime();
  }

  private static void queryAndPrint(final EntityRepository repository,
      final String name) {
    final EntityRepositoryTemplate template = new EntityRepositoryTemplate(
        repository);

    System.out.println(name + "\n--------");
    final List<Entity> result = repository.query(new FullQuery() {
      @Override
      public String name() {
        return name;
      }
    });

    for (final Entity entity : result) {
      System.out.println(EntityPrettyPrinter.print(entity,
          template.metadata(entity.getName())));
    }
    System.out.println();
  }

  private static Entity person(final String firstName, final String lastName,
      final Date birthDate) {
    return new BeanWrapperEntity<Person>(new Person(firstName, lastName,
        birthDate));
  }

  private static Entity dog(final String name, final Color color) {
    final SimpleEntity entity = new SimpleEntity("Dog");
    entity.add("name", name);
    entity.add("color", color);
    return entity;
  }

  private static EntityRepository createRepository(
      final Iterable<EntityMetadata> metadata, final Entity... entities) {
    final InMemoryRepository repository = new InMemoryRepository(metadata);
    for (final Entity entity : entities) {
      repository.add(entity);
    }
    return new EnhancerEntityRepository(repository,
        new PersonEnhancerStrategy());
  }

}