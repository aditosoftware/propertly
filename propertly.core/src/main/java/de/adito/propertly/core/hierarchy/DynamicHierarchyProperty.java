package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.IPropertyDescription;
import de.adito.propertly.core.common.exception.PropertlyRenameException;

import javax.annotation.Nonnull;

/**
 * @author j.boesl, 16.11.14
 */
class DynamicHierarchyProperty extends HierarchyProperty
{

  public DynamicHierarchyProperty(AbstractNode pNode, IPropertyDescription pPropertyDescription)
  {
    super(pNode, pPropertyDescription);
  }

  @Override
  public void rename(@Nonnull String pName) throws PropertlyRenameException
  {
    getNode().rename(pName);
  }

}
