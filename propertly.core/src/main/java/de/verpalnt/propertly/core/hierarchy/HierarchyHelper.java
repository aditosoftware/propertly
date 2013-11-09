package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyPit;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

/**
 * Created by PaL on 09.11.13.
 */
class HierarchyHelper
{

  private HierarchyHelper()
  {
  }

  static INode getNode(IPropertyPitProvider pPropertyPitProvider)
  {
    final IPropertyPit pit = pPropertyPitProvider.getPit();
    if (pit instanceof PropertyPit)
      return ((PropertyPit) pit).getNode();
    throw new IllegalStateException("only 'PropertyPit' is supported at hierarchy.");
  }

  static void setNode(IPropertyPitProvider pPropertyPitProvider, INode pNode)
  {
    final IPropertyPit pit = pPropertyPitProvider.getPit();
    if (pit instanceof PropertyPit)
      ((PropertyPit) pit).setNode(pNode);
    else
      throw new IllegalStateException("only 'PropertyPit' is supported at hierarchy.");
  }

}
