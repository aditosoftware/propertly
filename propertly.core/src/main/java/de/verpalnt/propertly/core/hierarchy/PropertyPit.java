package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPit<S>
{

  private S source;
  private INode node;


  PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <S extends IPropertyPitProvider<S>> PropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(pCreateFor);
  }

  @Override
  @Nonnull
  public S getSource()
  {
    return source;
  }

  @Override
  @Nullable
  public final IPropertyPitProvider getParent()
  {
    INode parent = node.getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getProperty().getValue();
  }

  @Override
  @Nullable
  public final <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> findProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    List<INode> children = node.getChildren();
    if (children != null)
      for (INode childNode : children)
      {
        IProperty property = childNode.getProperty();
        if (property.getDescription().equals(pPropertyDescription))
          return property;
      }
    return null;
  }

  @Override
  @Nonnull
  public final <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> getProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    IProperty<SOURCE, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    return property;
  }

  @Override
  @Nullable
  public final <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  @Nullable
  public final <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @Override
  public final Set<IPropertyDescription> getPropertyDescriptions()
  {
    Set<IPropertyDescription> set = new LinkedHashSet<IPropertyDescription>();
    for (INode childNode : node.getChildren())
      set.add(childNode.getProperty().getDescription());
    return set;
  }

  @Override
  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    List<IProperty<S, ?>> properties = new ArrayList<IProperty<S, ?>>();
    for (INode childNode : node.getChildren())
      properties.add(childNode.getProperty());
    return properties;
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
  {
    return getProperties().iterator();
  }

  @Override
  public final void addPropertyEventListener(final IPropertyEventListener pListener)
  {
    node.addPropertyEventListener(pListener);
  }

  @Override
  public final void removePropertyEventListener(IPropertyEventListener pListener)
  {
    node.removePropertyEventListener(pListener);
  }

  public IPropertyPit<S> getPit()
  {
    return this;
  }

  void setNode(INode pNode)
  {
    node = pNode;
  }

  INode getNode()
  {
    return node;
  }
}
