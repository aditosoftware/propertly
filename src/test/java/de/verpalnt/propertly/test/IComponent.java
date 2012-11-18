package de.verpalnt.propertly.test;

import de.verpalnt.propertly.IPropertyDescription;
import de.verpalnt.propertly.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:55
 */
public interface IComponent
{

  public static final IPropertyDescription<PropertyTest, Integer> WIDTH = PD.create(PropertyTest.class);
  public static final IPropertyDescription<PropertyTest, Integer> HEIGHT = PD.create(PropertyTest.class);

}
