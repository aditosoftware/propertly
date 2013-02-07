package de.verpalnt.propertly.core.api;

import de.verpalnt.propertly.core.hierarchy.PropertyPit;

/**
 * @author PaL
 *         Date: 14.10.12
 *         Time: 14:52
 */
public interface IPropertyPitProvider<S extends IPropertyPitProvider>
{

  PropertyPit<S> getPit();

}
