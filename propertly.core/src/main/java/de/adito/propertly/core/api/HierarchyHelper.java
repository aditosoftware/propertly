package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyPit;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author PaL, 09.11.13
 */
class HierarchyHelper
{

  private HierarchyHelper()
  {
  }

  @NotNull
  static INode getNode(@NotNull IPropertyPitProvider pPropertyPitProvider)
  {
    return _getPropertyPit(pPropertyPitProvider).getNode();
  }

  static void setNode(@NotNull IPropertyPitProvider pPropertyPitProvider, INode pNode)
  {
    _getPropertyPit(pPropertyPitProvider).setNode(pNode);
  }

  static INode getNode(@NotNull IProperty pProperty)
  {
    if (pProperty instanceof HierarchyProperty)
      return ((HierarchyProperty) pProperty).getNode();
    throw new IllegalStateException("only 'HierarchyProperty' is supported at hierarchy.");
  }

  @NotNull
  private static PropertyPit _getPropertyPit(@NotNull IPropertyPitProvider pPropertyPitProvider)
  {
    final IPropertyPit pit = pPropertyPitProvider.getPit();
    if (pit instanceof PropertyPit)
      return (PropertyPit) pit;
    else
      throw new IllegalStateException("only 'PropertyPit' is supported at hierarchy.");
  }

}
