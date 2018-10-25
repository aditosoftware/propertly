package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.common.annotations.PropertlyOverride;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractPPP;

import java.awt.*;
import java.util.Map;

/**
 * @author PaL
 *         Date: 26.11.12
 *         Time: 21:39
 */
public class TProperty extends AbstractPPP<IPropertyPitProvider, TProperty, Object>
    implements ITest<TProperty>, IComponent<IPropertyPitProvider, TProperty>
{

  @PropertlyOverride
  @IntVerifier(minValue = 0)
  public static final IPropertyDescription<TProperty, Integer> X = PD.create(TProperty.class);

  @PropertlyOverride
  @IntVerifier(minValue = 0)
  public static final IPropertyDescription<TProperty, Integer> Y = PD.create(TProperty.class);

  public static final IPropertyDescription<TProperty, Dimension> FF = PD.create(TProperty.class);

  public static final IPropertyDescription<TProperty, Map<String, String>> MAP = PD.create(TProperty.class);


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

  public IProperty<TProperty, PropertyTestChildren> getPropertyCHILD()
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