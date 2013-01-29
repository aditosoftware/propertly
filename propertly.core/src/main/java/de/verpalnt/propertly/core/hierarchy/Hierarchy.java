package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IPropertyPitProvider;
import de.verpalnt.propertly.core.PropertyDescription;

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

  public Node getNode()
  {
    return node;
  }

  public void addNodeListener(NodeListener pListener)
  {
    listeners.add(pListener);
  }

  public void removeNodeListener(NodeListener pListener)
  {
    listeners.remove(pListener);
  }

  void fireNodeChanged(Node pNode, Object pOldValue, Object pNewValue)
  {
    for (NodeListener listener : listeners)
      listener.valueChanged(pNode, pOldValue, pNewValue);
  }

}
