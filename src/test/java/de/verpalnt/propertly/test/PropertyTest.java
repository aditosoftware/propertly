package de.verpalnt.propertly.test;

import de.verpalnt.propertly.IProperty;
import de.verpalnt.propertly.PropertyDescription;
import de.verpalnt.propertly.listener.PropertyEventAdapter;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * @author PaL
 *         Date: 20.08.12
 *         Time: 00:55
 */
public class PropertyTest
{

  @Test
  public PropertyTest()
  {
    TProperty tProperty = new TProperty();
    //GetterSetterGen.run(tProperty);
    tProperty.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println("CHANGE: " + pProperty);
      }
    });
    PropertyTestChildren children = tProperty.setValue(TProperty.CHILD, new PropertyTestChildren());
    children.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyAdded(IProperty pProperty)
      {
        System.out.println("ADDED: " + pProperty);
      }
    });

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(PropertyDescription.create(PropertyTestChildren.class, Font.class, "font", null));
    children.addProperty(PropertyDescription.create(PropertyTestChildren.class, Color.class, "color", null));

    System.out.println("-------------------------------------------------------------------");

    for (IProperty property : tProperty.getValue(TProperty.CHILD).getProperties())
      System.out.println(property);

    for (IProperty property : tProperty.getProperties())
      System.out.println(property);
  }

}
