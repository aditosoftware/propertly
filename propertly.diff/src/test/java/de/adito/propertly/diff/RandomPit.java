package de.adito.propertly.diff;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.IIndexedMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractIndexedMutablePPP;

import java.util.Arrays;
import java.util.List;

/**
 * @author j.boesl, 12.07.15
 */
public class RandomPit
{

  public static IPropertyPitProvider create()
  {
    return create(9, 14);
  }

  public static IPropertyPitProvider create(int pDepthValue, int pLengthValue)
  {
    RandomPit rp = new RandomPit();

    IIndexedMutablePropertyPitProvider ppp =
        new Hierarchy<IIndexedMutablePropertyPitProvider>(rp._generateName(), new _Pit()).getValue();
    rp._createChildren(ppp, pDepthValue, pLengthValue);
    return ppp;
  }

  private void _createChildren(IMutablePropertyPitProvider<?, ?, Object> pPpp, int pDepth, int pLength)
  {
    int depth = new PropertyPath(pPpp).getPathElements().size();

    if (depth < pDepth) // add children
    {
      for (int i = 0; i < pLength; i++)
      {
        String name;
        while (true)
        {
          name = _generateName();
          if (pPpp.getPit().findProperty(name) == null)
            break;
        }
        IIndexedMutablePropertyPitProvider<?, ?, Object> childPit = pPpp.getPit().addProperty(name, new _Pit()).getValue();
        _createChildren(childPit, _rnd(pDepth), _rnd(pLength));
      }
    }
  }

  private int _rnd(int pValue)
  {
    return (int) (pValue * 0.7);
  }

  private String _generateName()
  {
    java.util.List<String> pre = Arrays.asList(
        "hairy", "frenzy", "nice", "strange", "dry", "steady", "fast", "poor", "dumb", "drunken");
    List<String> first = Arrays.asList(
        "Jones", "Homer", "Cowboy", "Apache", "Freezer", "Pal", "Bighead", "Master", "Scholar", "Judge", "Butcher");
    return _pickOne(pre) + _pickOne(first);
  }

  private String _pickOne(List<String> pChoices)
  {
    return pChoices.get((int) (pChoices.size() * Math.random()));
  }

  /**
   * PPP-Impl
   */
  public static class _Pit extends AbstractIndexedMutablePPP
  {
    public _Pit()
    {
      super(Object.class);
    }
  }

}
