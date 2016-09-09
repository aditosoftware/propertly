package de.adito.propertly.preset;

import de.adito.propertly.core.spi.*;

/**
 * @author j.boesl, 09.09.16
 */
public interface IPresetPropertyDescription<S extends IPropertyPitProvider, T>  extends IPropertyDescription<S, T>
{

  T getPreset();

}
