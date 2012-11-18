package de.verpalnt.propertly;

import java.beans.PropertyChangeListener;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S, T>
{

  IPropertyDescription<S, T> getDescription();

  T getValue();

  void setValue(T pValue);

}
