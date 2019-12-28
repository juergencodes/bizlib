package de.mathit.repository.entity.simple;

import de.mathit.repository.Entity;
import de.mathit.repository.enhancer.EnhancerStrategy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

final class PersonEnhancerStrategy implements EnhancerStrategy<Entity> {

  @Override
  public boolean canEnhance(final String name) {
    return name.equals(Person.class.getName());
  }

  @Override
  public String fieldName() {
    return "age";
  }

  @Override
  public Class<?> fieldType() {
    return Integer.class;
  }

  @Override
  public Object enhance(final Entity entity) {
    final Date birthDate = entity.get("birthDate");
    final LocalDate date = Instant.ofEpochMilli(birthDate.getTime()).atZone(ZoneId.systemDefault())
        .toLocalDate();
    return Period.between(date, LocalDate.now());
  }

}