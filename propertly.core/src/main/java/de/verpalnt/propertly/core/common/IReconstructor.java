package de.verpalnt.propertly.core.common;

import de.verpalnt.propertly.core.api.IPropertyPitProvider;

/**
 * @author j.boesl, 20.11.14
 */
public interface IReconstructor<T extends IPropertyPitProvider>
{

  T create();

}
