package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
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
  public void propertyRemoved(@Nonnull S pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyAdded(@Nonnull S pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyOrderWillBeChanged(@Nonnull S pSource, @Nonnull Consumer<Runnable> pOnChanged, @Nonnull Set<Object> pAttributes)
  {
  }

  @Override
  public void propertyOrderChanged(@Nonnull S pSource, @Nonnull Set<Object> pAttributes)
  {
  }
}
