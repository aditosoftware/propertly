package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractMutablePPP;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * @author j.boesl, 13.03.18
 */
public class ListenerTest
{
  private IPropertyPitEventListener<PPP, Object> pel;

  @Test
  public void test()
  {
    PPP ppp = new Hierarchy<>("", new PPP()).getValue();
    pel = new PropertyPitEventAdapter<PPP, Object>()
    {
      @Override
      public void propertyAdded(@NotNull PPP pSource, @NotNull IPropertyDescription<PPP, Object> pPropertyDescription, @NotNull Set<Object> pAttributes)
      {
        Assert.fail();
      }
    };
    ppp.addWeakListener(pel);
    ppp.removeListener(pel);
    ppp.addProperty("test");
  }

  public static class PPP extends AbstractMutablePPP<IPropertyPitProvider, PPP, Object>
  {
    public PPP()
    {
      super(Object.class);
    }
  }
}