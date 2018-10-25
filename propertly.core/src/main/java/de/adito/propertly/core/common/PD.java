package de.adito.propertly.core.common;

import de.adito.propertly.core.api.PropertyDescription;
import de.adito.propertly.core.common.exception.InitializationException;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * PD is used to create IPropertyDescription objects on IPropertyPitProvider objects.<br/>
 * It is provided to ease the creation of IPropertyDescription objects and for readability.<br/>
 * With PD only static IPropertyDescription objects shall be created. For dynamic descriptions see {@link PropertyDescription}.<br/>
 * <br/>
 * Example of use at a IPropertyPitProvider called <tt>MyPitProvider</tt>:
 * <p>
 * public static final IPropertyDescription&lt;MyPitProvider, Integer> X = PD.create(MyPitProvider.class);
 * </p>
 * The created IPropertyDescription for <tt>X</tt> is of type <tt>Integer</tt> with name <tt>X</tt> and source <tt>MyPitProvider</tt>.
 *
 * @author PaL
 * Date: 13.11.12
 * Time: 19:23
 */
public class PD
{
  private static final Map<Class, List<Field>> FIELD_CACHE = new LinkedHashMap<>();

  private PD()
  {
  }

  /**
   * Creates a IPropertyDescription object at a IPropertyPitProvider.
   *
   * @param pSource the corresponding IPropertyPitProvider class that the created IPropertyDescription object will be a
   *                static member of.
   * @param <S>     the type of the corresponding IPropertyPitProvider.
   * @param <T>     the value's type this IPropertyDescription object describes.
   * @return the fitting IPropertyDescription object for the given IPropertyPitProvider.
   */
  @NotNull
  public static <S extends IPropertyPitProvider<?, ?, ?>, T> IPropertyDescription<S, T>
  create(@NotNull Class<S> pSource)
  {
    List<Field> fields = FIELD_CACHE.get(pSource);
    if (fields == null) {
      fields = new ArrayList<>(Arrays.asList(pSource.getDeclaredFields()));
      FIELD_CACHE.put(pSource, fields);
    }
    boolean isPublicClass = Modifier.isPublic(pSource.getModifiers());

    Iterator<Field> iterator = fields.iterator();
    while (iterator.hasNext()) {
      Field field = iterator.next();
      try {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
          if (!Modifier.isPublic(modifiers) || !isPublicClass)
            field.setAccessible(true);
          if (field.get(null) == null) // not yet initialized
          {
            if (IPropertyDescription.class.isAssignableFrom(field.getType())) {
              iterator.remove();
              Class type = Object.class;
              Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
              if (types.length == 2) {
                if (!((Class<?>) types[0]).isAssignableFrom(pSource))
                  throw new RuntimeException("invalid type: " + types[0]);
                if (types[1] instanceof Class)
                  type = ((Class) types[1]);
                else if (types[1] instanceof ParameterizedType)
                  type = (Class) ((ParameterizedType) types[1]).getRawType();
              }
              String name = field.getName();
              Annotation[] annotations = field.getDeclaredAnnotations();
              //noinspection unchecked
              return (IPropertyDescription<S, T>) new PropertyDescription(pSource, type, name, annotations);
            }
          }
        }
      }
      catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      iterator.remove();
    }

    throw new InitializationException("couldn't find field at " + pSource);
  }
}
