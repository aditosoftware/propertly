package de.adito.propertly.diff.data;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;

import java.util.*;

/**
 * @author j.boesl, 13.09.15
 */
public class OrgPatchedMapping
{

  private PropertyNode orgToPatched;
  private Map<IProperty, PropertyNode> patchedToOrg;

  public OrgPatchedMapping(IHierarchy<?> pHierarchy)
  {
    orgToPatched = new PropertyNode(pHierarchy.getProperty());
    patchedToOrg = new HashMap<IProperty, PropertyNode>();
    _fillPatchedToOrg(patchedToOrg, orgToPatched);
  }

  private void _fillPatchedToOrg(Map<IProperty, PropertyNode> pPatchedToOrg, PropertyNode pPropertyNode)
  {
    pPatchedToOrg.put(pPropertyNode.getRef(), pPropertyNode);
    for (PropertyNode propertyNode : pPropertyNode.getChildren())
      _fillPatchedToOrg(pPatchedToOrg, propertyNode);
  }

  public IProperty<?, ?> removeOrgAndKeepCopy(IPropertyPath pSourcePath, IProperty pTargetProperty)
  {
    PropertyNode sourcePropNode = patchedToOrg.get(orgToPatched.resolveChild(pSourcePath).getRef());
    IProperty<?, ?> sourceProp = sourcePropNode.getRef();
    IProperty<?, IPropertyPitProvider> copy;

    if (pTargetProperty == null)
      copy = new Hierarchy<IPropertyPitProvider>(sourceProp.getName(), (IPropertyPitProvider) sourceProp.getValue()).getProperty();
    else
    {
      pTargetProperty.setValue(sourceProp.getValue());
      copy = pTargetProperty;
    }

    _replaceRefs(sourceProp, copy);

    IPropertyPit pit = sourceProp.getParent().getPit();
    if (sourceProp.isDynamic() && pit instanceof IMutablePropertyPit)
      ((IMutablePropertyPit) pit).removeProperty(sourceProp);
    else
      sourceProp.setValue(null);

    return copy;
  }

  private void _replaceRefs(IProperty<?, ?> pOldProp, IProperty<?, ?> pNewProp)
  {
    PropertyNode orgPropNode = patchedToOrg.remove(pOldProp);
    orgPropNode.setRef(pNewProp);
    patchedToOrg.put(pNewProp, orgPropNode);
    Object oldValue = pOldProp.getValue();
    if (oldValue instanceof IPropertyPitProvider)
    {
      IPropertyPit<?, ?, ?> oldPit = ((IPropertyPitProvider) oldValue).getPit();
      IPropertyPit<?, ?, ?> newPit = ((IPropertyPitProvider) pNewProp.getValue()).getPit();
      for (IProperty<? extends IPropertyPitProvider<?, ?, ?>, ?> oldProperty : oldPit.getProperties())
        _replaceRefs(oldProperty, newPit.findProperty(oldProperty.getDescription()));
    }
  }

}
