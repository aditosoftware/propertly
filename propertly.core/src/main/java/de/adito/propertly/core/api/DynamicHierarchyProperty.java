package de.adito.propertly.core.api;

import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IPropertyDescription;

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

  @Override
  public boolean isDynamic()
  {
    return true;
  }
}
