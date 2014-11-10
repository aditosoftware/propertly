package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:55
 */
public interface IComponent<T extends IComponent> extends IPropertyPitProvider<T>
{

  IPropertyDescription<IComponent, Integer> X = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> Y = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> WIDTH = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> HEIGHT = PD.create(IComponent.class);

}
