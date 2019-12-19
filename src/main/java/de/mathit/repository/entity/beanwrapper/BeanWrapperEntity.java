package de.mathit.repository.entity.beanwrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.mathit.repository.EntitySupport;

public class BeanWrapperEntity<T> extends EntitySupport {

  private final T bean;
  private final Map<String, Method> methods = new HashMap<String, Method>();
  private final Map<String, Object> overrideValues = new HashMap<String, Object>();

  public BeanWrapperEntity(final T bean) {
    super(bean.getClass().getName());
    this.bean = bean;
  }

  @Override
  public <T> T get(String name) {
    if (overrideValues.containsKey(name)) {
      return (T) overrideValues.get(name);
    }
    try {
      return (T) getMethod(name).invoke(bean);
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <T> void set(final String name, final T value) {
    overrideValues.put(name, value);
  }

  private Method getMethod(final String fieldName) {
    final String desiredMethodName = new StringBuilder().append("get")
        .append(fieldName.substring(0, 1).toUpperCase())
        .append(fieldName.substring(1)).toString();
    for (final Method method : bean.getClass().getMethods()) {
      final String methodName = method.getName();
      if (methodName.equals(desiredMethodName)) {
        if (method.getParameterTypes().length == 0) {
          return method;
        } else {
          throw new UnsupportedOperationException("Method " + method
              + " has parameters.");
        }
      }
    }
    throw new UnsupportedOperationException("Class "
        + bean.getClass().getName() + " has no method "
        + desiredMethodName);
  }

}