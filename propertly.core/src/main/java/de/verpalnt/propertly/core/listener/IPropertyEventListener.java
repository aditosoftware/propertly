package de.verpalnt.propertly.core.listener;

import de.verpalnt.propertly.core.IProperty;
import de.verpalnt.propertly.core.IPropertyDescription;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEventListener<S, T>
{

  void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue);

  void propertyAdded(IPropertyDescription<S, T> pPropertyDescription);

  void propertyRemoved(IPropertyDescription<S, T> pPropertyDescription);

}
