package de.adito.propertly.core.spi;

import org.jetbrains.annotations.NotNull;

/**
 * This is an extension to {@link IPropertyDescription} which supplies a default value which can be used for the respective {@link IProperty}.
 *
 * @author j.boesl, 20.11.20
 */
public interface IPropertyDescriptionDV<S extends IPropertyPitProvider, T> extends IPropertyDescription<S, T>
{

  /**
   * @return the default value which is present at this PropertyDescription.
   */
  @NotNull
  T getDefaultValue();

}
