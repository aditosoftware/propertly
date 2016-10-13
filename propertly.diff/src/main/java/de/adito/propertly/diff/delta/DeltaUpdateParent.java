package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.*;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.OrgPatchedMapping;

/**
 * @author j.boesl, 21.07.15
 */
public class DeltaUpdateParent extends Delta
{
  private IPropertyPath sourcePath;
  private IPropertyPath targetPath;
  private IPropertyDescription targetDescription;
  private int index;

  public DeltaUpdateParent(IPropertyPath pSourcePath, IPropertyPath pParentalTargetPath,
                           IPropertyDescription pTargetDescription, int pIndex)
  {
    sourcePath = pSourcePath;
    targetPath = pParentalTargetPath;
    targetDescription = pTargetDescription;
    index = pIndex;
  }

  @Override
  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IPropertyPitProvider parentalTargetPPP = (IPropertyPitProvider) targetPath.find(pTo).getValue();
    IProperty targetProperty = parentalTargetPPP.getPit().findProperty(targetDescription);
    if (targetProperty != null && !targetProperty.getDescription().equals(targetDescription))
      pOrgPatchedMapping.removeOrgAndKeepCopy(new PropertyPath(targetProperty), null);

    if (targetProperty == null)
    {
      if (index >= 0 && parentalTargetPPP instanceof IIndexedMutablePropertyPitProvider)
        targetProperty = ((IIndexedMutablePropertyPit) parentalTargetPPP.getPit()).addProperty(index, targetDescription);
      else
        targetProperty = ((IMutablePropertyPit) parentalTargetPPP.getPit()).addProperty(targetDescription);
    }

    pOrgPatchedMapping.removeOrgAndKeepCopy(sourcePath, targetProperty);
  }

}
