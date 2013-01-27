package de.verpalnt.propertly.core.test;

import de.verpalnt.propertly.core.IPropertyDescription;
import de.verpalnt.propertly.core.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:41
 */
public interface ITest
{

  public static final IPropertyDescription<ITest, PropertyTestChildren> CHILD = PD.create(ITest.class);

}
