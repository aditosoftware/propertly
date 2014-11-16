package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;

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
