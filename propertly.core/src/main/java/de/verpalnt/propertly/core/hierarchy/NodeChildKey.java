package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;

/**
* @author j.boesl, 09.11.14
*/
public class NodeChildKey
{
  private final Class type;
  private final String name;

  public NodeChildKey(IPropertyDescription pPropertyDescription)
  {
    this(pPropertyDescription.getType(), pPropertyDescription.getName());
  }

  public NodeChildKey(Class pType, String pName)
  {
    type = pType;
    name = pName;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodeChildKey childKey = (NodeChildKey) o;
    return name.equals(childKey.name) && type.equals(childKey.type);
  }

  @Override
  public int hashCode()
  {
    int result = type.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }
}
