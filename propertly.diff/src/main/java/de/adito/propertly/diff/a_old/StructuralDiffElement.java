package de.adito.propertly.diff.a_old;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.IPropertyDescription;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author j.boesl on 12.07.15.
 */
public class StructuralDiffElement implements Comparable<StructuralDiffElement>
{

  private IPropertyPath parentPath;
  private IPropertyDescription<?, ?> parental;
  private IPropertyDescription<?, ?> self;
  private int position;

  public StructuralDiffElement(IPropertyPath pParentPath, IPropertyDescription pParental, IPropertyDescription pSelf, int pPosition)
  {
    parentPath = pParentPath;
    parental = pParental;
    self = pSelf;
    position = pPosition;
  }

  public IPropertyPath getParentPath()
  {
    return parentPath;
  }

  public IPropertyDescription getParental()
  {
    return parental;
  }

  public IPropertyDescription getSelf()
  {
    return self;
  }

  public int getPosition()
  {
    return position;
  }

  @Override
  public int compareTo(@Nonnull StructuralDiffElement o)
  {
    int result;
    if (parental == null)
    {
      if (o.parental == null)
        result = 0;
      else
        return -1;
    }
    else if (o.parental == null)
      return 1;
    else
      result = parental.compareTo(o.parental);

    if (result == 0)
      result = parentPath.compareTo(o.parentPath);
    return result == 0 ? getPosition() - o.getPosition() : result;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StructuralDiffElement that = (StructuralDiffElement) o;
    return Objects.equals(parental, that.parental) &&
        Objects.equals(self, that.self);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(parental, self);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + getParental() + ", " + getSelf() + ", " + getPosition() + "}";
  }
}
