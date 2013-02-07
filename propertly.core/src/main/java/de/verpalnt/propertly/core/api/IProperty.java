package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S extends IPropertyPitProvider, T> extends IPropertyDescription<S, T>
{

  IPropertyDescription<S, T> getDescription();

  T getValue();

  T setValue(T pValue);

  void addPropertyEventListener(final IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

}
