package de.verpalnt.propertly.core;

import de.verpalnt.propertly.core.common.PPPIntrospector;
import de.verpalnt.propertly.core.listener.IPropertyEventListener;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPit<S>
{

  private S source;
  private IPropertyPitParentProvider parent;
  private final Map<IPropertyDescription, IProperty> properties = new LinkedHashMap<IPropertyDescription, IProperty>();
  private List<IPropertyEventListener> listenerList;

  protected PropertyPit()
  {
    _init(PPPIntrospector.get(getClass()));
    source = (S) this;
  }

  private PropertyPit(S pSource, List<IPropertyDescription> pProperties)
  {
    _init(pProperties);
    source = pSource;
  }

  private void _init(List<IPropertyDescription> pProperties)
  {
    for (IPropertyDescription description : pProperties)
      //noinspection unchecked
      properties.put(description, new PPProperty(description));
  }

  public static <S extends IPropertyPitProvider> IPropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(pCreateFor, PPPIntrospector.get(pCreateFor.getClass()));
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
    public void setValue(T pValue)
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
              return source;
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
    }

    @Override
    public String toString()
    {
      return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + value.get() + '}';
    }
  }

}
