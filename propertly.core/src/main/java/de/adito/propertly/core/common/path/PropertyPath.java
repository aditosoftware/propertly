package de.adito.propertly.core.common.path;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  public PropertyPath(@NotNull String pPath)
  {
    elements = Arrays.asList(pPath.split(DELIM));
  }

  public PropertyPath(@NotNull Iterable<String> pPathElements)
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
