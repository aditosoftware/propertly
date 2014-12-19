package de.adito.propertly.core.api;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEventListener<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
{

  void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue);

  void propertyAdded(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription);

  void propertyWillBeRemoved(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription);

  void propertyRemoved(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription);

}
