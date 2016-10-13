package de.adito.propertly.diff.data;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.security.MessageDigest;

/**
 * @author j.boesl, 01.08.15
 */
public class PatchNode extends AbstractStructuredPropertlyNode<PropertyNode, PatchNode>
{
  private PropertyNode propertlyNode;
  private String name;

  public PatchNode(PropertyNode pPropertlyNode)
  {
    super(pPropertlyNode.getId());
    name = pPropertlyNode.getName();
    propertlyNode = pPropertlyNode;
    for (PropertyNode node : pPropertlyNode.getChildren())
      addChild(new PatchNode(node));
  }

  @Override
  public PropertyNode getRef()
  {
    return propertlyNode;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void rename(String pName)
  {
    super.rename(pName);
    name = pName;
  }

  @Override
  public int getRating(PatchNode pMatch)
  {
    throw new NotImplementedException();
  }

  @Override
  protected boolean updateMessageDigest(MessageDigest pMessageDigest)
  {
    throw new NotImplementedException();
  }

  @Override
  public PatchNode createCopy()
  {
    throw new NotImplementedException();
  }
}
