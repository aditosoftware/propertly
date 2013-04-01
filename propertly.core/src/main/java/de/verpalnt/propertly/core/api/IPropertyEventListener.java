package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEventListener<S extends IPropertyPitProvider, T>
{

  void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue);

  void propertyAdded(IPropertyPitProvider<S> pSource, IPropertyDescription<S, T> pPropertyDescription);

  void propertyWillBeRemoved(IPropertyPitProvider<S> pSource, IPropertyDescription<S, T> pPropertyDescription);

  void propertyRemoved(IPropertyPitProvider<S> pSource, IPropertyDescription<S, T> pPropertyDescription);

}
