//package de.adito.propertly.diff;
//
//import de.adito.propertly.core.spi.*;
//
//import java.util.*;
//
///**
// * @author j.boesl, 10.07.15
// */
//public class PropertlyPathList extends AbstractSequentialList<Object>
//{
//
//  private IProperty<?, ?> baseProperty;
//  private static final Object TERMINATOR = new Object();
//  private static final Object SOMETHING_FOR_NOTHING = new Object();
//
//
//  public PropertlyPathList(IHierarchy<?> pHierarchy)
//  {
//    this(pHierarchy.getValue());
//  }
//
//  public PropertlyPathList(IPropertyPitProvider<?, ?, ?> pPpp)
//  {
//    this(pPpp.getPit().getOwnProperty());
//  }
//
//  public PropertlyPathList(IProperty<?, ?> pProperty)
//  {
//    baseProperty = pProperty;
//  }
//
//  private void _toFlat(IProperty<?, ?> pProperty, List<Object> pList)
//  {
//    IPropertyDescription<?, ?> description = pProperty.getDescription();
//    Object value = pProperty.getValue();
//
//    pList.add(description);
//    if (value instanceof IPropertyPitProvider || IPropertyPitProvider.class.isAssignableFrom(description.getType()))
//    {
//      if (value != null)
//        for (IProperty<?, ?> prop : ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties())
//          _toFlat(prop, pList);
//      pList.add(TERMINATOR);
//    }
//    else
//      pList.add(value == null ? SOMETHING_FOR_NOTHING : value);
//  }
//
//  @Override
//  public ListIterator<Object> listIterator(int index)
//  {
//    return new ListIterator<Object>()
//    {
//      @Override
//      public boolean hasNext()
//      {
//        return false;
//      }
//
//      @Override
//      public Object next()
//      {
//        return null;
//      }
//
//      @Override
//      public boolean hasPrevious()
//      {
//        return false;
//      }
//
//      @Override
//      public Object previous()
//      {
//        return null;
//      }
//
//      @Override
//      public int nextIndex()
//      {
//        return 0;
//      }
//
//      @Override
//      public int previousIndex()
//      {
//        return 0;
//      }
//
//      @Override
//      public void remove()
//      {
//
//      }
//
//      @Override
//      public void set(Object e)
//      {
//
//      }
//
//      @Override
//      public void add(Object e)
//      {
//
//      }
//    }
//  }
//
//  @Override
//  public int size()
//  {
//    return 0;
//  }
//}
