package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IPropertyDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 16.11.14
 */
class DynamicHierarchyProperty extends HierarchyProperty
{

  DynamicHierarchyProperty(AbstractNode pNode, IPropertyDescription pPropertyDescription)
  {
    super(pNode, pPropertyDescription);
  }

  @Override
  public void rename(@NotNull String pName, @Nullable Object... pAttributes) throws PropertlyRenameException
  {
    getNode().rename(pName, PropertlyUtility.toNonnullSet(pAttributes));
  }

  @Override
  public boolean isDynamic()
  {
    return true;
  }
}
