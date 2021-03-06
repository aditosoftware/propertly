package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author W.Glanzer, 31.03.2017
 */
public class DynamicPropertiesTest
{
  private Model._PropertyContainer container;

  @BeforeEach
  public void setUp() throws Exception
  {
    // Initialize Models
    Model rootModel = new Hierarchy<>("rootModel", new Model()).getValue();
    container = rootModel.getProperty(Model.myProperty).setValue(new Model._PropertyContainer());
  }

  @Test
  public void test_addPropertyWithDescription() throws Exception
  {
    PropertyDescription<Model._PropertyContainer, _AbstractProperty> propDesc = new PropertyDescription<>(Model._PropertyContainer.class, _AbstractProperty.class, "property");
    container.addProperty(propDesc).setValue(new Property1());
    assertNotNull(container.findProperty(propDesc));

    try
    {
      container.addProperty(propDesc).setValue(new Property2());
      fail(); // We expect an exception - duplicate name
    }
    catch (Exception ignored)
    {
    }
  }

  @Test
  public void test_addPropertyWODescription() throws Exception
  {
    container.addProperty("property1", new Property1());
    assertNotNull(container.findProperty("property1"));

    try
    {
      container.addProperty("property1", new Property2());
      fail(); // We expect an exception - duplicate name
    }
    catch (Exception ignored)
    {
    }

    assertEquals(1, container.getProperties().size());
  }

  public static class Model extends AbstractPPP<IPropertyPitProvider, Model, Object>
  {
    public static final IPropertyDescription<Model, _PropertyContainer> myProperty = PD.create(Model.class);

    public static class _PropertyContainer extends AbstractMutablePPP<Model, _PropertyContainer, _AbstractProperty>
    {
      public _PropertyContainer()
      {
        super(_AbstractProperty.class);
      }
    }
  }

  public static class Property1 extends _AbstractProperty
  {
  }

  public static class Property2 extends _AbstractProperty
  {
  }

  public abstract static class _AbstractProperty extends AbstractPPP<Model, _AbstractProperty, Object>
  {
  }

}
