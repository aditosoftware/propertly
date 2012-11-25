package de.verpalnt.propertly.test;

import de.verpalnt.propertly.*;
import de.verpalnt.propertly.listener.PropertyEventAdapter;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * @author PaL
 *         Date: 20.08.12
 *         Time: 00:55
 */
public class PropertyTest extends PropertyPit<PropertyTest> implements ITest
{

  public static final IPropertyDescription<PropertyTest, Integer> X = PD.create(PropertyTest.class);
  public static final IPropertyDescription<PropertyTest, Integer> Y = PD.create(PropertyTest.class);
  public static final IPropertyDescription<PropertyTest, Dimension> FF = PD.create(PropertyTest.class);

  @Test
  public PropertyTest()
  {
    //GetterSetterGen.run(this);
    addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println("CHANGE: " + pProperty);
      }
    });
    PropertyTestChildren children = setValue(CHILD, new PropertyTestChildren());
    children.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyAdded(IProperty pProperty)
      {
        System.out.println("ADDED: " + pProperty);
      }
    });

    setX(123);
    setValue(FF, new Dimension(123, 456));

    children.addProperty(PropertyDescription.create(PropertyTestChildren.class, Font.class, "font", null));
    children.addProperty(PropertyDescription.create(PropertyTestChildren.class, Color.class, "color", null));

    System.out.println("-------------------------------------------------------------------");

    for (IProperty property : getValue(CHILD).getProperties())
      System.out.println(property);

    for (IProperty property : getProperties())
      System.out.println(property);
  }

  public IProperty<PropertyTest, Integer> getPropertyX()
  {
    return getProperty(X);
  }

  public Integer getX()
  {
    return getValue(X);
  }

  public void setX(Integer pX)
  {
    setValue(X, pX);
  }

  public IProperty<PropertyTest, Integer> getPropertyY()
  {
    return getProperty(Y);
  }

  public Integer getY()
  {
    return getValue(Y);
  }

  public void setY(Integer pY)
  {
    setValue(Y, pY);
  }

  public IProperty<PropertyTest, Dimension> getPropertyFF()
  {
    return getProperty(FF);
  }

  public Dimension getFF()
  {
    return getValue(FF);
  }

  public void setFF(Dimension pFF)
  {
    setValue(FF, pFF);
  }

  public IProperty<ITest, PropertyTestChildren> getPropertyCHILD()
  {
    return getProperty(CHILD);
  }

  public PropertyTestChildren getCHILD()
  {
    return getValue(CHILD);
  }

  public void setCHILD(PropertyTestChildren pCHILD)
  {
    setValue(CHILD, pCHILD);
  }

}
