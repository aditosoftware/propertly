package de.adito.propertly.preset;

import de.adito.propertly.core.spi.*;

/**
 * @author j.boesl, 09.09.16
 */
public interface IPresetSupplier<S extends IPropertyPitProvider, T>
{

  T get(IPropertyDescription<S, T> pDescription);

}
