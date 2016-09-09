package de.adito.propertly.preset;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.test.core.impl.*;
import org.junit.*;

import java.awt.*;

/**
 * @author j.boesl, 09.09.16
 */
public class Test_PresetPropertiesHierarchy
{

  @Test
  public void simple()
  {
    IHierarchy<PresetPPP> hierarchy = new PresetPropertiesHierarchy<>(new Hierarchy<>("root", new PresetPPP()));
    PresetPPP ppp = hierarchy.getValue();

    Assert.assertEquals(Integer.valueOf(10), ppp.getValue(IComponent.X));
    Assert.assertEquals(Integer.valueOf(10), ppp.getValue(IComponent.Y));
    Assert.assertEquals(Integer.valueOf(320), ppp.getValue(IComponent.WIDTH));
    Assert.assertEquals(Integer.valueOf(200), ppp.getValue(IComponent.HEIGHT));
    Assert.assertNotNull(ppp.getValue(ITest.CHILD));
    Assert.assertEquals(Color.RED, ppp.getValue(PresetPPP.COLOR));
  }

}