package de.adito.propertly.core.common;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.serialization.*;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.*;
import org.junit.*;

import java.awt.*;

/**
 * @author j.boesl, 27.02.15
 */
public class SerializiationTest
{

  @Test
  public void simple()
  {
    IHierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new TProperty()));
    TProperty tProperty = hierarchy.getValue();

    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1", null).setValue(Color.BLACK);
    children.addProperty(Color.class, "color2", null).setValue(Color.RED);

    Serializer<MapSerializationProviderMap> serializer = Serializer.create(new MapSerializationProvider());


    MapSerializationProviderMap serialize = serializer.serialize(hierarchy);
    IPropertyPitProvider deserialize = serializer.deserialize(serialize);

    Assert.assertEquals(PropertlyDebug.toTreeString(tProperty),
                        PropertlyDebug.toTreeString(deserialize));
  }

}
