package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.hierarchy.HierarchyPitFactory;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:32
 */
public abstract class AbstractPPP<S extends IPropertyPitProvider<S, Object>>
    extends AbstractPropertyPitProviderBase<S, Object>
{
  private final IPropertyPit<S, Object> pit;

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
  public final IPropertyPit<S, Object> getPit()
  {
    return pit;
  }

}
