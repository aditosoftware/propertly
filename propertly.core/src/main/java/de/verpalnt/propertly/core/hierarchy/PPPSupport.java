package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IPropertyDescription;
import de.verpalnt.propertly.core.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PPPIntrospector;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 21:57
 */
public final class PPPSupport
{

  private Class<? extends IPropertyPitProvider> pppCls;
  private Reference<IPropertyPitProvider> ppp;

  public PPPSupport(Class<? extends IPropertyPitProvider> pPppCls)
  {
    pppCls = pPppCls;
  }

  public Set<IPropertyDescription> getDescriptions()
  {
    return PPPIntrospector.get(pppCls);
  }

  public IPropertyPitProvider getPPP()
  {
    IPropertyPitProvider p;
    if (ppp != null)
    {
      p = ppp.get();
      if (p != null)
        return p;
    }
    p = _create();
    ppp = new WeakReference<IPropertyPitProvider>(p);
    return p;
  }

  private IPropertyPitProvider _create()
  {
    try
    {
      return pppCls.newInstance();
    }
    catch (InstantiationException e)
    {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
  }

}
