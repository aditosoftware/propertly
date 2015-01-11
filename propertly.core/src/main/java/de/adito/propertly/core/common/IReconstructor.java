package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.IPropertyPitProvider;

/**
 * @author j.boesl, 20.11.14
 */
public interface IReconstructor<T extends IPropertyPitProvider>
{

  T create();

}
