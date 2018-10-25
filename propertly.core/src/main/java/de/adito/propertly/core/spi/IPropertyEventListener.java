package de.adito.propertly.core.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author PaL
 *         Date: 06.01.15
 *         Time. 22:03
 */
public interface IPropertyEventListener<S extends IPropertyPitProvider, T> extends EventListener
{

  void propertyValueWillBeChanged(@NotNull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                  @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes);

  void propertyValueChanged(@NotNull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue, @NotNull Set<Object> pAttributes);

  void propertyNameChanged(@NotNull IProperty<S, T> pProperty, @NotNull String pOldName, @NotNull String pNewName, @NotNull Set<Object> pAttributes);

  void propertyWillBeRemoved(@NotNull IProperty<S, T> pProperty, @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes);

}
