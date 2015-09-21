package de.adito.propertly.core.spi;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyPitEventListener<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends IPropertyEventListener<S, T>
{

  void propertyAdded(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes);

  void propertyWillBeRemoved(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes);

  void propertyRemoved(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes);

  void propertyOrderChanged(@Nonnull IPropertyPitProvider<P, S, T> pSource, @Nonnull Set<Object> pAttributes);
}
