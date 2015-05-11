package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.IPropertyPath;

import java.util.*;

/**
 * @author j.boesl, 11.05.15
 */
class PropertyPath implements IPropertyPath
{
  List<String> elements;

  public PropertyPath(INode pNode)
  {
    elements = new ArrayList<String>();
    while (pNode != null)
    {
      elements.add(0, pNode.getProperty().getName());
      pNode = pNode.getParent();
    }
    elements = Collections.unmodifiableList(elements);
  }

  @Override
  public List<String> getPathElements()
  {
    return elements;
  }

  @Override
  public String asString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (String element : elements)
    {
      if (stringBuilder.length() != 0)
        stringBuilder.append("/");
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
