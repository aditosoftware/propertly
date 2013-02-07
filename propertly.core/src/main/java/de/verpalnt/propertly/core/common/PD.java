package de.verpalnt.propertly.core.common;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.hierarchy.PropertyDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 19:23
 */
public class PD
{
  private static final Map<Class, List<Field>> FIELD_CACHE = new HashMap<Class, List<Field>>();

  private PD()
  {
  }


  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(Class<S> pSource)
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
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.get(null) == null)
        {
          if (IPropertyDescription.class.isAssignableFrom(field.getType()))
          {
            iterator.remove();
            Class type = Object.class;
            Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            if (types.length == 2)
            {
              if (!((Class<?>) types[0]).isAssignableFrom(pSource))
                return null;
              type = ((Class) types[1]);
            }
            String name = field.getName();
            List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
            //noinspection unchecked
            return (IPropertyDescription<S, T>) PropertyDescription.create(pSource, type, name, annotations);
          }
        }
      }
      catch (IllegalAccessException e)
      {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      iterator.remove();
    }

    return null;
  }
}
