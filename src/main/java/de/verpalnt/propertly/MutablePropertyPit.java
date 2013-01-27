package de.verpalnt.propertly;

import de.verpalnt.propertly.listener.IPropertyEventListener;

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
    IProperty<? super S, T> property;
    synchronized (syncject)
    {
      Map<IPropertyDescription, IProperty> propertyMap = getPropertyMap();
      IProperty existingProperty = propertyMap.get(pPropertyDescription);
      if (existingProperty != null)
        throw new IllegalStateException("Property for '" + pPropertyDescription + "' already exists in '" + this + "'.");
      //noinspection unchecked
      property = new _MutablePPProperty(pPropertyDescription);
      propertyMap.put(pPropertyDescription, property);
    }
    for (IPropertyEventListener listener : getListeners())
      listener.propertyAdded(property);
    return property;
  }

  @Nullable
  @Override
  public <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    IProperty<? super S, T> property;
    synchronized (syncject)
    {
      Map<IPropertyDescription, IProperty> propertyMap = getPropertyMap();
      IProperty existingProperty = propertyMap.get(pPropertyDescription);
      if (existingProperty == null)
        return null;
      if (existingProperty instanceof _MutablePPProperty)
        //noinspection unchecked
        property = propertyMap.remove(pPropertyDescription);
      else
        throw new IllegalStateException("Property for '" + pPropertyDescription + "' mustn't be removed from '" + this + "' " +
            "since it's not a mutable property.");
    }
    for (IPropertyEventListener listener : getListeners())
      listener.propertyRemoved(property);
    return property;
  }

  /**
   * PropertyImpl
   */
  private class _MutablePPProperty<S, T> extends PPProperty<S, T>
  {
    private _MutablePPProperty(IPropertyDescription<S, T> propertyDescription)
    {
      super(propertyDescription);
    }
  }

}
