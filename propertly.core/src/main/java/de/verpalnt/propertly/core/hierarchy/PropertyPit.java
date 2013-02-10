package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPitProvider<S>, Iterable<IProperty<S, ?>>
{

  private S source;
  private AbstractNode node;


  PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <S extends IPropertyPitProvider<S>> PropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(pCreateFor);
  }

  @Nonnull
  public S getSource()
  {
    return source;
  }

  @Nullable
  public final IPropertyPitProvider getParent()
  {
    AbstractNode parent = node.getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getProperty().getValue();
  }

  @Nullable
  public final <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> findProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    List<AbstractNode> children = node.getChildren();
    if (children != null)
      for (AbstractNode childNode : children)
      {
        IProperty property = childNode.getProperty();
        if (property.getDescription().equals(pPropertyDescription))
          return property;
      }
    return null;
  }

  @Nonnull
  public final <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> getProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    IProperty<SOURCE, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    return property;
  }

  @Nullable
  public final <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Nullable
  public final <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  public final Set<IPropertyDescription> getPropertyDescriptions()
  {
    Set<IPropertyDescription> set = new LinkedHashSet<IPropertyDescription>();
    for (AbstractNode childNode : node.getChildren())
      set.add(childNode.getProperty().getDescription());
    return set;
  }

  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    List<IProperty<S, ?>> properties = new ArrayList<IProperty<S, ?>>();
    for (AbstractNode childNode : node.getChildren())
      properties.add(childNode.getProperty());
    return properties;
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
  {
    return getProperties().iterator();
  }

  public final void addPropertyEventListener(final IPropertyEventListener pListener)
  {
    node.addPropertyEventListener(pListener);
  }

  public final void removePropertyEventListener(IPropertyEventListener pListener)
  {
    node.removePropertyEventListener(pListener);
  }

  public PropertyPit<S> getPit()
  {
    return this;
  }

  void setNode(AbstractNode pNode)
  {
    node = pNode;
  }

  AbstractNode getNode()
  {
    return node;
  }
}
