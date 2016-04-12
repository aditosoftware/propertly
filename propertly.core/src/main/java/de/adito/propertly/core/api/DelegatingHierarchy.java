package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import java.util.function.Function;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 23:00
 */
public abstract class DelegatingHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  protected DelegatingHierarchy(Hierarchy<T> pSourceHierarchy)
  {
    this(pSourceHierarchy, (pHierarchy, pSourceNode) -> new DelegatingNode(pHierarchy, null, pSourceNode));
  }

  protected DelegatingHierarchy(Hierarchy<T> pSourceHierarchy, IDelegateSupply pDelegateSupply)
  {
    this(new DelegatingNodeFunction(pSourceHierarchy, pDelegateSupply));
  }

  private DelegatingHierarchy(DelegatingNodeFunction pDelegatingNodeFunction)
  {
    super(pDelegatingNodeFunction);
  }

  protected interface IDelegateSupply
  {
    DelegatingNode apply(DelegatingHierarchy pHierarchy, INode pSourceNode);
  }

  /**
   * Function implementation
   */
  private static class DelegatingNodeFunction implements Function<Hierarchy, INode>
  {
    private Hierarchy sourceHierarchy;
    private IDelegateSupply delegateSupply;

    DelegatingNodeFunction(Hierarchy pSourceHierarchy, IDelegateSupply pDelegateSupply)
    {
      sourceHierarchy = pSourceHierarchy;
      delegateSupply = pDelegateSupply;
    }

    @Override
    public INode apply(Hierarchy pHierarchy)
    {
      return delegateSupply.apply((DelegatingHierarchy) pHierarchy, sourceHierarchy.getNode());
    }
  }

}
