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

  public static final IPropertyDescription<IComponent, Integer> WIDTH = PD.create(IComponent.class);
  public static final IPropertyDescription<IComponent, Integer> HEIGHT = PD.create(IComponent.class);

}
