package de.mathit.repository.entity.beanwrapper;

import de.mathit.repository.EntitySupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanWrapperEntity<T> extends EntitySupport {

  private final T bean;
  private final Map<String, Object> overrideValues = new HashMap<>();

  public BeanWrapperEntity(final T bean) {
    super(bean.getClass().getName());
    this.bean = bean;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <V> V get(final String name) {
    if (overrideValues.containsKey(name)) {
      return (V) overrideValues.get(name);
    }
    try {
      return (V) getMethod(name).invoke(bean);
    } catch (final IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <V> void set(final String name, final V value) {
    overrideValues.put(name, value);
  }

  private Method getMethod(final String fieldName) {
    final String desiredMethodName =
        "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    for (final Method method : bean.getClass().getMethods()) {
      final String methodName = method.getName();
      if (methodName.equals(desiredMethodName)) {
        if (method.getParameterTypes().length == 0) {
          return method;
        } else {
          throw new UnsupportedOperationException("Method " + method + " has parameters.");
        }
      }
    }
    throw new UnsupportedOperationException(
        "Class " + bean.getClass().getName() + " has no method " + desiredMethodName);
  }

}