package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.*;

/**
 * @author j.boesl, 21.07.15
 */
public class DeltaUpdateValue extends Delta
{
  private IPropertyPath path;
  private Object value;

  public DeltaUpdateValue(IPropertyPath pPath, Object pValue)
  {
    path = pPath;
    value = pValue;
  }

  @Override
  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IProperty<?, ?> property = path.find(pTo);
    try
    {
      if (value instanceof Class && IPropertyPitProvider.class.isAssignableFrom((Class) value))
        ((IProperty) property).setValue(((Class) value).newInstance());
      else
        ((IProperty) property).setValue(value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
