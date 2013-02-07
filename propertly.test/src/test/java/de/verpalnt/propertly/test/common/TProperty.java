package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.AbstractPPP;
import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.common.PD;

import java.awt.*;

/**
 * @author PaL
 *         Date: 26.11.12
 *         Time: 21:39
 */
public class TProperty extends AbstractPPP<TProperty> implements ITest, IComponent
{

  public static final IPropertyDescription<TProperty, Integer> X = PD.create(TProperty.class);
  public static final IPropertyDescription<TProperty, Integer> Y = PD.create(TProperty.class);
  public static final IPropertyDescription<TProperty, Dimension> FF = PD.create(TProperty.class);


  public IProperty<TProperty, Integer> getPropertyX()
  {
    return getProperty(X);
  }

  public Integer getX()
  {
    return getPit().getValue(X);
  }

  public void setX(Integer pX)
  {
    setValue(X, pX);
  }

  public IProperty<TProperty, Integer> getPropertyY()
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

  public IProperty<TProperty, Dimension> getPropertyFF()
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

  public PropertyTestChildren setCHILD(PropertyTestChildren pCHILD)
  {
    return setValue(CHILD, pCHILD);
  }

}