package de.adito.propertly.core.spi;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyPitEventListener<S extends IPropertyPitProvider, T> extends IPropertyEventListener<S, T>
{

  void propertyRemoved(@Nonnull S pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes);

  void propertyAdded(@Nonnull S pSource, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nonnull Set<Object> pAttributes);

  void propertyOrderWillBeChanged(@Nonnull S pSource, @Nonnull Consumer<Runnable> pOnChanged, @Nonnull Set<Object> pAttributes);

  void propertyOrderChanged(@Nonnull S pSource, @Nonnull Set<Object> pAttributes);

}
