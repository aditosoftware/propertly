package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.common.annotations.PropertlyOverride;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import de.adito.propertly.test.core.impl.*;
import org.junit.jupiter.api.*;

/**
 * @author j.boesl, 20.11.20
 */
public class DefaultValueTest
{

  @Test
  public void test()
  {
    DefaultValuePPP dfPPP = new Hierarchy<>("test", new DefaultValuePPP()).getValue();
    Assertions.assertEquals(DefaultValuePPP.K, dfPPP.getProperty(DefaultValuePPP.K).getDescription());
    Assertions.assertEquals(Integer.valueOf(1), DefaultValuePPP.K.getDefaultValue());
    Assertions.assertEquals(Integer.valueOf(2), DefaultValuePPP.L.getDefaultValue());

    PropertyTestChildren propertyTestChildren = dfPPP.setValue(DefaultValuePPP.CHILD, DefaultValuePPP.CHILD.getDefaultValue());
    Assertions.assertNotEquals(DefaultValuePPP.CHILD.getDefaultValue(), propertyTestChildren);
  }

  public static class DefaultValuePPP extends AbstractPPP<IPropertyPitProvider, DefaultValuePPP, Object>
      implements ITest<DefaultValuePPP>, IComponent<IPropertyPitProvider, DefaultValuePPP>
  {
    @PropertlyOverride
    public static final IPropertyDescriptionDV<DefaultValuePPP, PropertyTestChildren> CHILD = PD.create(DefaultValuePPP.class, new PropertyTestChildren());

    public static final IPropertyDescriptionDV<DefaultValuePPP, Integer> K = PD.create(DefaultValuePPP.class, 1);
    public static final IPropertyDescriptionDV<DefaultValuePPP, Integer> L = PD.create(DefaultValuePPP.class, 2);
  }

}
