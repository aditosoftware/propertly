package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.*;

/**
 * @author j.boesl, 21.07.15
 */
public class DeltaDelete extends Delta
{
  private IPropertyPath path;
  private boolean isProperty;

  public DeltaDelete(IPropertyPath pPath, boolean pIsProperty)
  {
    path = pPath;
    isProperty = pIsProperty;
  }

  @Override
  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IProperty<?, ?> property = path.find(pTo);
    IPropertyPitProvider parent = property.getParent();
    if (isProperty && parent instanceof IMutablePropertyPitProvider)
      ((IMutablePropertyPitProvider) parent).getPit().removeProperty(property);
    else
      property.setValue(null);
  }

}
