package de.adito.propertly.core.common;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.hierarchy.PropertyDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 19:23
 */
public class PD
{
  private static final Map<Class, List<Field>> FIELD_CACHE = new LinkedHashMap<Class, List<Field>>();

  private PD()
  {
  }


  public static <S extends IPropertyPitProvider<?, ? super S, ? super T>, T> IPropertyDescription<S, T> create(Class<S> pSource)
  {
    List<Field> fields = FIELD_CACHE.get(pSource);
    if (fields == null)
    {
      fields = new ArrayList<Field>(Arrays.asList(pSource.getDeclaredFields()));
      FIELD_CACHE.put(pSource, fields);
    }
    Iterator<Field> iterator = fields.iterator();
    while (iterator.hasNext())
    {
      Field field = iterator.next();
      try
      {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))
        {
          if (!Modifier.isPublic(modifiers))
            field.setAccessible(true);
          if (field.get(null) == null) // not yet initialized
          {
            if (IPropertyDescription.class.isAssignableFrom(field.getType()))
            {
              iterator.remove();
              Class type = Object.class;
              Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
              if (types.length == 2)
              {
                if (!((Class<?>) types[0]).isAssignableFrom(pSource))
                  throw new RuntimeException("invalid type: " + types[0]);
                if (types[1] instanceof Class)
                  type = ((Class) types[1]);
                else if (types[1] instanceof ParameterizedType)
                  type = (Class) ((ParameterizedType) types[1]).getRawType();
              }
              String name = field.getName();
              List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
              //noinspection unchecked
              return (IPropertyDescription<S, T>) PropertyDescription.create(pSource, type, name, annotations);
            }
          }
        }
      }
      catch (IllegalAccessException e)
      {
        throw new RuntimeException(e);
      }
      iterator.remove();
    }

    throw new RuntimeException("couldn't find field at " + pSource);
  }
}
