package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

/**
 * An adapter implementation for IPropertyPitEventListener implementations.
 *
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyPitEventAdapter<S extends IPropertyPitProvider, T> extends PropertyEventAdapter<S, T>
    implements IPropertyPitEventListener<S, T>
{
  @Override
  public void propertyRemoved(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyAdded(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyOrderWillBeChanged(@NotNull S pSource, @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyOrderChanged(@NotNull S pSource, @NotNull Set<Object> pAttributes)
  {
  }
}
