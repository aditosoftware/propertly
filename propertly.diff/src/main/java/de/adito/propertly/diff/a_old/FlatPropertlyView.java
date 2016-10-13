package de.adito.propertly.diff.a_old;

import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;

import java.util.*;

/**
 * @author j.boesl, 10.07.15
 */
public class FlatPropertlyView
{

  private IProperty<?, ?> baseProperty;


  public FlatPropertlyView(IHierarchy<?> pHierarchy)
  {
    this(pHierarchy.getValue());
  }

  public FlatPropertlyView(IPropertyPitProvider<?, ?, ?> pPpp)
  {
    this(pPpp.getPit().getOwnProperty());
  }

  public FlatPropertlyView(IProperty<?, ?> pProperty)
  {
    baseProperty = pProperty;
  }

  public List<Object> toFlat()
  {
    ArrayList<StructuralDiffElement> flat = new ArrayList<StructuralDiffElement>();
    _toFlat(baseProperty, flat, 0);
    Collections.sort(flat);
    return (List) flat;
  }

  private void _toFlat(IProperty<?, ?> pProperty, List<StructuralDiffElement> pList, int pIndex)
  {
    IPropertyPitProvider parent = pProperty.getParent();
    PropertyPath parentalPath = new PropertyPath(parent);
    IPropertyDescription parentalDescription = parent == null ? null : parent.getPit().getOwnProperty().getDescription();
    IPropertyDescription<?, ?> description = pProperty.getDescription();
    pList.add(new StructuralDiffElement(parentalPath, parentalDescription, description, pIndex));

    Object value = pProperty.getValue();
    if (value instanceof IPropertyPitProvider || IPropertyPitProvider.class.isAssignableFrom(description.getType()))
    {
      if (value != null)
      {
        List<? extends IProperty<?, ?>> properties = ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties();
        for (int i = 0; i < properties.size(); i++)
          _toFlat(properties.get(i), pList, i);
      }
    }
  }

  /*private void _toFlat(IProperty<?, ?> pProperty, List<Object> pList)
  {
    PropertyPath path = new PropertyPath(pProperty.getParent());
    IPropertyDescription<?, ?> description = pProperty.getDescription();
    Object value = pProperty.getValue();

    if (value instanceof IPropertyPitProvider || IPropertyPitProvider.class.isAssignableFrom(description.getType()))
    {
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.DESCRIPTION, description));
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.NODE, null));
      if (value != null)
        for (IProperty<?, ?> prop : ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties())
          _toFlat(prop, pList);
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.DESCRIPTION, description));
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.NODE_END, null));
    }
    else
    {
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.DESCRIPTION, description));
      pList.add(new PropertlyDiffElement(path, PropertlyDiffElement.EType.VALUE, value));
    }
  }*/

}
