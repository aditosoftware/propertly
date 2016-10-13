package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.*;

/**
 * @author j.boesl, 21.07.15
 */
public class DeltaInsert extends Delta
{
  private IPropertyPath parentPath;
  private IPropertyDescription description;
  private Object value;
  private int index;

  public DeltaInsert(IPropertyPath pParentPath, IPropertyDescription pDescription, Object pValue, int pIndex)
  {
    parentPath = pParentPath;
    description = pDescription;
    value = pValue;
    index = pIndex;
  }

  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IProperty<?, ?> parentProperty = parentPath.find(pTo);
    IPropertyPitProvider ppp = (IPropertyPitProvider) parentProperty.getValue();
    IProperty targetProperty = ppp.getPit().findProperty(description);
    if (targetProperty == null)
    {
      if (index >= 0 && ppp instanceof IIndexedMutablePropertyPitProvider)
        targetProperty = ((IIndexedMutablePropertyPit) ppp.getPit()).addProperty(index, description);
      else
        targetProperty = ((IMutablePropertyPit) ppp.getPit()).addProperty(description);
    }

    try
    {
      if (value instanceof Class && IPropertyPitProvider.class.isAssignableFrom((Class) value))
        targetProperty.setValue(((Class) value).newInstance());
      else
        targetProperty.setValue(value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
