package de.adito.propertly.test.core;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.AbstractIndexedMutablePPP;
import de.adito.propertly.test.core.impl.DynamicTestPropertyPitProvider;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author j.boesl, 02.12.14
 */
public class SimpleMutableTest
{

  @Test
  public void TestMutable()
  {
    DynamicTestPropertyPitProvider root =
        new Hierarchy<>("root", new DynamicTestPropertyPitProvider()).getValue();

    List<IProperty<DynamicTestPropertyPitProvider, Color>> propertyList = new ArrayList<>();


    IProperty<DynamicTestPropertyPitProvider, Color> property;

    property = root.addProperty(Color.RED);
    propertyList.add(property);

    property = root.addProperty("blue", Color.BLUE);
    propertyList.add(property);

    IPropertyDescription<DynamicTestPropertyPitProvider, Color> pinkDescription =
        new PropertyDescription<>(DynamicTestPropertyPitProvider.class, Color.class, "pink");
    property = root.addProperty(0, pinkDescription);
    property.setValue(Color.PINK);
    propertyList.add(0, property);

    property = root.addProperty(Color.class, "green");
    property.setValue(Color.green);
    propertyList.add(property);

    IPropertyDescription<DynamicTestPropertyPitProvider, Color> blackDescription =
        new PropertyDescription<>(DynamicTestPropertyPitProvider.class, Color.class, "black");
    property = root.addProperty(3, blackDescription);
    property.setValue(Color.black);
    propertyList.add(3, property);

    assertEquals(propertyList, root.getProperties());


    Comparator<IProperty<DynamicTestPropertyPitProvider, Color>> comparator = Comparator.comparing(IProperty::getName);
    propertyList.sort(comparator);
    root.reorder(comparator);

    assertEquals(propertyList, root.getProperties());


    propertyList.removeIf(
        next -> Arrays.<IPropertyDescription>asList(pinkDescription, blackDescription).contains(next.getDescription()));
    root.removeProperty(pinkDescription);
    root.removeProperty(blackDescription);

    assertEquals(propertyList, root.getProperties());
  }

  @Test
  public void testDescription()
  {
    _PPP pit = new Hierarchy<>("", new _PPP()).getValue();

    IProperty<_PPP, String> fixedProperty = pit.getProperty(_PPP.fixedProperty);
    IProperty<_PPP, String> dynamicProperty = pit.addProperty("DynamicProperty");

    assertEquals(_PPP.class, fixedProperty.getDescription().getSourceType());
    assertEquals(_PPP.class, dynamicProperty.getDescription().getSourceType());
  }

  public static class _PPP extends AbstractIndexedMutablePPP<IPropertyPitProvider, _PPP, Object>
  {
    public static final IPropertyDescription<_PPP, String> fixedProperty = PD.create(_PPP.class);

    public _PPP()
    {
      super(Object.class);
    }
  }

}
