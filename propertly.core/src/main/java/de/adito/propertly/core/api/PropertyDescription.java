package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PaL
 *         Date: 30.09.11
 *         Time: 23:49
 */
public class PropertyDescription<S extends IPropertyPitProvider, T> implements IPropertyDescription<S, T>
{

  private Class<S> sourceType;
  private Class<? extends T> type;
  private String name;
  private Map<Class<? extends Annotation>, Annotation> annotations;


  private PropertyDescription(Class<S> pSourceType, Class<? extends T> pType, String pName,
                              Iterable<? extends Annotation> pAnnotations)
  {
    sourceType = pSourceType;
    type = pType;
    name = pName;
    if (pAnnotations == null)
      annotations = Collections.emptyMap();
    else
    {
      annotations = new HashMap<Class<? extends Annotation>, Annotation>();
      for (Annotation annotation : pAnnotations)
        annotations.put(annotation.annotationType(), annotation);
    }
  }

  @Override
  public Class<S> getSourceType()
  {
    return sourceType;
  }

  @Override
  public Class<? extends T> getType()
  {
    return type;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> pAnnotationClass)
  {
    return annotations.containsKey(pAnnotationClass);
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> pAnnotationClass)
  {
    //noinspection unchecked
    return (A) annotations.get(pAnnotationClass);
  }

  @Override
  public Annotation[] getAnnotations()
  {
    Annotation[] arr = new Annotation[annotations.size()];
    int i = 0;
    for (Annotation annotation : annotations.values())
      arr[i++] = annotation;
    return arr;
  }

  @Override
  public Annotation[] getDeclaredAnnotations()
  {
    return getAnnotations();
  }

  void setName(String name)
  {
    this.name = name;
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType, @Nonnull String pName,
      @Nullable Annotation... pAnnotations)
  {
    return new PropertyDescription<S, T>(pSourceType, pType, pName,
        pAnnotations == null ? Collections.<Annotation>emptyList() : Arrays.asList(pAnnotations));
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType, @Nonnull String pName,
      @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return new PropertyDescription<S, T>(pSourceType, pType, pName, pAnnotations);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<T> pType, @Nonnull String pName)
  {
    return new PropertyDescription<S, T>(pSourceType, pType, pName, null);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull IPropertyDescription<S, T> pPropertyDescription)
  {
    return new PropertyDescription<S, T>(pPropertyDescription.getSourceType(), pPropertyDescription.getType(),
        pPropertyDescription.getName(), Arrays.asList(pPropertyDescription.getAnnotations()));
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PropertyDescription that = (PropertyDescription) o;
    return annotations.equals(that.annotations) &&
        name.equals(that.name) &&
        sourceType.equals(that.sourceType) &&
        type.equals(that.type);
  }

  @Override
  public int hashCode()
  {
    int result = sourceType.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + annotations.hashCode();
    return result;
  }

  @Override
  public String toString()
  {
    return "PropertyDescription{" +
        "type='" + (type == null ? null : type.getSimpleName()) +
        "', name='" + name + '\'' +
        '}';
  }


}
