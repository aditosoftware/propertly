package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.base.AbstractIndexedMutablePPP;

import java.awt.*;

/**
 * @author j.boesl, 02.12.14
 */
public class DynamicTestPropertyPitProvider extends AbstractIndexedMutablePPP<DynamicTestPropertyPitProvider, Color>
{
  public DynamicTestPropertyPitProvider()
  {
    super(Color.class);
  }
}
