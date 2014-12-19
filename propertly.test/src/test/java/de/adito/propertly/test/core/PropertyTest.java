package de.adito.propertly.test.core;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertyEventAdapter;
import de.adito.propertly.core.hierarchy.Hierarchy;
import de.adito.propertly.test.common.*;
import org.junit.*;

import java.awt.*;

/**
 * @author PaL
 *         Date: 20.08.12
 *         Time: 00:55
 */
public class PropertyTest
{


  @Test
  public void simpleTest()
  {
    Hierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new TProperty()));
    hierarchy.addPropertyEventListener(new IPropertyEventListener()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println("\tpropertyChange: " + pOldValue + ", " + pNewValue + ", " + pProperty);
      }

      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        System.out.println("\tpropertyAdded: " + pSource + ", " + pPropertyDescription);
      }

      @Override
      public void propertyWillBeRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        System.out.println("\tpropertyWillBeRemoved: " + pSource + ", " + pPropertyDescription);
      }

      @Override
      public void propertyRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        System.out.println("\tpropertyRemoved: " + pSource + ", " + pPropertyDescription);
      }
    });
    TProperty tProperty = hierarchy.getValue();
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
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
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
    for (IProperty property : tProperty.getCHILD())
      System.out.println(property);

    System.out.println("tProperty parent=" + tProperty.getPit().getParent());
    for (IProperty property : tProperty.getPit())
      System.out.println(property);


    Exception ex = null;
    try
    {
      tProperty.setX(-1);
    }
    catch (Exception e)
    {
      ex = e;
      System.out.println("failed properly: " + e.getMessage());
    }
    Assert.assertNotNull(ex);
  }

}
