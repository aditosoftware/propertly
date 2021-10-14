package de.adito.propertly.core.common;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.exception.InitializationException;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

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
 * @author J.Boesl, 13.11.12
 */
public class PD
{
  private static final Logger _LOGGER = Logger.getLogger(PD.class.getName());
  private static final Map<Class<?>, List<Field>> FIELD_CACHE = new LinkedHashMap<>();

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
    try
    {
      return _create(pSource, null);
    }
    catch(Throwable t)
    {
      _LOGGER.log(Level.WARNING, "", t);
      throw t;
    }
  }

  /**
   * Creates a IPropertyDescriptionDV object at a IPropertyPitProvider.
   *
   * @param pSource       the corresponding IPropertyPitProvider class that the created IPropertyDescription object will be a
   *                      static member of.
   * @param pDefaultValue if a default value is supplied a {@link PropertyDescriptionDV} is created.
   * @param <S>           the type of the corresponding IPropertyPitProvider.
   * @param <T>           the value's type this IPropertyDescription object describes.
   * @return the fitting IPropertyDescriptionDV object for the given IPropertyPitProvider.
   */
  @NotNull
  public static <S extends IPropertyPitProvider<?, ?, ?>, T> IPropertyDescriptionDV<S, T>
  create(@NotNull Class<S> pSource, @NotNull T pDefaultValue)
  {
    try
    {
      return (IPropertyDescriptionDV<S, T>) _create(pSource, pDefaultValue);
    }
    catch(Throwable t)
    {
      _LOGGER.log(Level.WARNING, "", t);
      throw t;
    }
  }

  @NotNull
  private static <S extends IPropertyPitProvider<?, ?, ?>, T> IPropertyDescription<S, T>
  _create(@NotNull Class<S> pSource, @Nullable T pDefaultValue)
  {
    List<Field> fields = FIELD_CACHE.computeIfAbsent(pSource, source -> new ArrayList<>(Arrays.asList(source.getDeclaredFields())));
    boolean isPublicClass = Modifier.isPublic(pSource.getModifiers());

    Iterator<Field> iterator = fields.iterator();
    while (iterator.hasNext())
    {
      Field field = iterator.next();
      try
      {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))
        {
          if (!Modifier.isPublic(modifiers) || !isPublicClass)
            field.setAccessible(true);
          if (field.get(null) == null) // not yet initialized
          {
            if (IPropertyDescription.class.isAssignableFrom(field.getType()))
            {
              iterator.remove();
              Class<?> type = Object.class;
              Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
              if (types.length == 2)
              {
                if (!((Class<?>) types[0]).isAssignableFrom(pSource))
                  throw new RuntimeException("invalid type: " + types[0]);
                if (types[1] instanceof Class)
                  type = ((Class<?>) types[1]);
                else if (types[1] instanceof ParameterizedType)
                  type = (Class<?>) ((ParameterizedType) types[1]).getRawType();
              }
              String name = field.getName();
              Annotation[] annotations = field.getDeclaredAnnotations();
              if (pDefaultValue == null)
                //noinspection unchecked
                return new PropertyDescription<>(pSource, (Class<? extends T>) type, name, annotations);
              //noinspection unchecked
              return new PropertyDescriptionDV<>(pSource, (Class<? extends T>) type, name, pDefaultValue, annotations);
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

    throw new InitializationException("couldn't find field at " + pSource);
  }
}
