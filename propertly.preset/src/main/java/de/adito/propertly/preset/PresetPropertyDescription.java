package de.adito.propertly.preset;

import de.adito.propertly.core.api.AbstractPropertyDescription;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author j.boesl, 09.09.16
 */
public class PresetPropertyDescription<S extends IPropertyPitProvider, T>
    extends AbstractPropertyDescription<S, T>
    implements IPresetPropertyDescription<S, T>
{
  private IPresetSupplier<S, T> presetSupplier;

  public PresetPropertyDescription(@Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType,
                                   @Nonnull String pName, @Nonnull IPresetSupplier<S, T> pPresetSupplier,
                                   @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    super(pSourceType, pType, pName, pAnnotations);
    presetSupplier = pPresetSupplier;
  }

  public PresetPropertyDescription(@Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType,
                                   @Nonnull String pName, @Nonnull IPresetSupplier<S, T> pPresetSupplier,
                                   @Nullable Annotation... pAnnotations)
  {
    super(pSourceType, pType, pName, pAnnotations);
    presetSupplier = pPresetSupplier;
  }

  public PresetPropertyDescription(@Nonnull Class<S> pSourceType, @Nonnull Class<? extends T> pType,
                                   @Nonnull String pName, @Nonnull IPresetSupplier<S, T> pPresetSupplier)
  {
    super(pSourceType, pType, pName);
    presetSupplier = pPresetSupplier;
  }

  public PresetPropertyDescription(@Nonnull IPropertyDescription<S, T> pPropertyDescription,
                                   @Nonnull IPresetSupplier<S, T> pPresetSupplier)
  {
    super(pPropertyDescription);
    presetSupplier = pPresetSupplier;
  }

  @Override
  public T getPreset()
  {
    return presetSupplier.get(this);
  }

  @Override
  public IPropertyDescription<S, T> copy(@Nullable String pNewName)
  {
    return pNewName == null ?
        new PresetPropertyDescription<>(this, presetSupplier) :
        new PresetPropertyDescription<>(getSourceType(), getType(), pNewName, presetSupplier, getAnnotations());
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO)
      return true;
    if (!(pO instanceof PresetPropertyDescription))
      return false;
    if (!super.equals(pO))
      return false;
    PresetPropertyDescription<?, ?> that = (PresetPropertyDescription<?, ?>) pO;
    return Objects.equals(_getPresetValueWithoutException(), that._getPresetValueWithoutException());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(super.hashCode(), _getPresetValueWithoutException());
  }

  @Override
  public String toString()
  {
    return "PresetPropertyDescription{" +
        "type='" + (getType() == null ? null : getType().getSimpleName()) +
        "', name='" + getName() + '\'' +
        "', preset='" + Objects.toString(_getPresetValueWithoutException()) + '\'' +
        '}';
  }

  private Object _getPresetValueWithoutException()
  {
    try {
      return getPreset();
    }
    catch (Exception pE) {
      return pE.getClass().getSimpleName() + ": " + pE.getMessage();
    }
  }

}
