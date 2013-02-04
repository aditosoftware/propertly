package de.verpalnt.propertly.test.core;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.hierarchy.Hierarchy;
import de.verpalnt.propertly.core.listener.PropertyEventAdapter;
import de.verpalnt.propertly.test.common.PropertyTestChildren;
import de.verpalnt.propertly.test.common.TProperty;
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
    TProperty tProperty = Hierarchy.create("root", new TProperty());
    //GetterSetterGen.run(tProperty);
    tProperty.getPit().addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println("CHANGE: " + pProperty);
      }
    });
    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());
    children.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyAdded(IPropertyDescription pPropertyDescription)
      {
        System.out.println("ADDED: " + pPropertyDescription);
      }
    });

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1", null).setValue(Color.BLACK);
    children.addProperty(Color.class, "color2", null).setValue(Color.RED);

    System.out.println("-------------------------------------------------------------------");

    System.out.println("child parent=" + tProperty.getCHILD().getParent());
    for (IProperty property : tProperty.getCHILD().getProperties())
      System.out.println(property);

    System.out.println("tProperty parent=" + tProperty.getPit().getParent());
    for (IProperty property : tProperty.getPit().getProperties())
      System.out.println(property);
  }

}
