package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
public class MutablePropertyPit<S extends IMutablePropertyPitProvider, T> extends PropertyPit<S> implements IMutablePropertyPit<S, T>
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

  @Override
  @Nonnull
  public IProperty<S, T> addProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    getNode().addProperty(pPropertyDescription);
    return getProperty(pPropertyDescription);
  }

  @Override
  @Nonnull
  public IProperty<S, T> addProperty(@Nonnull Class<T> pType, @Nonnull String pName,
                                     @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return addProperty(PropertyDescription.create((Class<S>) getSource().getClass(), pType, pName, pAnnotations));
  }

  @Override
  public boolean removeProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    IProperty<S, T> property = getProperty(pPropertyDescription);
    return getNode().removeProperty(pPropertyDescription.getName());
  }

  @Override
  public Class<T> getChildType()
  {
    return allowedChildType;
  }

}