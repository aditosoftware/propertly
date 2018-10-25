package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author j.boesl, 12.11.15
 */
public abstract class PropertyEventAdapter<S extends IPropertyPitProvider, T> implements IPropertyEventListener<S, T>
{
  @Override
  public void propertyValueWillBeChanged(@NotNull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                         @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyValueChanged(@NotNull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                   @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyWillBeRemoved(@NotNull IProperty<S, T> pProperty, @NotNull Consumer<Runnable> pOnRemoved,
                                    @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyNameChanged(@NotNull IProperty<S, T> pProperty, @NotNull String pOldName, @NotNull String pNewName,
                                  @NotNull Set<Object> pAttributes)
  {
  }
}
