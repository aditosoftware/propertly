package de.adito.propertly.diff;

import de.adito.propertly.diff.common.HungarianAlgorithm;
import de.adito.propertly.diff.data.PropertyNode;

import java.util.*;

/**
 * @author j.boesl, 19.07.2015
 */
public class Matcher
{

  private List<PropertyNode> oldNodes;
  private List<PropertyNode> newNodes;
  private Map<PropertyNode, Integer> oldNodesIndexes;
  private Map<PropertyNode, Integer> newNodesIndexes;
  private int[][] ratings;

  public Matcher()
  {
    oldNodes = new LinkedList<PropertyNode>();
    newNodes = new LinkedList<PropertyNode>();
    oldNodesIndexes = new HashMap<PropertyNode, Integer>();
    newNodesIndexes = new HashMap<PropertyNode, Integer>();
  }

  public void printSize()
  {
    if (oldNodes.size() > 0 && newNodes.size() > 0 && !(oldNodes.size() == 1 && newNodes.size() == 1))
    {
      PropertyNode nodeSample;
      if (!oldNodes.isEmpty())
        nodeSample = oldNodes.get(0);
      else
        nodeSample = newNodes.get(0);
      System.out.println("old: " + oldNodes.size() + ", new: " + newNodes.size() + ", " + nodeSample + ", " + nodeSample.getRef());
    }
  }

  public void addOriginalNode(PropertyNode pStructureNode)
  {
    oldNodesIndexes.put(pStructureNode, oldNodes.size());
    oldNodes.add(pStructureNode);
  }

  public void addRevisedNode(PropertyNode pStructureNode)
  {
    newNodesIndexes.put(pStructureNode, newNodes.size());
    newNodes.add(pStructureNode);
  }

  public void rate()
  {
    int oldSize = oldNodes.size();
    int newSize = newNodes.size();

    if (oldSize == 0)
      newSize = 0;
    else if (newSize == 0)
      oldSize = 0;

    ratings = new int[oldSize][newSize];
    for (int i = 0; i < oldSize; i++)
      for (int j = 0; j < newSize; j++)
        ratings[i][j] = _getRating(oldNodes.get(i), newNodes.get(j));
  }

  public void amendRating(Map<Integer, Matcher> pMatcherMap)
  {
    int oldSize = oldNodes.size();
    int newSize = newNodes.size();

    for (int i = 0; i < oldSize; i++)
    {
      for (int j = 0; j < newSize; j++)
      {
        if (ratings[i][j] < Integer.MAX_VALUE)
        {
          PropertyNode oldNodeParent = oldNodes.get(i).getParent();
          PropertyNode newNodeParent = newNodes.get(j).getParent();
          if (oldNodeParent != null && newNodeParent != null)
          {
            Matcher matcher = pMatcherMap.get(oldNodeParent.getId());
            if (matcher.equals(pMatcherMap.get(newNodeParent.getId())))
            {
              int pi = matcher.oldNodesIndexes.get(oldNodeParent);
              int pj = matcher.newNodesIndexes.get(newNodeParent);
              ratings[i][j] -= (matcher.ratings[pi][pj] - Integer.MAX_VALUE) / -100000;
            }
          }
        }
      }
    }
  }

  public List<Map.Entry<PropertyNode, PropertyNode>> getFitting()
  {
    int[] result;
    if (ratings.length != 0 && ratings[0].length != 0)
    {
      HungarianAlgorithm ha = new HungarianAlgorithm(ratings);
      result = ha.execute();
    }
    else
      result = new int[0];

    List<Map.Entry<PropertyNode, PropertyNode>> list = new LinkedList<Map.Entry<PropertyNode, PropertyNode>>();

    for (int i = 0; i < result.length; i++)
    {
      PropertyNode oldNode = oldNodes.get(i);
      oldNodes.set(i, null);

      PropertyNode newNode = null;
      int newIndex = result[i];
      if (newIndex != -1)
      {
        newNode = newNodes.get(newIndex);
        newNodes.set(newIndex, null);
      }
      list.add(new AbstractMap.SimpleEntry<PropertyNode, PropertyNode>(oldNode, newNode));
    }

    for (PropertyNode oldNode : oldNodes)
      if (oldNode != null)
        list.add(new AbstractMap.SimpleEntry<PropertyNode, PropertyNode>(oldNode, null));

    for (PropertyNode newNode : newNodes)
      if (newNode != null)
        list.add(new AbstractMap.SimpleEntry<PropertyNode, PropertyNode>(null, newNode));

    return list;
  }

  private int _getRating(PropertyNode pOldNode, PropertyNode pNewNode)
  {
    return Integer.MAX_VALUE - (100000 * pOldNode.getRating(pNewNode));
  }


}
