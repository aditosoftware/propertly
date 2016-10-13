package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.*;

/**
 * @author j.boesl, 21.07.15
 */
public class DeltaUpdateName extends Delta
{
  private IPropertyPath path;
  private String name;

  public DeltaUpdateName(IPropertyPath pPath, String pName)
  {
    path = pPath;
    name = pName;
  }

  @Override
  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IProperty<?, ?> property = path.find(pTo);
    property.rename(name);
  }

}
