package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.AbstractIndexedMutablePPP;
import org.junit.*;

/**
 * @author W.Glanzer, 10.10.2016
 */
public class HierarchyTest
{

  private Hierarchy<MPPP> hierarchy;

  @Before
  public void setUp() throws Exception
  {
    hierarchy = new Hierarchy<>("myHierarchy", new MPPP());
  }

  @Test
  public void test_rename() throws Exception
  {
    IProperty<IPropertyPitProvider, MPPP> hierProp = hierarchy.getProperty();
    hierProp.rename("myNewName");
    Assert.assertEquals("myNewName", hierProp.getName());
  }

  @Test
  public void test_renameOnClone() throws Exception
  {
    IProperty<MPPP, String> stringProp = hierarchy.getValue().getPit().addProperty("stringProp", "value");

    Hierarchy<MPPP> copied = new Hierarchy<>(hierarchy.getProperty().getName(), hierarchy.getValue());
    IIndexedMutablePropertyPit<IPropertyPitProvider, MPPP, Object> pit = copied.getValue().getPit();
    IProperty<MPPP, String> copiedStringProperty = pit.getProperty(stringProp.getDescription());

    copiedStringProperty.rename("renamedCopiedStringProp");
    Assert.assertEquals("stringProp", stringProp.getName());
    Assert.assertEquals("renamedCopiedStringProp", copiedStringProperty.getName());
  }

  @After
  public void tearDown() throws Exception
  {
    hierarchy = null;
  }

  /**
   * MutablePPP-Impl
   */
  public static class MPPP extends AbstractIndexedMutablePPP<IPropertyPitProvider, MPPP, Object>
  {
    public MPPP()
    {
      super(Object.class);
    }
  }
}
