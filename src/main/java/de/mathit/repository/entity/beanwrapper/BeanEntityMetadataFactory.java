package de.mathit.repository.entity.beanwrapper;

import de.mathit.repository.EntityMetadata;
import de.mathit.repository.FieldMetadata;
import de.mathit.repository.entity.simple.SimpleEntityMetadata;
import de.mathit.repository.entity.simple.SimpleFieldMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class BeanEntityMetadataFactory {

  public static EntityMetadata generateMetadata(final Class<?> clazz) {
    final List<FieldMetadata> fields = new LinkedList<>();
    for (final Method method : clazz.getMethods()) {
      final String methodName = method.getName();
      if (!methodName.equals("getClass") && methodName.startsWith("get") && !Modifier
          .isStatic(method.getModifiers()) && method.getParameterTypes().length == 0) {
        final String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        final boolean writable = false; // TODO determine based on if there is
        // a setter
        // TODO Any good idea to supply this reflection analysis to
        // BeanWrapperEntity?
        fields.add(SimpleFieldMetadata.create(fieldName, method.getReturnType(), writable));
      }
    }

    return new SimpleEntityMetadata(clazz.getName(), fields);
  }

}