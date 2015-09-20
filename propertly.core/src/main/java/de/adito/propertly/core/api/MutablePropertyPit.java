package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
class MutablePropertyPit<P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends PropertyPit<P, S, T>
    implements IMutablePropertyPit<P, S, T>
{

  private Class<T> allowedChildType;


  MutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource);
    allowedChildType = pAllowedChildType;
  }

  public static <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
  MutablePropertyPit<P, S, T> create(S pCreateFor, Class<T> pAllowedChildType)
  {
    return new MutablePropertyPit<P, S, T>(pCreateFor, pAllowedChildType);
  }

  @Nonnull
  @Override
  public IMutablePropertyPit<P, S, T> getPit()
  {
    return this;
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull E pValue)
  {
    if (pValue instanceof IPropertyPitProvider)
    {
      IPropertyPit pit = ((IPropertyPitProvider) pValue).getPit();
      if (pit.isValid())
        return addProperty(pit.getOwnProperty().getName(), pValue);
    }
    return addProperty(UUID.randomUUID().toString(), pValue);
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull String pName, @Nonnull E pValue)
  {
    //noinspection unchecked
    IPropertyDescription<S, E> description = PropertyDescription.create((Class) getClass(), pValue.getClass(), pName);
    IProperty<S, E> property = addProperty(description);
    property.setValue(pValue);
    return property;
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull IPropertyDescription<S, E> pPropertyDescription, @Nullable Object... pAttributes)
  {
    getNode().addProperty(pPropertyDescription, PropertlyUtility.toNonnullList(pAttributes));
    return getProperty(pPropertyDescription);
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull Class<E> pType, @Nonnull String pName,
                                                   @Nullable Annotation... pAnnotations)
  {
    //noinspection unchecked
    return addProperty(PropertyDescription.create((Class<S>) getSource().getClass(), pType, pName, pAnnotations));
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> addProperty(@Nonnull Class<E> pType, @Nonnull String pName,
                                                   @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    //noinspection unchecked
    return addProperty(PropertyDescription.create((Class<S>) getSource().getClass(), pType, pName, pAnnotations));
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription<? super S, T> pPropertyDescription, @Nullable Object... pAttributes)
  {
    return getNode().removeProperty(pPropertyDescription, PropertlyUtility.toNonnullList(pAttributes));
  }

  @Override
  public boolean removeProperty(@Nonnull IProperty<S, T> pProperty)
  {
    return removeProperty(pProperty.getDescription());
  }

  @Override
  public Class<T> getChildType()
  {
    return allowedChildType;
  }

}