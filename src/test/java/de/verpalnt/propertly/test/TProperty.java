package de.verpalnt.propertly.test;

import de.verpalnt.propertly.*;

import java.awt.*;

/**
 * @author PaL
 *         Date: 26.11.12
 *         Time: 21:39
 */
public class TProperty implements IPropertyPitProvider<TProperty>, ITest
{

  public static final IPropertyDescription<TProperty, Integer> X = PD.create(TProperty.class);
  public static final IPropertyDescription<TProperty, Integer> Y = PD.create(TProperty.class);
  public static final IPropertyDescription<TProperty, Dimension> FF = PD.create(TProperty.class);

  private IPropertyPit<TProperty> pit = PropertyPit.create(this);

  public IProperty<TProperty, Integer> getPropertyX()
  {
    return pit.getProperty(X);
  }

  public Integer getX()
  {
    return pit.getValue(X);
  }

  public void setX(Integer pX)
  {
    pit.setValue(X, pX);
  }

  public IProperty<TProperty, Integer> getPropertyY()
  {
    return pit.getProperty(Y);
  }

  public Integer getY()
  {
    return pit.getValue(Y);
  }

  public void setY(Integer pY)
  {
    pit.setValue(Y, pY);
  }

  public IProperty<TProperty, Dimension> getPropertyFF()
  {
    return pit.getProperty(FF);
  }

  public Dimension getFF()
  {
    return pit.getValue(FF);
  }

  public void setFF(Dimension pFF)
  {
    pit.setValue(FF, pFF);
  }

  public IProperty<ITest, PropertyTestChildren> getPropertyCHILD()
  {
    return pit.getProperty(CHILD);
  }

  public PropertyTestChildren getCHILD()
  {
    return pit.getValue(CHILD);
  }

  public void setCHILD(PropertyTestChildren pCHILD)
  {
    pit.setValue(CHILD, pCHILD);
  }


  @Override
  public IPropertyPit<TProperty> getPit()
  {
    return pit;
  }
}