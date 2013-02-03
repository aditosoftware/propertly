package de.verpalnt.propertly.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
public class MutablePropertyPit<S extends IMutablePropertyPitProvider, T> extends PropertyPit<S> implements IMutablePropertyPit<S, T>
{
  private final Object syncject = new Object();

  @Nonnull
  @Override
  public <T> IProperty<? super S, T> addProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    getNode().addProperty(pPropertyDescription);
    return getProperty(pPropertyDescription);
  }

  @Nullable
  @Override
  public <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    IProperty<? super S, T> property = getProperty(pPropertyDescription);
    if (property != null)
      getNode().removeProperty(pPropertyDescription.getName());
    return property;
  }

  @Override
  public IMutablePropertyPit<S, T> getPit()
  {
    return this;
  }

  @Override
  public T getChildType()
  {
    return null;
  }
}
