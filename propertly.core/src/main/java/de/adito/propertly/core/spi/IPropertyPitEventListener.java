package de.adito.propertly.core.spi;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyPitEventListener<S extends IPropertyPitProvider, T> extends IPropertyEventListener<S, T>
{

  void propertyRemoved(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes);

  void propertyAdded(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes);

  void propertyOrderWillBeChanged(@NotNull S pSource, @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes);

  void propertyOrderChanged(@NotNull S pSource, @NotNull Set<Object> pAttributes);

}
