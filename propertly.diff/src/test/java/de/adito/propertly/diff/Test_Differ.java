package de.adito.propertly.diff;

import de.adito.propertly.core.spi.*;
import org.junit.Test;

/**
 * @author j.boesl, 10.07.15
 */
public class Test_Differ
{

  @Test
  public void run()
  {
    IPropertyPitProvider ppp1 = RandomPit.create();
    IPropertyPitProvider ppp2 = RandomPit.create();

    Patch patch = Differ.diff(ppp1, ppp2);

    IHierarchy hierarchy = ppp1.getPit().getHierarchy();
    patch.apply(hierarchy);

    patch = Differ.diff(hierarchy.getValue(), ppp2);

    System.out.println(patch);
  }

}
