package de.verpalnt.propertly;

import de.verpalnt.propertly.listener.IPropertyEvent;
import de.verpalnt.propertly.listener.IPropertyEventListener;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S> implements IPropertyPit<S>
{

  private final Map<IPropertyDescription<? super S, ?>, IProperty<? super S, ?>> properties;

  private List<IPropertyEventListener> listenerList;

  protected PropertyPit()
  {
    properties = new LinkedHashMap<IPropertyDescription<? super S, ?>, IProperty<? super S, ?>>();
    List descriptions = _getProperties(this);
    //noinspection unchecked
    for (IPropertyDescription<S, ?> descr : (List<IPropertyDescription<S, ?>>) descriptions)
      //noinspection unchecked
      properties.put(descr, new _Property(descr));
  }

  private PropertyPit(List<IPropertyDescription<? super S, ?>> pProperties)
  {
    properties = new LinkedHashMap<IPropertyDescription<? super S, ?>, IProperty<? super S, ?>>();
    for (IPropertyDescription<? super S, ?> descr : pProperties)
      //noinspection unchecked
      properties.put(descr, new _Property(descr));
  }

  public static <S> IPropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(_getProperties(pCreateFor));
  }

  private static <S> List<IPropertyDescription<? super S, ?>> _getProperties(S pCreateFor)
  {
    List<IPropertyDescription<? super S, ?>> propertyDescriptions = new ArrayList<IPropertyDescription<? super S, ?>>();
    for (Field field : Util.getAllFields(pCreateFor.getClass()))
    {
      try
      {
        field.setAccessible(true);
        if (IPropertyDescription.class.isAssignableFrom(field.getType()))
        {
          @SuppressWarnings("unchecked")
          IPropertyDescription<? super S, ?> propertyDescription = (IPropertyDescription<? super S, ?>) field.get(pCreateFor);
          propertyDescriptions.add(propertyDescription);
        }
      }
      catch (IllegalAccessException e)
      {
        // skip
      }
    }
    return propertyDescriptions;
  }

  @Override
  public <SOURCE, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    //noinspection SuspiciousMethodCalls,unchecked
    return (IProperty<SOURCE, T>) properties.get(pPropertyDescription);
  }

  @Override
  public <SOURCE, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    IProperty<SOURCE, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    return property;
  }

  @Override
  public <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  public <T> void setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    getProperty(pPropertyDescription).setValue(pValue);
  }

  @Override
  public Set<IPropertyDescription<? super S, ?>> getProperties()
  {
    return Collections.unmodifiableSet(properties.keySet());
  }

  @Override
  public synchronized void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList == null)
      listenerList = new ArrayList<IPropertyEventListener>();
    listenerList.add(pListener);
  }

  @Override
  public synchronized void removePropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList != null)
    {
      if (listenerList.remove(pListener) && listenerList.isEmpty())
        listenerList = null;
    }
  }

  public synchronized List<IPropertyEventListener> _getListeners()
  {
    if (listenerList == null)
      return Collections.emptyList();
    return new ArrayList<IPropertyEventListener>(listenerList);
  }


  /**
   * PropertyImpl
   */
  private class _Property<S, T> implements IProperty<S, T>
  {
    private IPropertyDescription<S, T> propertyDescription;
    private T value;

    private _Property(IPropertyDescription<S, T> propertyDescription)
    {
      this.propertyDescription = propertyDescription;
    }

    @Override
    public IPropertyDescription<S, T> getDescription()
    {
      return propertyDescription;
    }

    @Override
    public T getValue()
    {
      return value;
    }

    @Override
    public void setValue(T pValue)
    {
      final T oldValue = value;
      value = pValue;
      IPropertyEvent<S, T> evt = new IPropertyEvent<S, T>()
      {
        @Override
        public IProperty<S, T> getProperty()
        {
          return _Property.this;
        }

        @Override
        public T oldValue()
        {
          return oldValue;
        }
      };
      //new PropertyChangeEvent(this, getDescription().getName(), oldValue, pValue);
      for (IPropertyEventListener propertyEventListener : _getListeners())
        propertyEventListener.propertyChange(evt);
    }

    @Override
    public String toString()
    {
      return "_Property@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + value + '}';
    }
  }

}
