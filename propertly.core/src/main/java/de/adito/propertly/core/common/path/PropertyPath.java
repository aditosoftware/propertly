package de.adito.propertly.core.common.path;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 11.05.15
 */
public class PropertyPath implements IPropertyPath
{
  private static final String DELIM = "/";


  private List<String> elements;

  public PropertyPath(IProperty<?, ?> pProperty)
  {
    elements = new ArrayList<>();
    while (pProperty != null)
    {
      elements.add(0, pProperty.getName());
      IPropertyPitProvider parent = pProperty.getParent();
      pProperty = parent == null ? null : parent.getPit().getOwnProperty();
    }
  }

  public PropertyPath(@Nonnull String pPath)
  {
    elements = Arrays.asList(pPath.split(DELIM));
  }

  public PropertyPath(@Nonnull Iterable<String> pPathElements)
  {
    elements = new ArrayList<>();
    for (String pathElement : pPathElements)
      elements.add(pathElement);
  }

  @Nullable
  public IProperty<?, ?> find(IHierarchy<?> pHierarchy)
  {
    IProperty<?, ?> property = null;
    for (String element : elements)
    {
      if (property == null)
      {
        property = pHierarchy.getProperty();
        if (!Objects.equals(property.getName(), element))
          return null;
      }
      else
      {
        Object value = property.getValue();
        if (value instanceof IPropertyPitProvider)
          property = ((IPropertyPitProvider) value).getPit().findProperty(element);
        else
          return null;
      }
      if (property == null)
        return null;
    }
    return property;
  }

  @Nonnull
  @Override
  public IPropertyPath getParent() throws NoParentPathForRootException
  {
    if (elements.isEmpty())
      throw new NoParentPathForRootException();
    return new PropertyPath(elements.subList(0, elements.size()-1));
  }

  @Nonnull
  @Override
  public IPropertyPath getChild(String pName)
  {
    List<String> list = new ArrayList<>(elements);
    list.add(pName);
    return new PropertyPath(list);
  }

  @Nonnull
  @Override
  public List<String> getPathElements()
  {
    return new ArrayList<>(elements);
  }

  @Nonnull
  @Override
  public String asString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (String element : elements)
    {
      if (stringBuilder.length() != 0)
        stringBuilder.append(DELIM);
      stringBuilder.append(element);
    }
    return stringBuilder.toString();
  }

  @Override
  public String toString()
  {
    return PropertlyUtility.asString(this, asString());
  }

  @Override
  public boolean equals(Object o)
  {
    return this == o ||
        !(o == null || getClass() != o.getClass()) && Objects.equals(elements, ((PropertyPath) o).elements);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(elements);
  }
}
