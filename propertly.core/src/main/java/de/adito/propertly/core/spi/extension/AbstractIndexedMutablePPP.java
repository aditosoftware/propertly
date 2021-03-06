package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.api.PitFactory;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:30
 */
public abstract class AbstractIndexedMutablePPP
    <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends AbstractMutablePropertyPitProviderBase<P, S, T>
    implements IIndexedMutablePropertyPitProvider<P, S, T>
{

  private final IIndexedMutablePropertyPit<P, S, T> pit;

  public AbstractIndexedMutablePPP(Class<T> pAllowedChildType)
  {
    this(PitFactory.getInstance(), pAllowedChildType);
  }

  protected AbstractIndexedMutablePPP(IIndexedMutablePropertyPitFactory pFactory, Class<T> pAllowedChildType)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this, pAllowedChildType);
  }

  @NotNull
  @Override
  public final IIndexedMutablePropertyPit<P, S, T> getPit()
  {
    return pit;
  }

  public int getSize()
  {
    return pit.getSize();
  }

  @NotNull
  public IProperty<S, T> getProperty(int pIndex)
  {
    return pit.getProperty(pIndex);
  }

  public void removeProperty(int pIndex)
  {
    pit.removeProperty(pIndex);
  }

  @NotNull
  public IProperty<S, T> addProperty(int pIndex, IPropertyDescription<S, T> pPropertyDescription)
  {
    return pit.addProperty(pIndex, pPropertyDescription);
  }

  public int indexOf(@NotNull IPropertyDescription<?, ?> pPropertyDescription)
  {
    return pit.indexOf(pPropertyDescription);
  }

  public void reorder(Comparator<IProperty<S, T>> pComparator)
  {
    pit.reorder(pComparator);
  }

}

