package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

/**
 * @author PaL
 *         Date: 30.09.11
 *         Time: 23:49
 */
public class PropertyDescription<S extends IPropertyPitProvider, T> extends AbstractPropertyDescription<S, T>
{

  public PropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType, @NotNull String pName,
                             @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    super(pSourceType, pType, pName, pAnnotations);
  }

  public PropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType, @NotNull String pName,
                             @Nullable Annotation... pAnnotations)
  {
    super(pSourceType, pType, pName, pAnnotations);
  }

  public PropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType, @NotNull String pName)
  {
    super(pSourceType, pType, pName);
  }

  public PropertyDescription(@NotNull IPropertyDescription<S, T> pPropertyDescription)
  {
    super(pPropertyDescription);
  }

  @Override
  public IPropertyDescription<S, T> copy(@Nullable String pNewName)
  {
    return pNewName == null ?
        new PropertyDescription<>(this) :
        new PropertyDescription<>(getSourceType(), getType(), pNewName, getAnnotations());
  }

  @Override
  public String toString()
  {
    return "PropertyDescription{" +
        "type='" + (getType() == null ? null : getType().getSimpleName()) +
        "', name='" + getName() + '\'' +
        '}';
  }

}
