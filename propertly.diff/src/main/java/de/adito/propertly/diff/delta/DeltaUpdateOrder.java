package de.adito.propertly.diff.delta;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.data.*;

import java.util.*;

/**
 * @author j.boesl, 04.08.15
 */
public class DeltaUpdateOrder extends Delta
{
  private IPropertyPath parentPath;
  private int[] newOrder;

  public DeltaUpdateOrder(IPropertyPath pParentPath, int[] pNewOrder)
  {
    parentPath = pParentPath;
    newOrder = pNewOrder;
  }

  @Override
  public void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo)
  {
    IProperty<?, ?> property = parentPath.find(pTo);
    IIndexedMutablePropertyPit pit = ((IIndexedMutablePropertyPitProvider) property.getValue()).getPit();
    List<IProperty> properties = pit.getProperties();
    final Map<IProperty, Integer> orderMap = new HashMap<IProperty, Integer>();
    for (int i = 0; i < properties.size(); i++)
      orderMap.put(properties.get(i), newOrder[i]);

    pit.reorder(new Comparator<IProperty>()
    {
      @Override
      public int compare(IProperty o1, IProperty o2)
      {
        return orderMap.get(o1) - orderMap.get(o2);
      }
    });
  }

}
