package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

  public List<? extends Annotation> getAnnotations()
  {
    return annotations;
  }

  public void setSourceType(Class<S> pSourceType)
  {
    this.sourceType = pSourceType;
  }

  void setType(Class<T> type)
  {
    this.type = type;
  }

  void setName(String name)
  {
    this.name = name;
  }

  void setAnnotations(List<? extends Annotation> annotations)
  {
    this.annotations = annotations;
  }

  public static <S extends IPropertyPitProvider, T> IPropertyDescription<S, T> create(
      @Nonnull Class<S> pSourceType, @Nonnull Class<T> pType, @Nonnull String pName,
      @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return new PropertyDescription<S, T>(pSourceType, pType, pName, pAnnotations);
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