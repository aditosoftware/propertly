package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author j.boesl, 12.11.15
 */
public abstract class PropertyEventAdapter<S extends IPropertyPitProvider, T> implements IPropertyEventListener<S, T>
{
  @Override
  public void propertyValueWillBeChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                         @Nonnull Consumer<Runnable> pOnChanged, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyValueChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                   @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyWillBeRemoved(@Nonnull IProperty<S, T> pProperty, @Nonnull Consumer<Runnable> pOnRemoved,
                                    @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyNameChanged(@Nonnull IProperty<S, T> pProperty, @Nonnull String pOldName, @Nonnull String pNewName,
                                  @Nonnull Set<Object> pAttributes)
  {
  }
}
