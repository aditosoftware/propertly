package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractIndexedMutablePPP;

import java.awt.*;

/**
 * @author j.boesl, 02.12.14
 */
public class DynamicTestPropertyPitProvider extends AbstractIndexedMutablePPP<IPropertyPitProvider, DynamicTestPropertyPitProvider, Color>
{
  public DynamicTestPropertyPitProvider()
  {
    super(Color.class);
  }
}
