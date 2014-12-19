package de.adito.propertly.test.common;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:55
 */
public interface IComponent<P extends IPropertyPitProvider, S extends IComponent<P, S>> extends IPropertyPitProvider<P, S, Object>
{

  IPropertyDescription<IComponent, Integer> X = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> Y = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> WIDTH = PD.create(IComponent.class);
  IPropertyDescription<IComponent, Integer> HEIGHT = PD.create(IComponent.class);

}
