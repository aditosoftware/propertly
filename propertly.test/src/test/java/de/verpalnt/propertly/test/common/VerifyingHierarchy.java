package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.ISupplier;
import de.verpalnt.propertly.core.hierarchy.AbstractNode;
import de.verpalnt.propertly.core.hierarchy.DelegatingHierarchy;
import de.verpalnt.propertly.core.hierarchy.DelegatingNode;
import de.verpalnt.propertly.core.hierarchy.Hierarchy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:07
 */
public class VerifyingHierarchy<T extends IPropertyPitProvider> extends DelegatingHierarchy<T>
{


  public VerifyingHierarchy(Hierarchy<T> pHierarchy)
  {
    super(pHierarchy);
  }

  @Override
  public Object delegatingSetValue(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue)
  {
    List<? extends Annotation> annotations = pDelegatingNode.getProperty().getAnnotations();
    for (Annotation annotation : annotations)
    {
      if (annotation.annotationType().isAssignableFrom(IntVerifier.class) && pValue instanceof Integer)
      {
        IntVerifier verifier = (IntVerifier) annotation;
        Integer value = (Integer) pValue;
        if (value < verifier.minValue() || value > verifier.maxValue())
          throw new IllegalArgumentException(value.toString() + " for " + pDelegateNode.getProperty());
      }
    }
    return pDelegateNode.setValue(pValue);
  }

  @Override
  public Object delegatingGetValue(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    return pDelegateNode.getValue();
  }

  @Override
  public List<AbstractNode> delegatingGetChildren(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    List<AbstractNode> children = new ArrayList<AbstractNode>();
    for (final AbstractNode node : pDelegateNode.getChildren())
      children.add(new DelegatingNode(this, pDelegatingNode, node.getProperty().getDescription(), new ISupplier<AbstractNode>()
      {
        @Override
        public AbstractNode get()
        {
          return node;
        }
      }));
    return children;
  }

  @Override
  public void delegatingAddProperty(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription)
  {
    pDelegateNode.addProperty(pPropertyDescription);
  }

  @Override
  public boolean delegatingRemoveProperty(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, String pName)
  {
    return pDelegateNode.removeProperty(pName);
  }
}
