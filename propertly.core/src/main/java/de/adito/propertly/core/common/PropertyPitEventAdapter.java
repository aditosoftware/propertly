package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * An adapter implementation for IPropertyPitEventListener implementations.
 *
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyPitEventAdapter<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPitEventListener<P, S, T>
{
  @Override
  public void propertyChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyAdded(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyWillBeRemoved(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyRemoved(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyNameChanged(@Nonnull IProperty<S, T> pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyOrderChanged(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull Set<Object> pAttributes)
  {
  }
}
