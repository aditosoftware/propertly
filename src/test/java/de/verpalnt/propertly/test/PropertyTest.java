package de.verpalnt.propertly.test;

import de.verpalnt.propertly.IProperty;
import de.verpalnt.propertly.IPropertyDescription;
import de.verpalnt.propertly.PD;
import de.verpalnt.propertly.PropertyPit;
import de.verpalnt.propertly.listener.IPropertyEvent;
import de.verpalnt.propertly.listener.IPropertyEventListener;
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
    //GetterSetterGen.run(pit);
    addPropertyEventListener(new IPropertyEventListener()
    {
      @Override
      public void propertyChange(IPropertyEvent pEvent)
      {
        System.out.println("CHANGE: " + pEvent.getProperty());
      }
    });
    setX(123);
    setValue(FF, new Dimension(123, 456));
    System.out.println(getValue(X));
    for (IPropertyDescription<? super PropertyTest, ?> prop : getProperties())
      System.out.println(prop);
    System.out.println(getProperty(FF));

    //IPropertyDescription<ITest, Integer> name = NAME;
    //System.out.println(name);
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

}
