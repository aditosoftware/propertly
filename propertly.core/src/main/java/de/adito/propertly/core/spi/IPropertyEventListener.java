package de.adito.propertly.core.spi;

/**
 * @author PaL
 *         Date: 06.01.15
 *         Time. 22:03
 */
public interface IPropertyEventListener<S extends IPropertyPitProvider<?, S, T>, T>
{

  void propertyChanged(IProperty<S, T> pProperty, T pOldValue, T pNewValue);

}
