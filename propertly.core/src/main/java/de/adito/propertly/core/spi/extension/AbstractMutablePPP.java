package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.api.PitFactory;
import de.adito.propertly.core.spi.IMutablePropertyPit;
import de.adito.propertly.core.spi.IMutablePropertyPitFactory;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:44
 */
public abstract class AbstractMutablePPP
    <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends AbstractMutablePropertyPitProviderBase<P, S, T>
{
  private final IMutablePropertyPit<P, S, T> pit;

  public AbstractMutablePPP(Class<T> pAllowedChildType)
  {
    this(PitFactory.getInstance(), pAllowedChildType);
  }

  protected AbstractMutablePPP(IMutablePropertyPitFactory pFactory, Class<T> pAllowedChildType)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this, pAllowedChildType);
  }

  @NotNull
  @Override
  public final IMutablePropertyPit<P, S, T> getPit()
  {
    return pit;
  }
}
