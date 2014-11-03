package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:32
 */
public abstract class AbstractPPP<S extends IPropertyPitProvider>
    extends AbstractPropertyPitProviderBase<S>
{
  private final IPropertyPit<S> pit;

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
