package de.verpalnt.propertly;

import de.verpalnt.propertly.listener.IPropertyEventListener;

import java.util.Set;

/**
 * @author PaL
 *         Date: 14.10.12
 *         Time: 15:52
 */
public interface IPropertyPit<S>
{
  <SOURCE, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription);

  <SOURCE, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription);

  <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription);

  <T> void setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue);

  Set<IPropertyDescription<? super S, ?>> getProperties();

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);
}
