package de.verpalnt.propertly.test;

import de.verpalnt.propertly.*;
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
    GetterSetterGen.run(this);
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

    setValue(CHILD, new PropertyTestChildren());
    for (IProperty property : getValue(CHILD).getProperties())
      System.out.println(property);

    for (IProperty property : getProperties())
      System.out.println(property);
  }

  public IProperty<PropertyTest, Integer> getPropertyX(){return getProperty(X);}
  public Integer getX(){return getValue(X);}
  public void setX(Integer pX){setValue(X, pX);}
  public IProperty<PropertyTest, Integer> getPropertyY(){return getProperty(Y);}
  public Integer getY(){return getValue(Y);}
  public void setY(Integer pY){setValue(Y, pY);}
  public IProperty<PropertyTest, Dimension> getPropertyFF(){return getProperty(FF);}
  public Dimension getFF(){return getValue(FF);}
  public void setFF(Dimension pFF){setValue(FF, pFF);}
  public IProperty<ITest, PropertyTestChildren> getPropertyCHILD(){return getProperty(CHILD);}
  public PropertyTestChildren getCHILD(){return getValue(CHILD);}
  public void setCHILD(PropertyTestChildren pCHILD){setValue(CHILD, pCHILD);}

}
