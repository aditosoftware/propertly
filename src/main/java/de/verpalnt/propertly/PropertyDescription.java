package de.verpalnt.propertly;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author PaL
 *         Date: 30.09.11
 *         Time: 23:49
 */
public class PropertyDescription<S, T> implements IPropertyDescription<S, T>
{

  private Class<S> parentType;
  private Class<T> type;
  private String name;
  private List<? extends Annotation> annotations;


  private PropertyDescription(Class<S> pParentType, Class<T> pType, String pName,
                              Iterable<? extends Annotation> pAnnotations)
  {
    parentType = pParentType;
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
  public Class<S> getParentType()
  {
    return parentType;
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

  public void setParentType(Class<S> parentType)
  {
    this.parentType = parentType;
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

  public static <S, T> IPropertyDescription<S, T> create(Class<S> pParentType, Class<T> pType, String pName,
                                                         Iterable<? extends Annotation> pAnnotations)
  {
    return new PropertyDescription<S, T>(pParentType, pType, pName, pAnnotations);
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
