package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.Nonnull;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:30
 */
public abstract class AbstractIndexedMutablePPP<S extends IIndexedMutablePropertyPitProvider, T>
    extends AbstractMutablePropertyPitProviderBase<S, T>
    implements IIndexedMutablePropertyPitProvider<S, T>
{

  private final IIndexedMutablePropertyPit<S, T> pit;


  protected AbstractIndexedMutablePPP(IIndexedMutablePropertyPitFactory pFactory, Class<T> pAllowedChildType)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this, pAllowedChildType);
  }

  @Override
  public final IIndexedMutablePropertyPit<S, T> getPit()
  {
    return pit;
  }

  public int getSize()
  {
    return pit.getSize();
  }

  @Nonnull
  public IProperty<S, T> getProperty(int pIndex)
  {
    return pit.getProperty(pIndex);
  }

  public void removeProperty(int pIndex)
  {
    pit.removeProperty(pIndex);
  }

  @Nonnull
  public IProperty<S, T> addProperty(int pIndex, IPropertyDescription<S, T> pPropertyDescription)
  {
    return pit.addProperty(pIndex, pPropertyDescription);
  }

}

