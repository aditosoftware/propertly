package de.adito.propertly.preset;

import de.adito.propertly.core.common.annotations.PropertlyOverride;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import de.adito.propertly.test.core.impl.*;

import java.awt.*;

/**
 * @author j.boesl, 09.09.16
 */
public class PresetPPP extends AbstractPPP<IPropertyPitProvider, PresetPPP, Object>
    implements ITest<PresetPPP>, IComponent<IPropertyPitProvider, PresetPPP>
{

  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, Integer> X = PDP.create(PresetPPP.class, 10);
  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, Integer> Y = PDP.create(PresetPPP.class, 10);
  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, Integer> WIDTH = PDP.create(PresetPPP.class, 320);
  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, Integer> HEIGHT = PDP.create(PresetPPP.class, 200);

  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, String> DESCRIPTION = PDP.create(PresetPPP.class, "<empty>");

  @PropertlyOverride
  public static final IPropertyDescription<PresetPPP, PropertyTestChildren> CHILD = PDP.createWithPresetFromType(PresetPPP.class);

  public static final IPropertyDescription<PresetPPP, Color> COLOR = PDP.create(PresetPPP.class, Color.RED);

}
