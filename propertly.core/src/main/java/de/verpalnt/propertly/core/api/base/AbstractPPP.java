package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.hierarchy.HierarchyPitFactory;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:32
 */
public abstract class AbstractPPP<S extends IPropertyPitProvider<S>>
    extends AbstractPropertyPitProviderBase<S>
{
  private final IPropertyPit<S> pit;

  public AbstractPPP()
  {
    this(HierarchyPitFactory.getInstance());
  }

  protected AbstractPPP(IPropertyPitFactory pFactory)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this);
  }

  @Override
  public final IPropertyPit<S> getPit()
  {
    return pit;
  }

}
