package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
public class MutablePropertyPit<S extends IMutablePropertyPitProvider, T> extends PropertyPit<S, T> implements IMutablePropertyPit<S, T>
{

  private Class<T> allowedChildType;


  MutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource);
    allowedChildType = pAllowedChildType;
  }

  public static <S extends IMutablePropertyPitProvider<S, T>, T> MutablePropertyPit<S, T> create(S pCreateFor, Class<T> pAllowedChildType)
  {
    return new MutablePropertyPit<S, T>(pCreateFor, pAllowedChildType);
  }

  @Override
  public IMutablePropertyPit<S, T> getPit()
  {
    return this;
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull E pValue)
  {
    return addProperty(UUID.randomUUID().toString(), pValue);
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull String pName, @Nonnull E pValue)
  {
    IPropertyDescription<S, E> description = PropertyDescription.create((Class<S>) getClass(), (Class<E>) pValue.getClass(), pName, null);
    IProperty<S, E> property = addProperty(description);
    property.setValue(pValue);
    return property;
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull IPropertyDescription<S, E> pPropertyDescription)
  {
    getNode().addProperty(pPropertyDescription);
    return getProperty(pPropertyDescription);
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull Class<E> pType, @Nonnull String pName, @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return addProperty(PropertyDescription.create((Class<S>) getSource().getClass(), pType, pName, pAnnotations));
  }

  @Override
  public boolean removeProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    return getNode().removeProperty(pPropertyDescription);
  }

  @Override
  public Class<T> getChildType()
  {
    return allowedChildType;
  }

}