package de.verpalnt.propertly.test;

import de.verpalnt.propertly.MutablePropertyPit;
import de.verpalnt.propertly.PropertyDescription;

import java.awt.*;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 23:43
 */
public class PropertyTestChildren extends MutablePropertyPit<PropertyTestChildren>
{

  public PropertyTestChildren()
  {
    addProperty(PropertyDescription.create(PropertyTestChildren.class, Font.class, "font", null));
    addProperty(PropertyDescription.create(PropertyTestChildren.class, Color.class, "color", null));
  }

}
