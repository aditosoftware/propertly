package de.adito.propertly.core.api.base;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.hierarchy.HierarchyPitFactory;

import javax.annotation.Nonnull;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:32
 */
public abstract class AbstractPPP<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends AbstractPropertyPitProviderBase<P, S, T>
{
  private final IPropertyPit<P, S, T> pit;

  public AbstractPPP()
  {
    this(HierarchyPitFactory.getInstance());
  }

  protected AbstractPPP(IPropertyPitFactory pFactory)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this);
  }

  @Nonnull
  @Override
  public final IPropertyPit<P, S, T> getPit()
  {
    return pit;
  }

}
