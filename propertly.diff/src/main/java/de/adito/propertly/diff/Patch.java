package de.adito.propertly.diff;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.common.IValueCompare;
import de.adito.propertly.diff.data.*;
import de.adito.propertly.diff.delta.*;

import java.util.*;

/**
 * @author j.boesl, 22.07.15
 */
public class Patch
{

  private List<Delta> deltas = new ArrayList<Delta>();

  public Patch(PropertyNode pOriginalRoot, PropertyNode pRevisedRoot,
               Iterable<Map.Entry<PropertyNode, PropertyNode>> pMatches, IValueCompare pValueCompare)
  {
    Set<PropertyNode> deleteNodes = new HashSet<PropertyNode>();
    Set<PropertyNode> insertedNodes = new HashSet<PropertyNode>();
    Map<PropertyNode, Map.Entry<IPropertyPath, PropertyNode>> revisedOriginalMap = new HashMap<PropertyNode, Map.Entry<IPropertyPath, PropertyNode>>();
    for (Map.Entry<PropertyNode, PropertyNode> match : pMatches)
    {
      PropertyNode originalNode = match.getKey();
      PropertyNode revisedNode = match.getValue();
      if (revisedNode == null)
        deleteNodes.add(originalNode);
      else if (originalNode == null)
        insertedNodes.add(revisedNode);
      else
        revisedOriginalMap.put(revisedNode, new AbstractMap.SimpleEntry<IPropertyPath, PropertyNode>(originalNode.getPath(), originalNode));
    }

    _fillCommonDeltas(pRevisedRoot, pOriginalRoot, revisedOriginalMap, insertedNodes, pValueCompare);

    _fillDeleteDeltas(pOriginalRoot, deleteNodes);

    _fillOrderDeltas(pRevisedRoot, pOriginalRoot);
  }


  private void _fillDeleteDeltas(PropertyNode pOriginalNode, Set<PropertyNode> pDeletedNodes)
  {
    Deque<List<PropertyNode>> originalNodeDeque = new LinkedList<List<PropertyNode>>();
    originalNodeDeque.add(Collections.singletonList(pOriginalNode));
    while (!originalNodeDeque.isEmpty())
    {
      List<PropertyNode> childPack = originalNodeDeque.pop();
      for (PropertyNode originalNode : childPack)
      {
        if (pDeletedNodes.contains(originalNode))
        {
          deltas.add(new DeltaDelete(originalNode.getPath(), true));
          originalNode.remove();
        }
        else
          originalNodeDeque.add(originalNode.getChildren());
      }
    }
  }

  private void _fillOrderDeltas(PropertyNode pRevisedNode, PropertyNode pOrgRoot)
  {
    List<PropertyNode> revChildren = pRevisedNode.getChildren();
    if (revChildren.size() > 1 && pRevisedNode.getRef().getValue() instanceof IIndexedMutablePropertyPitProvider)
    {
      ArrayList<String> rc = new ArrayList<String>(revChildren.size());
      for (PropertyNode revChild : revChildren)
        rc.add(revChild.getName());

      List<PropertyNode> orgChildren = pOrgRoot.resolveChild(pRevisedNode.getPath()).getChildren();
      ArrayList<String> oc = new ArrayList<String>(orgChildren.size());
      for (PropertyNode orgChild : orgChildren)
        oc.add(orgChild.getName());

      if (!oc.equals(rc))
      {
        int[] order = new int[oc.size()];
        for (int i = 0; i < order.length; i++)
          order[i] = rc.indexOf(oc.get(i));
        deltas.add(new DeltaUpdateOrder(pRevisedNode.getPath(), order));
      }
    }
    for (PropertyNode revChild : revChildren)
      _fillOrderDeltas(revChild, pOrgRoot);
  }

  private void _fillCommonDeltas(PropertyNode pRevisedRoot, PropertyNode pOrgRoot,
                                 Map<PropertyNode, Map.Entry<IPropertyPath, PropertyNode>> pMatches, Set<PropertyNode> pInsertedNodes,
                                 IValueCompare pValueCompare)
  {
    Deque<List<PropertyNode>> revisedNodeDeque = new LinkedList<List<PropertyNode>>();
    revisedNodeDeque.add(Collections.singletonList(pRevisedRoot));
    while (!revisedNodeDeque.isEmpty())
    {
      List<PropertyNode> childPack = revisedNodeDeque.pop();
      for (PropertyNode revisedNode : childPack)
      {
        IPropertyPath revisedPath = revisedNode.getPath();
        IProperty revisedRef = revisedNode.getRef();
        if (pInsertedNodes.contains(revisedNode))
        {
          // insert
          PropertyNode orgParent = pOrgRoot.resolveChild(revisedPath.getParent());
          int index = _getIndex(revisedRef, orgParent);
          orgParent.addChild(index, revisedNode.createCopy(), null);
          deltas.add(new DeltaInsert(revisedPath.getParent(), revisedRef.getDescription(), _getValue(revisedRef), index));
        }
        else
        {
          Map.Entry<IPropertyPath, PropertyNode> orgEntry = pMatches.get(revisedNode);
          if (orgEntry != null)
          {
            IPropertyPath originalPath = orgEntry.getKey();
            PropertyNode originalNode = orgEntry.getValue();
            IProperty originalRef = originalNode.getRef();
            if (!revisedPath.getParent().equals(originalPath.getParent()) ||
                revisedRef.getName().equals(originalRef.getName()) && !revisedRef.getDescription().equals(originalRef.getDescription()))
            {
              // move
              PropertyNode orgParent = pOrgRoot.resolveChild(revisedPath.getParent());
              int index = _getIndex(revisedRef, orgParent);
              orgParent.addChild(index, originalNode, revisedNode.getName());
              deltas.add(new DeltaUpdateParent(originalPath, revisedPath.getParent(), revisedRef.getDescription(), index));
            }
            else
            {
              if (!revisedRef.getDescription().equals(originalRef.getDescription()))
              {
                // rename
                originalNode.rename(revisedPath.getName());
                deltas.add(new DeltaUpdateName(originalPath, revisedPath.getName()));
              }
              Object originalValue = _getValue(originalRef);
              Object revisedValue = _getValue(revisedRef);
              if (!pValueCompare.equals(revisedValue, originalValue))
              {
                // update value
                deltas.add(new DeltaUpdateValue(revisedPath, revisedValue));
              }
            }
          }
        }
        List<PropertyNode> children = revisedNode.getChildren();
        if (!children.isEmpty())
          revisedNodeDeque.add(children);
      }
    }
  }

  private int _getIndex(IProperty pRevisedRef, PropertyNode pOrgParent)
  {
    IPropertyPitProvider revisedParent = pRevisedRef.getParent();
    if (revisedParent instanceof IIndexedMutablePropertyPitProvider)
    {
      int index = ((IIndexedMutablePropertyPit) revisedParent.getPit()).indexOf(pRevisedRef.getDescription());
      if (index < pOrgParent.getChildren().size())
        return index;
    }
    return -1;
  }


  private Object _getValue(IProperty<?, ?> pProperty)
  {
    Object value = pProperty.getValue();
    return value instanceof IPropertyPitProvider ? value.getClass() : value;
  }

  public List<Delta> getDeltas()
  {
    return deltas;
  }

  public void apply(IHierarchy<?> pHierarchy)
  {
    OrgPatchedMapping orgPatchedMapping = new OrgPatchedMapping(pHierarchy);
    for (int i = 0; i < deltas.size(); i++)
    {
      Delta delta = deltas.get(i);
      delta.applyTo(orgPatchedMapping, pHierarchy);
    }
  }

}
