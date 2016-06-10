package de.adito.propertly.core.spi;

import javax.annotation.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author PaL
 *         Date: 06.01.15
 *         Time. 22:03
 */
public interface IPropertyEventListener<S extends IPropertyPitProvider, T> extends EventListener
{

  void propertyValueWillBeChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue,
                                  @Nonnull Consumer<Runnable> pOnChanged, @Nonnull Set<Object> pAttributes);

  void propertyValueChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue, @Nonnull Set<Object> pAttributes);

  void propertyNameChanged(@Nonnull IProperty<S, T> pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes);

  void propertyWillBeRemoved(@Nonnull IProperty<S, T> pProperty, @Nonnull Consumer<Runnable> pOnRemoved, @Nonnull Set<Object> pAttributes);

}
