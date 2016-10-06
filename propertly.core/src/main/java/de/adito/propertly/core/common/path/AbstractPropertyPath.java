package de.adito.propertly.core.common.path;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 06.10.16
 */
public abstract class AbstractPropertyPath implements IPropertyPath
{
  protected static final String DELIM = "/";


  protected abstract List<String> getInternalElements();

  @Nullable
  public IProperty<?, ?> find(IHierarchy<?> pHierarchy)
  {
    IProperty<?, ?> property = null;
    for (String element : getInternalElements()) {
      if (property == null) {
        property = pHierarchy.getProperty();
        if (!Objects.equals(property.getName(), element))
          return null;
      }
      else {
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
    List<String> elements = getInternalElements();
    if (elements.isEmpty())
      throw new NoParentPathForRootException();
    return new PropertyPath(elements.subList(0, elements.size() - 1));
  }

  @Override
  public boolean isParentOf(IPropertyPath pPath)
  {
    List<String> elements = getInternalElements();
    List<String> otherElements = pPath.getPathElements();
    if (elements.size() <= otherElements.size())
      return false;
    for (int i = 0; i < elements.size(); i++)
      if (!elements.get(i).equals(otherElements.get(i)))
        return false;
    return true;
  }

  @Nonnull
  @Override
  public IPropertyPath getChild(String pName)
  {
    List<String> list = new ArrayList<>(getInternalElements());
    list.add(pName);
    return new PropertyPath(list);
  }

  @Nonnull
  @Override
  public List<String> getPathElements()
  {
    return new ArrayList<>(getInternalElements());
  }

  @Nonnull
  @Override
  public String asString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (String element : getInternalElements()) {
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
        !(o == null || !IPropertyPath.class.isAssignableFrom(o.getClass())) && Objects.equals(getInternalElements(), ((IPropertyPath) o).getPathElements());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getInternalElements());
  }
}
