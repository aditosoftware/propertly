package de.verpalnt.propertly;

import java.util.Map;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 21:51
 */
public class MutablePropertyPit<S> extends PropertyPit<S> implements IMutablePropertyPit<S>
{

  @Override
  public synchronized <T> IProperty<? super S, T> addProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    Map<IPropertyDescription, IProperty> propertyMap = getPropertyMap();
    IProperty existingProperty = propertyMap.get(pPropertyDescription);
    if (existingProperty != null)
      throw new IllegalStateException("Property for '" + pPropertyDescription + "' already exists in '" + this + "'.");
    //noinspection unchecked
    IProperty<? super S, T> property = new _MutablePPProperty(pPropertyDescription);
    propertyMap.put(pPropertyDescription, property);
    return property;
  }

  @Override
  public synchronized <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    Map<IPropertyDescription, IProperty> propertyMap = getPropertyMap();
    IProperty existingProperty = propertyMap.get(pPropertyDescription);
    if (existingProperty == null)
      return null;
    if (existingProperty instanceof _MutablePPProperty)
      //noinspection unchecked
      return propertyMap.remove(pPropertyDescription);
    throw new IllegalStateException("Property for '" + pPropertyDescription + "' mustn't be removed from '" + this + "' " +
        "since it's not a mutable property.");
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
