package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.api.PropertyDescription;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.test.core.impl.DynamicTestPropertyPitProvider;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        PropertyDescription.create(DynamicTestPropertyPitProvider.class, Color.class, "pink" /*annotations*/);
    property = root.addProperty(0, pinkDescription);
    property.setValue(Color.PINK);
    propertyList.add(0, property);

    property = root.addProperty(Color.class, "green");
    property.setValue(Color.green);
    propertyList.add(property);

    IPropertyDescription<DynamicTestPropertyPitProvider, Color> blackDescription =
        PropertyDescription.create(DynamicTestPropertyPitProvider.class, Color.class, "black" /*annotations*/);
    property = root.addProperty(3, blackDescription);
    property.setValue(Color.black);
    propertyList.add(3, property);

    Assert.assertEquals(propertyList, root.getProperties());


    Comparator<IProperty<DynamicTestPropertyPitProvider, Color>> comparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
    Collections.sort(propertyList, comparator);
    root.reorder(comparator);

    Assert.assertEquals(propertyList, root.getProperties());


    for (Iterator<IProperty<DynamicTestPropertyPitProvider, Color>> i = propertyList.iterator(); i.hasNext(); )
    {
      IProperty<DynamicTestPropertyPitProvider, Color> next = i.next();
      if (Arrays.<IPropertyDescription>asList(pinkDescription, blackDescription).contains(next.getDescription()))
        i.remove();
    }
    root.removeProperty(pinkDescription);
    root.removeProperty(blackDescription);

    Assert.assertEquals(propertyList, root.getProperties());
  }

}
