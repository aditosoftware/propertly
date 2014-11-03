package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:44
 */
public abstract class AbstractMutablePPP<S extends IMutablePropertyPitProvider, T>
    extends AbstractMutablePropertyPitProviderBase<S, T>
{
  private final IMutablePropertyPit<S, T> pit;

  protected AbstractMutablePPP(IMutablePropertyPitFactory pFactory, Class<T> pAllowedChildType)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this, pAllowedChildType);
  }

  @Override
  public final IMutablePropertyPit<S, T> getPit()
  {
    return pit;
  }
}
