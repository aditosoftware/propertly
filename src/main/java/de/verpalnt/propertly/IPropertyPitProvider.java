package de.verpalnt.propertly;

/**
 * @author PaL
 *         Date: 14.10.12
 *         Time: 14:52
 */
public interface IPropertyPitProvider<S extends IPropertyPitProvider>
{

  IPropertyPit<S> getPit();

}
