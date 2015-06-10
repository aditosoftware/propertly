package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 18:17
 */
public class DelegatingNode extends AbstractNode
{
  private final ISupplier<INode> delegateProvider;
  private IPropertyPitProvider pitProvider;

  public DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                        @Nonnull IPropertyDescription pPropertyDescription,
                        @Nonnull ISupplier<INode> pDelegateSupplier)
  {
    super(pHierarchy, pParent, pPropertyDescription);
    delegateProvider = pDelegateSupplier;
  }

  @Nonnull
  @Override
  public final DelegatingHierarchy getHierarchy()
  {
    return (DelegatingHierarchy) super.getHierarchy();
  }

  @Override
  public Object setValue(Object pValue)
  {
    return getHierarchy().delegatingSetValue(delegateProvider.get(), this, pValue);
  }

  @Override
  public boolean canRead()
  {
    return getHierarchy().canRead(delegateProvider.get(), this);
  }

  @Override
  public boolean canWrite()
  {
    return getHierarchy().canWrite(delegateProvider.get(), this);
  }

  @Override
  public Object getValue()
  {
    Object o = getHierarchy().delegatingGetValue(delegateProvider.get(), this);
    if (o instanceof IPropertyPitProvider)
    {
      IPropertyPitProvider ppp = (IPropertyPitProvider) o;
      if (!this.equals(HierarchyHelper.getNode(ppp)))
      {
        if (pitProvider != null)
          return pitProvider;
        ppp = PropertlyUtility.create(ppp);
        HierarchyHelper.setNode(ppp, this);
        pitProvider = ppp;
        return ppp;
      }
    }
    else if (pitProvider != null)
      pitProvider = null;
    return o;
  }

  @Nullable
  @Override
  public List<INode> getChildren()
  {
    //noinspection unchecked
    return getHierarchy().delegatingGetChildren(delegateProvider.get(), this);
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull String pName)
  {
    return getHierarchy().findDelegatingChild(delegateProvider.get(), this, pName);
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return getHierarchy().findDelegatingChild(delegateProvider.get(), this, pPropertyDescription);
  }

  @Override
  public void addProperty(@Nonnull IPropertyDescription pPropertyDescription)
  {
    getHierarchy().delegatingAddProperty(delegateProvider.get(), this, pPropertyDescription);
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pPropertyDescription);
  }

  @Override
  public void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription)
  {
    getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pIndex);
  }

  @Override
  public void removeProperty(int pIndex)
  {
    getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pIndex);
  }

  @Override
  public void reorder(Comparator pComparator)
  {
    getHierarchy().delegatingReorder(delegateProvider.get(), this, pComparator);
  }

  @Override
  public void rename(String pName) throws PropertlyRenameException
  {
    getHierarchy().rename(delegateProvider.get(), this, pName);
  }
}
