package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PD;
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
    Hierarchy<MPPP> clonedHierarchy = new Hierarchy<>(hierarchy.getProperty().getName(), hierarchy.getValue());
    IProperty<MPPP, String> stringProp = hierarchy.getValue().getPit().getProperty(MPPP.myStringProp);
    IProperty<MPPP, String> clonedStringProp = clonedHierarchy.getValue().getPit().getProperty(MPPP.myStringProp);
    clonedStringProp.rename("renamedCloneStringProp");
    Assert.assertEquals("myStringProp", stringProp.getName());
    Assert.assertEquals("renamedCloneStringProp", clonedStringProp.getName());
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
    public static final IPropertyDescription<MPPP, String> myStringProp = PD.create(MPPP.class);
    public static final IPropertyDescription<MPPP, Integer> myIntProp = PD.create(MPPP.class);

    public MPPP()
    {
      super(Object.class);
    }
  }
}
