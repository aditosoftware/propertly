package de.adito.propertly.core.common;

import de.adito.propertly.core.common.exception.InitializationException;
import de.adito.propertly.core.common.exception.WrongModifiersException;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import org.junit.Test;

public class PPPIntrospectorTest
{

  @Test(expected = WrongModifiersException.class)
  public void testWrongModifier()
  {
    PPPIntrospector.get(WRONG_MODIFIER_PIT.class);
  }

  private static class WRONG_MODIFIER_PIT extends AbstractPPP<IPropertyPitProvider, WRONG_MODIFIER_PIT, Object>
  {
    IPropertyDescription<WRONG_MODIFIER_PIT, String> PROPERTY = PD.create(WRONG_MODIFIER_PIT.class);
  }

  @Test(expected = InitializationException.class)
  public void testWrongInit() throws Throwable
  {
    try
    {
      PPPIntrospector.get(WRONG_INIT_PIT.class);
    }
    catch (ExceptionInInitializerError e)
    {
      throw e.getException();
    }
  }

  private static class WRONG_INIT_PIT extends AbstractPPP<IPropertyPitProvider, WRONG_MODIFIER_PIT, Object>
  {
    public static final IPropertyDescription<WRONG_MODIFIER_PIT, String> PROPERTY = PD.create(WRONG_MODIFIER_PIT.class);
  }

  @Test
  public void testProperInit()
  {
    PPPIntrospector.get(PROPER_PIT.class);
  }

  private static class PROPER_PIT extends AbstractPPP<IPropertyPitProvider, PROPER_PIT, Object>
  {
    public static final IPropertyDescription<PROPER_PIT, String> PROPERTY = PD.create(PROPER_PIT.class);
  }

}