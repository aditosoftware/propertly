package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;

/**
 * @author PaL, 09.11.13
 */
class HierarchyHelper
{

  private HierarchyHelper()
  {
  }

  static boolean hasNode(@Nonnull IPropertyPitProvider pPropertyPitProvider)
  {
    return _getPropertyPit(pPropertyPitProvider).isValid();
  }

  @Nonnull
  static INode getNode(@Nonnull IPropertyPitProvider pPropertyPitProvider)
  {
    return _getPropertyPit(pPropertyPitProvider).getNode();
  }

  static void setNode(@Nonnull IPropertyPitProvider pPropertyPitProvider, INode pNode)
  {
    _getPropertyPit(pPropertyPitProvider).setNode(pNode);
  }

  @Nonnull
  private static PropertyPit _getPropertyPit(@Nonnull IPropertyPitProvider pPropertyPitProvider)
  {
    final IPropertyPit pit = pPropertyPitProvider.getPit();
    if (pit instanceof PropertyPit)
      return (PropertyPit) pit;
    else
      throw new IllegalStateException("only 'PropertyPit' is supported at hierarchy.");
  }

}
