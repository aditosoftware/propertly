package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.ISupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 18:17
 */
public class DelegatingNode extends AbstractNode
{
  private final ISupplier<INode> delegateProvider;
  private Map<IPropertyPitProvider, IPropertyPitProvider> pitProvider;

  public DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                        @Nonnull IPropertyDescription pPropertyDescription,
                        @Nonnull ISupplier<INode> pDelegateSupplier)
  {
    super(pHierarchy, pParent, pPropertyDescription);
    delegateProvider = pDelegateSupplier;
  }

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
  public Object getValue()
  {
    Object o = getHierarchy().delegatingGetValue(delegateProvider.get(), this);
    if (o instanceof IPropertyPitProvider)
    {
      IPropertyPitProvider ppp = (IPropertyPitProvider) o;
      if (!this.equals(HierarchyHelper.getNode(ppp)))
      {
        IPropertyPitProvider currentPit = pitProvider == null ? null : pitProvider.get(ppp);
        if (currentPit != null)
          return currentPit;
        try
        {
          IPropertyPitProvider ownPitProvider = ppp.getClass().newInstance();
          HierarchyHelper.setNode(ownPitProvider, this);
          pitProvider = Collections.singletonMap(ppp, ownPitProvider);
          return ownPitProvider;
        }
        catch (Exception e)
        {
          throw new RuntimeException(e);
        }
      }
    }
    return o;
  }

  @Nullable
  @Override
  public List<INode> getChildren()
  {
    //noinspection unchecked
    return getHierarchy().delegatingGetChildren(delegateProvider.get(), this);
  }

  @Override
  public void addProperty(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().delegatingAddProperty(delegateProvider.get(), this, pPropertyDescription);
  }

  @Override
  public boolean removeProperty(String pName)
  {
    return getHierarchy().delegatingRemoveProperty(delegateProvider.get(), this, pName);
  }

}
