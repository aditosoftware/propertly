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

  @Nullable
  @Override
  public String getName()
  {
    List<String> elements = getInternalElements();
    return elements.isEmpty() ? null : elements.get(elements.size() - 1);
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

  @Override
  public int compareTo(IPropertyPath o)
  {
    List<String> elements2;
    if (o instanceof PropertyPath)
      elements2 = ((PropertyPath) o).getInternalElements();
    else
      elements2 = o.getPathElements();

    int size1 = getInternalElements().size();
    int size2 = elements2.size();
    int maxSize = Math.max(size1, size2);
    for (int i = 0; i < maxSize; i++) {
      String s1 = i < size1 ? getInternalElements().get(i) : null;
      String s2 = i < size2 ? elements2.get(i) : null;
      if (s1 == null)
        return s2 == null ? 0 : -1;
      if (s2 == null)
        return 1;
      int result = s1.compareTo(s2);
      if (result != 0)
        return result;
    }
    return 0;
  }
}
