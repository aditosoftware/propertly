package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.*;

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
      annotations = new HashMap<>();
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

  void setName(String pName)
  {
    name = pName;
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType, @Nonnull String pName,
      @Nullable Annotation... pAnnotations)
  {
    return new PropertyDescription<>(pSourceType, pType, pName,
                                     pAnnotations == null ? Collections.emptyList() : Arrays.asList(pAnnotations));
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType, @Nonnull String pName,
      @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return new PropertyDescription<>(pSourceType, pType, pName, pAnnotations);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<T> pType, @Nonnull String pName)
  {
    return new PropertyDescription<>(pSourceType, pType, pName, null);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull IPropertyDescription<S, T> pPropertyDescription)
  {
    return new PropertyDescription<>(pPropertyDescription.getSourceType(), pPropertyDescription.getType(),
                                     pPropertyDescription.getName(), Arrays.asList(pPropertyDescription.getAnnotations()));
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO)
      return true;
    if (pO == null || getClass() != pO.getClass())
      return false;
    PropertyDescription<?, ?> that = (PropertyDescription<?, ?>) pO;
    return Objects.equals(sourceType, that.sourceType) &&
        Objects.equals(type, that.type) &&
        Objects.equals(name, that.name) &&
        Objects.equals(annotations, that.annotations);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(sourceType, type, name, annotations);
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
