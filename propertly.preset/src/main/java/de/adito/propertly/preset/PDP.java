package de.adito.propertly.preset;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;

/**
 * @author j.boesl, 09.09.16
 */
public class PDP
{

  private PDP()
  {
  }

  @Nonnull
  public static <S extends IPropertyPitProvider<?, ? extends S, ?>, T> IPresetPropertyDescription<S, T>
  create(@Nonnull Class<S> pSource, IPresetSupplier<S, T> pPresetSupplier)
  {
    IPropertyDescription<S, T> description = PD.create(pSource);
    return new PresetPropertyDescription<>(description, pPresetSupplier);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider<?, ? extends S, ?>, T> IPresetPropertyDescription<S, T>
  create(@Nonnull Class<S> pSource, T pPreset)
  {
    return create(pSource, description -> pPreset);
  }

  @Nonnull
  public static <S extends IPropertyPitProvider<?, ? extends S, ?>, T extends IPropertyPitProvider> IPresetPropertyDescription<S, T>
  createWithPresetFromType(@Nonnull Class<S> pSource)
  {
    return create(pSource, description -> PropertlyUtility.create(description.getType()));
  }

}
