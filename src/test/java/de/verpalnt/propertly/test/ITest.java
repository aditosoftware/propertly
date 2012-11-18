package de.verpalnt.propertly.test;

import de.verpalnt.propertly.IPropertyDescription;
import de.verpalnt.propertly.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:41
 */
public interface ITest
{

  public static final IPropertyDescription<ITest, IComponent> CHILD = PD.create(ITest.class);

}
