package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy
{

  private final Node node;
  private final Set<NodeListener> listeners;

  public Hierarchy(String pName)
  {
    node = new Node(this, null, PropertyDescription.create(null, IPropertyPitProvider.class, pName, Collections.<Annotation>emptySet()));
    listeners = new LinkedHashSet<NodeListener>();
  }

  public Hierarchy(String pName, IPropertyPitProvider pPPP)
  {
    this(pName);
    node.setValue(pPPP);
  }

  public static <T extends IPropertyPitProvider> T create(String pName, T pPPP)
  {
    return (T) new Hierarchy(pName, pPPP).getNode().getValue();
  }

  public Node getNode()
  {
    return node;
  }

  void addNodeListener(NodeListener pListener)
  {
    listeners.add(pListener);
  }

  void removeNodeListener(NodeListener pListener)
  {
    listeners.remove(pListener);
  }

  void fireNodeChanged(Node pNode, Object pOldValue, Object pNewValue)
  {
    for (NodeListener listener : listeners)
      listener.valueChanged(pNode, pOldValue, pNewValue);
  }

  void fireNodeAdded(Node pParentNode, IPropertyDescription pDescription)
  {
    for (NodeListener listener : listeners)
      listener.nodeAdded(pParentNode, pDescription);
  }

  void fireNodeRemoved(Node pParentNode, IPropertyDescription pDescription)
  {
    for (NodeListener listener : listeners)
      listener.nodeRemoved(pParentNode, pDescription);
  }

}
