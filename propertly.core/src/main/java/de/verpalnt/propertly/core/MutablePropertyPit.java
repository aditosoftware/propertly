package de.verpalnt.propertly.core;

import de.verpalnt.propertly.core.hierarchy.Node;
import de.verpalnt.propertly.core.listener.IPropertyEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
public class MutablePropertyPit<S extends IPropertyPitProvider> extends PropertyPit<S> implements IMutablePropertyPit<S>
{
  private final Object syncject = new Object();

  @Nonnull
  @Override
  public <T> IProperty<? super S, T> addProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    getNode().addProperty(pPropertyDescription);
    IProperty<? super S, T> property = getProperty(pPropertyDescription);
    for (IPropertyEventListener listener : getListeners())
      listener.propertyAdded(property);
    return property;
  }

  @Nullable
  @Override
  public <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    IProperty<? super S, T> property = getProperty(pPropertyDescription);
    if (property != null)
    {
      getNode().removeProperty(pPropertyDescription.getName());
      for (IPropertyEventListener listener : getListeners())
        listener.propertyRemoved(property);
    }
    return property;
  }
}
