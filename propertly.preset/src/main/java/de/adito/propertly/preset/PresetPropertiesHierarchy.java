package de.adito.propertly.preset;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;

/**
 * @author j.boesl, 09.09.16
 */
public class PresetPropertiesHierarchy<T extends IPropertyPitProvider> extends DelegatingHierarchy<T>
{
  protected PresetPropertiesHierarchy(Hierarchy<T> pSourceHierarchy)
  {
    super(pSourceHierarchy, (pHierarchy, pSourceNode) -> new PresetPropertiesNode(pHierarchy, null, pSourceNode));
  }

  /**
   * Node-Impl
   */
  protected static class PresetPropertiesNode extends DelegatingNode
  {
    protected PresetPropertiesNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull INode pDelegate)
    {
      super(pHierarchy, pParent, pDelegate);
    }

    @Override
    protected DelegatingNode createChild(INode pDelegate)
    {
      return new PresetPropertiesNode(getHierarchy(), this, pDelegate);
    }

    @Override
    public Object getValue()
    {
      Object value = super.getValue();
      return value == null ? getDefaultValue() : value;
    }

    protected boolean isDefaultValue()
    {
      return super.getValue() == null;
    }

    protected Object getDefaultValue()
    {
      IPropertyDescription description = getProperty().getDescription();
      if (description instanceof IPresetPropertyDescription)
        return ((IPresetPropertyDescription) description).getPreset();
      return null;
    }
  }

}
