package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IPropertyDescription;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:33
 */
public interface NodeListener
{

  void valueChanged(Node pNode, Object pOldValue, Object pNewValue);

  void nodeAdded(Node pParent, IPropertyDescription pPropertyDescription);

  void nodeRemoved(Node pParent, IPropertyDescription pPropertyDescription);

}