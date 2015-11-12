package de.adito.propertly.core.spi;

import javax.annotation.*;
import java.util.Set;

/**
 * @author PaL
 *         Date: 06.01.15
 *         Time. 22:03
 */
public interface IPropertyEventListener<S extends IPropertyPitProvider, T>
{

  void propertyChanged(@Nonnull IProperty<S, T> pProperty, @Nullable T pOldValue, @Nullable T pNewValue, @Nonnull Set<Object> pAttributes);

  void propertyNameChanged(@Nonnull IProperty<S, T> pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes);

}
