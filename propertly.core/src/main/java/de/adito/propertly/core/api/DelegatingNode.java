package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 18:17
 */
public class DelegatingNode extends AbstractNode
{
  private final Supplier<INode> delegateProvider;
  private IPropertyPitProvider pitProvider;

  public DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                        @Nonnull IPropertyDescription pPropertyDescription,
                        @Nonnull Supplier<INode> pDelegateSupplier)
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
  @Nullable
  public Object setValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes)
  {
    return getHierarchy().delegatingSetValue(delegateProvider.get(), this, pValue, pAttributes);
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
  public void addProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    getHierarchy().delegatingAddProperty(delegateProvider.get(), this, pPropertyDescription, pAttributes);
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    return getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pPropertyDescription, pAttributes);
  }

  @Override
  public void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    getHierarchy().delegatingAddProperty(delegateProvider.get(), this, pIndex, pPropertyDescription, pAttributes);
  }

  @Override
  public void removeProperty(int pIndex, @Nonnull Set<Object> pAttributes)
  {
    getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pIndex, pAttributes);
  }

  @Override
  public int indexOf(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return getHierarchy().delegatingIndexOf(delegateProvider.get(), this, pPropertyDescription);
  }

  @Override
  public void reorder(@Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes)
  {
    getHierarchy().delegatingReorder(delegateProvider.get(), this, pComparator, pAttributes);
  }

  @Override
  public void rename(@Nonnull String pName, @Nonnull Set<Object> pAttributes) throws PropertlyRenameException
  {
    getHierarchy().rename(delegateProvider.get(), this, pName, pAttributes);
  }
}
