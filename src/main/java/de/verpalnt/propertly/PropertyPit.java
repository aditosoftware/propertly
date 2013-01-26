package de.verpalnt.propertly;

import de.verpalnt.propertly.listener.IPropertyEventListener;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S> implements IPropertyPit<S>
{

  private IPropertyPitParentProvider parent;
  private final Map<IPropertyDescription, IProperty> properties = new LinkedHashMap<IPropertyDescription, IProperty>();
  private List<IPropertyEventListener> listenerList;

  protected PropertyPit()
  {
    _init(_getProperties(this));
  }

  private PropertyPit(List<IPropertyDescription> pProperties)
  {
    _init(pProperties);
  }

  private void _init(List<IPropertyDescription> pProperties)
  {
    for (IPropertyDescription description : pProperties)
      //noinspection unchecked
      properties.put(description, new PPProperty(description));
  }

  public static <S> IPropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(_getProperties(pCreateFor));
  }

  private static <S> List<IPropertyDescription> _getProperties(S pCreateFor)
  {
    List<IPropertyDescription> propertyDescriptions = new ArrayList<IPropertyDescription>();
    for (Field field : Util.getAllFields(pCreateFor.getClass()))
    {
      try
      {
        field.setAccessible(true);
        if (IPropertyDescription.class.isAssignableFrom(field.getType()))
        {
          IPropertyDescription<?, ?> propertyDescription = (IPropertyDescription) field.get(pCreateFor);
          boolean isParentalType = propertyDescription.getParentType().isAssignableFrom(pCreateFor.getClass());
          if (isParentalType)
            propertyDescriptions.add(propertyDescription);
          assert isParentalType;
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
  public final IPropertyPitProvider getParent()
  {
    return parent == null ? null : parent.get();
  }

  @Override
  public final <SOURCE, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    //noinspection unchecked
    return (IProperty<SOURCE, T>) properties.get(pPropertyDescription);
  }

  @Override
  public final <SOURCE, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    IProperty<SOURCE, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    return property;
  }

  @Override
  public final <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  public final <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    getProperty(pPropertyDescription).setValue(pValue);
    return pValue;
  }

  @Override
  public final Set<IPropertyDescription> getPropertyDescriptions()
  {
    return Collections.unmodifiableSet(properties.keySet());
  }


  @Override
  public List<IProperty> getProperties()
  {
    return Collections.unmodifiableList(new ArrayList<IProperty>(properties.values()));
  }

  @Override
  public final synchronized void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList == null)
      listenerList = new ArrayList<IPropertyEventListener>();
    listenerList.add(pListener);
  }

  @Override
  public final synchronized void removePropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList != null)
    {
      if (listenerList.remove(pListener) && listenerList.isEmpty())
        listenerList = null;
    }
  }

  @Override
  public IPropertyPit<S> getPit()
  {
    return this;
  }

  synchronized List<IPropertyEventListener> getListeners()
  {
    if (listenerList == null)
      return Collections.emptyList();
    return new ArrayList<IPropertyEventListener>(listenerList);
  }

  Map<IPropertyDescription, IProperty> getPropertyMap()
  {
    return properties;
  }


  /**
   * PropertyImpl
   */
  class PPProperty<S, T> implements IProperty<S, T>
  {
    private IPropertyDescription<S, T> propertyDescription;
    private AtomicReference<T> value = new AtomicReference<T>();

    PPProperty(IPropertyDescription<S, T> propertyDescription)
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
      return value.get();
    }

    @Override
    public T setValue(T pValue)
    {
      if (pValue instanceof IPropertyPitProvider)
      {
        IPropertyPit pit = ((IPropertyPitProvider) pValue).getPit();
        if (pit instanceof PropertyPit)
        {
          PropertyPit pp = (PropertyPit) pit;
          if (pp.parent != null)
            pp.parent.remove();
          pp.parent = new IPropertyPitParentProvider()
          {
            @Override
            public IPropertyPitProvider get()
            {
              return PropertyPit.this;
            }

            @Override
            public void remove()
            {
              setValue(null);
            }
          };
        }
        else
          throw new UnsupportedOperationException();
      }
      final T oldValue = value.getAndSet(pValue);
      if (pValue != oldValue && (pValue == null || !pValue.equals(oldValue)))
      {
        for (IPropertyEventListener propertyEventListener : getListeners())
          propertyEventListener.propertyChange(this, oldValue, pValue);
      }
      return pValue;
    }

    @Override
    public String toString()
    {
      return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + value.get() + '}';
    }
  }

}
