package de.adito.propertly.core.api;

import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author w.glanzer, 07.07.2021
 */
class ReadablePropertyPitEventListener implements IPropertyPitEventListener<IPropertyPitProvider<?, ?, ?>, Object>
{
  private final StringBuilder builder = new StringBuilder();

  @Override
  public void propertyValueWillBeChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty,
                                         @Nullable Object pOldValue, @Nullable Object pNewValue, @NotNull Consumer<Runnable> pOnChanged,
                                         @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyValueWillBeChanged")
        .append(" ").append(new PropertyPath(pProperty).asString())
        .append(" ").append(pOldValue)
        .append(" ").append(pNewValue)
        .append('\n');
  }

  @Override
  public void propertyValueChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @Nullable Object pOldValue,
                                   @Nullable Object pNewValue, @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyValueChanged")
        .append(" ").append(new PropertyPath(pProperty).asString())
        .append(" ").append(pOldValue)
        .append(" ").append(pNewValue)
        .append('\n');
  }

  @Override
  public void propertyNameChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @NotNull String pOldName,
                                  @NotNull String pNewName, @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyNameChanged")
        .append(" ").append(new PropertyPath(pProperty).asString())
        .append(" ").append(pOldName)
        .append(" ").append(pNewName)
        .append('\n');
  }

  @Override
  public void propertyWillBeRemoved(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @NotNull Consumer<Runnable> pOnRemoved,
                                    @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyWillBeRemoved")
        .append(" ").append(new PropertyPath(pProperty).asString())
        .append('\n');
  }

  @Override
  public void propertyRemoved(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                              @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                              @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyRemoved")
        .append(" ").append(new PropertyPath(pSource).asString())
        .append(" ").append(pPropertyDescription.getName())
        .append('\n');
  }

  @Override
  public void propertyAdded(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                            @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                            @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyAdded")
        .append(" ").append(new PropertyPath(pSource).asString())
        .append(" ").append(pPropertyDescription.getName())
        .append('\n');
  }

  @Override
  public void propertyOrderWillBeChanged(@NotNull IPropertyPitProvider<?, ?, ?> pSource, @NotNull Consumer<Runnable> pOnChanged,
                                         @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyOrderWillBeChanged")
        .append(" ").append(new PropertyPath(pSource).asString())
        .append('\n');
  }

  @Override
  public void propertyOrderChanged(@NotNull IPropertyPitProvider<?, ?, ?> pSource, @NotNull Set<Object> pAttributes)
  {
    builder.append("propertyOrderChanged")
        .append(" ").append(new PropertyPath(pSource).asString())
        .append('\n');
  }

  @NotNull
  public String asString()
  {
    return builder.toString();
  }
}
