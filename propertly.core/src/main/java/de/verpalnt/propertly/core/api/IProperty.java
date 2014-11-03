package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S extends IPropertyPitProvider, T>
{

  IPropertyDescription<? super S, T> getDescription();

  T getValue();

  T setValue(T pValue);

  S getParent();

  Class<T> getType();

  String getName();

  void addPropertyEventListener(final IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

}
