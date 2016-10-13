package de.adito.propertly.diff.a_old;

import de.adito.propertly.core.common.path.IPropertyPath;

import java.util.Objects;

/**
 * @author j.boesl, 10.07.15
 */
class PropertlyDiffElement
{

  private IPropertyPath path;
  private EType type;
  private Object value;


  public PropertlyDiffElement(IPropertyPath pPath, EType pType, Object pValue)
  {
    path = pPath;
    type = pType;
    value = pValue;
  }

  public IPropertyPath getPath()
  {
    return path;
  }

  public EType getType()
  {
    return type;
  }

  public Object getValue()
  {
    return value;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PropertlyDiffElement that = (PropertlyDiffElement) o;
    return Objects.equals(type, that.type) &&
        Objects.equals(value, that.value);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, value);
  }

  enum EType
  {
    DESCRIPTION,
    VALUE,
    NODE,
    NODE_END
  }
}
