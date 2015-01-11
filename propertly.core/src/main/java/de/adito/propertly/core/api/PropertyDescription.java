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
  private Class<T> type;
  private String name;
  private List<? extends Annotation> annotations;


  private PropertyDescription(Class<S> pSourceType, Class<T> pType, String pName,
                              Iterable<? extends Annotation> pAnnotations)
  {
    sourceType = pSourceType;
    type = pType;
    name = pName;
    if (pAnnotations == null)
      annotations = Collections.emptyList();
    else
    {
      List<Annotation> annos;
      if (pAnnotations instanceof List)
        //noinspection unchecked
        annos = (List<Annotation>) pAnnotations;
      else
      {
        annos = new ArrayList<Annotation>();
        for (Annotation annotation : pAnnotations)
          annos.add(annotation);
      }
      annotations = Collections.unmodifiableList(annos);
    }
  }

  @Override
  public Class<S> getSourceType()
  {
    return sourceType;
  }

  @Override
  public Class<T> getType()
  {
    return type;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Nonnull
  public List<? extends Annotation> getAnnotations()
  {
    return annotations;
  }

  void setName(String name)
  {
    this.name = name;
  }

  @Nonnull
  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<T> pType, @Nonnull String pName,
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
                                         pPropertyDescription.getName(), pPropertyDescription.getAnnotations());
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
