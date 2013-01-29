package de.verpalnt.propertly.guibuilder;

import de.verpalnt.propertly.core.IProperty;
import de.verpalnt.propertly.core.PropertyDescription;
import de.verpalnt.propertly.core.listener.PropertyEventAdapter;
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
    tProperty.getPit().addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println("CHANGE: " + pProperty);
      }
    });
    PropertyTestChildren children = new PropertyTestChildren();
    tProperty.setCHILD(new PropertyTestChildren());
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

    System.out.println("child parent=" + tProperty.getCHILD().getParent());
    for (IProperty property : tProperty.getCHILD().getProperties())
      System.out.println(property);

    System.out.println("tProperty parent=" + tProperty.getPit().getParent());
    for (IProperty property : tProperty.getPit().getProperties())
      System.out.println(property);
  }

}
