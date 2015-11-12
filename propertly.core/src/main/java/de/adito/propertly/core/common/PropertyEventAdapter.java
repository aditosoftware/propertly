package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.Set;

/**
 * @author j.boesl, 12.11.15
 */
public class PropertyEventAdapter<S extends IPropertyPitProvider, T> implements IPropertyEventListener<S, T>
{
  @Override
  public void propertyChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyNameChanged(@Nonnull IProperty<S, T> pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
  }
}
