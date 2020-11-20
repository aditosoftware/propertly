package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The default implementation for {@link IPropertyDescriptionDV}.
 *
 * @author j.boesl, 19.11.20
 */
public class PropertyDescriptionDV<S extends IPropertyPitProvider, T> extends PropertyDescription<S, T> implements IPropertyDescriptionDV<S, T>
{
  private final T defaultValue;

  public PropertyDescriptionDV(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType, @NotNull String pName, @NotNull T pDefaultValue,
                               @Nullable Annotation... pAnnotations)
  {
    super(pSourceType, pType, pName, pAnnotations);
    defaultValue = pDefaultValue;
  }

  public PropertyDescriptionDV(@NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull T pDefaultValue)
  {
    super(pPropertyDescription);
    defaultValue = pDefaultValue;
  }

  @NotNull
  public T getDefaultValue()
  {
    return defaultValue;
  }

  @Override
  public IPropertyDescription<S, T> copy(@Nullable String pNewName)
  {
    return pNewName == null ?
        new PropertyDescriptionDV<>(this, defaultValue) :
        new PropertyDescriptionDV<>(getSourceType(), getType(), pNewName, defaultValue, getAnnotations());

  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO)
      return true;
    if (!(pO instanceof PropertyDescriptionDV))
      return false;
    PropertyDescriptionDV<?, ?> that = (PropertyDescriptionDV<?, ?>) pO;
    return Objects.equals(getSourceType(), that.getSourceType()) &&
        Objects.equals(getType(), that.getType()) &&
        Objects.equals(getName(), that.getName()) &&
        Arrays.equals(getAnnotations(), that.getAnnotations()) &&
        Objects.equals(getDefaultValue(), that.getDefaultValue());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(super.hashCode(), defaultValue);
  }

  @Override
  public String toString()
  {
    return "PropertyDescription{" +
        "type='" + (getType() == null ? null : getType().getSimpleName()) +
        "', name='" + getName() +
        "', defaultValue='" + defaultValue + '\'' +
        '}';
  }
}
