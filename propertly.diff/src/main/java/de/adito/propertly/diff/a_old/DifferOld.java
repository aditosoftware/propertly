package de.adito.propertly.diff.a_old;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import difflib.DiffUtils;
import difflib.Patch;

import java.util.List;

/**
 * @author j.boesl, 10.07.15
 */
public class DifferOld
{

  public static Patch<Object> diff(IPropertyPitProvider<?, ?, ?> pPpp1, IPropertyPitProvider<?, ?, ?> pPpp2)
  {
    FlatPropertlyView fpv1 = new FlatPropertlyView(pPpp1);
    FlatPropertlyView fpv2 = new FlatPropertlyView(pPpp2);
    List<Object> flat1 = fpv1.toFlat();
    List<Object> flat2 = fpv2.toFlat();
    Patch<Object> diff = DiffUtils.diff(flat1, flat2);
    return diff;
  }

}
