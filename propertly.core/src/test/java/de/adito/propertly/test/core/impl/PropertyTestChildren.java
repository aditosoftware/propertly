package de.adito.propertly.test.core.impl;


import de.adito.propertly.core.api.base.AbstractMutablePPP;

import java.awt.*;

/**
 * @author PaL
 *         Date: 18.11.12
 *         Time: 23:43
 */
public class PropertyTestChildren extends AbstractMutablePPP<ITest, PropertyTestChildren, Color>
{
  public PropertyTestChildren()
  {
    super(Color.class);
  }
}
