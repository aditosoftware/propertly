package de.adito.propertly.core.common;

import javax.annotation.Nonnull;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A listener list implementation that can handle weak and strong listeners.
 *
 * @author PaL
 *         Date: 11.01.15
 *         Time. 03:15
 */
public class ListenerList<T> implements Iterable<T>
{
  private List<Object> listeners;

  @Override
  public Iterator<T> iterator()
  {
    return getListeners().iterator();
  }

  public List<T> getListeners()
  {
    List<T> l = new ArrayList<>();
    if (listeners != null)
    {
      for (Object o : listeners)
      {
        if (o instanceof Reference)
          o = ((Reference) o).get();
        if (o != null)
          //noinspection unchecked
          l.add((T) o);
      }
      _cleanUp();
    }
    return l;
  }

  public void addWeakListener(@Nonnull T pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<>();
    listeners.add(new WeakReference<>(pListener));
    _cleanUp();
  }

  public void addStrongListener(@Nonnull T pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<>();
    listeners.add(pListener);
    _cleanUp();
  }

  public void removeListener(@Nonnull T pListener)
  {
    if (listeners != null)
    {
      for (Iterator i = listeners.iterator(); i.hasNext(); )
      {
        Object next = i.next();
        if (next instanceof Reference)
          next = ((Reference) next).get();
        if (pListener.equals(next))
          i.remove();
      }
      _cleanUp();
    }
  }

  private void _cleanUp()
  {
    for (Iterator i = listeners.iterator(); i.hasNext(); )
    {
      Object next = i.next();
      if (next instanceof Reference)
        next = ((Reference) next).get();
      if (next == null)
        i.remove();
    }
  }

}
