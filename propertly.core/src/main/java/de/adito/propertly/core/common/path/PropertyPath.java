package de.adito.propertly.core.common.path;

import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author j.boesl, 11.05.15
 */
public class PropertyPath extends AbstractPropertyPath
{
  private static final String DELIM = "/";


  private List<String> elements;

  public PropertyPath(IProperty<?, ?> pProperty)
  {
    elements = new ArrayList<>();
    while (pProperty != null) {
      elements.add(0, pProperty.getName());
      IPropertyPitProvider parent = pProperty.getParent();
      pProperty = parent == null ? null : parent.getPit().getOwnProperty();
    }
  }

  public PropertyPath(IPropertyPitProvider pPPP)
  {
    this(pPPP.getPit().getOwnProperty());
  }

  public PropertyPath(@Nonnull String pPath)
  {
    elements = Arrays.asList(pPath.split(DELIM));
  }

  public PropertyPath(@Nonnull Iterable<String> pPathElements)
  {
    elements = new ArrayList<>();
    for (String pathElement : pPathElements)
      elements.add(pathElement);
  }

  @Override
  protected List<String> getInternalElements()
  {
    return elements;
  }
}
