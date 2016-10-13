package de.adito.propertly.diff;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.common.IValueCompare;
import de.adito.propertly.diff.data.*;

import java.util.*;

/**
 * @author j.boesl, 19.07.2015
 */
public class Differ
{

  public static Patch diff(IPropertyPitProvider<?, ?, ?> pOriginalPPP, IPropertyPitProvider<?, ?, ?> pRevisedPPP)
  {
    IValueCompare valueCompare = IValueCompare.SIMPLE_IMPL;

    Map<Integer, Matcher> matcherMap = new HashMap<Integer, Matcher>();
    HashMap<String, Integer> hashIdMapping = new HashMap<String, Integer>();
    PropertyNode originalRoot = _fillMatchersMap(pOriginalPPP, hashIdMapping, valueCompare, matcherMap, true);
    PropertyNode revisedRoot = _fillMatchersMap(pRevisedPPP, hashIdMapping, valueCompare, matcherMap, false);

    long l = System.currentTimeMillis();

    for (Matcher matcher : matcherMap.values())
      matcher.rate();

    System.out.println(System.currentTimeMillis() - l);
    l = System.currentTimeMillis();

    for (Matcher matcher : matcherMap.values())
      matcher.amendRating(matcherMap);

    System.out.println(System.currentTimeMillis() - l);

    for (Matcher matcher : matcherMap.values())
      matcher.printSize();

    l = System.currentTimeMillis();

    List<Map.Entry<PropertyNode, PropertyNode>> matches = new ArrayList<Map.Entry<PropertyNode, PropertyNode>>();
    for (Matcher matcher : matcherMap.values())
      matches.addAll(matcher.getFitting());

    System.out.println(System.currentTimeMillis() - l);

    return new Patch(originalRoot, revisedRoot, matches, valueCompare);
  }

  private static PropertyNode _fillMatchersMap(IPropertyPitProvider<?, ?, ?> pPpp, Map<String, Integer> pHashIdMapping,
                                                 IValueCompare pValueCompare, Map<Integer, Matcher> pMatchesMap,
                                                 boolean pIsOriginalNode)
  {
    return _fillMatchersMap(pPpp.getPit().getOwnProperty(), pHashIdMapping, pValueCompare, pMatchesMap, pIsOriginalNode);
  }

  private static PropertyNode _fillMatchersMap(IProperty<?, ?> pProperty, Map<String, Integer> pHashIdMapping,
                                                 IValueCompare pValueCompare, Map<Integer, Matcher> pMatchesMap,
                                                 boolean pIsOriginalNode)
  {
    PropertyNode propertlyNode = new PropertyNode(pProperty, pHashIdMapping, pValueCompare);
    _addMatch(pMatchesMap, pIsOriginalNode, propertlyNode);

    Object value = pProperty.getValue();

    if (value instanceof IPropertyPitProvider ||
        IPropertyPitProvider.class.isAssignableFrom(pProperty.getDescription().getType()))
    {
      //noinspection ConstantConditions
      List<? extends IProperty<?, ?>> properties = ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties();
      for (IProperty<?, ?> property : properties)
        propertlyNode.addChild(_fillMatchersMap(property, pHashIdMapping, pValueCompare, pMatchesMap, pIsOriginalNode));
    }

    return propertlyNode;
  }

  private static void _addMatch(Map<Integer, Matcher> pMatchesMap, boolean pIsOriginalNode, PropertyNode pStructureNode)
  {
    int id = pStructureNode.getId();
    if (id < 0)
      return;

    Matcher matcher = pMatchesMap.get(id);
    if (matcher == null)
    {
      matcher = new Matcher();
      pMatchesMap.put(id, matcher);
    }
    if (pIsOriginalNode)
      matcher.addOriginalNode(pStructureNode);
    else
      matcher.addRevisedNode(pStructureNode);
  }

}
