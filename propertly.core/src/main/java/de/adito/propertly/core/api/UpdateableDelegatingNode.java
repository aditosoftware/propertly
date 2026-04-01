package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * @author w.glanzer, 16.06.2021
 */
public class UpdateableDelegatingNode extends DelegatingNode
{
  private static final String _EVENT_BY_DELEGATINGNODE = "EVENT_BY_UPDATINGDELEGATINGNODE";
  private _DelegateListener delegateListener;
  private ThreadLocal<Boolean> writeOnDelegate;

  protected UpdateableDelegatingNode(@NotNull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent, @NotNull INode pDelegate)
  {
    super(pHierarchy, pParent, pDelegate);
  }

  protected UpdateableDelegatingNode(@NotNull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                                     @NotNull IPropertyDescription pPropertyDescription, boolean pDynamic, @NotNull INode pDelegate)
  {
    super(pHierarchy, pParent, pPropertyDescription, pDynamic, pDelegate);
  }

  @Override
  @Nullable
  public Object setValue(@Nullable Object pValue, @NotNull Set<Object> pAttributes)
  {
    Object delegateValue = executeReadOnDelegate(INode::getValue);
    if ((!hasCreatedCopyOfValue() && delegateValue instanceof IPropertyPitProvider) || (hasCreatedCopyOfValue() && delegateValue == null))
      alignToDelegate();

    pAttributes = new HashSet<>(pAttributes);
    return super.setValue(pValue, pAttributes);
  }

  @Override
  public void remove()
  {
    executeReadOnDelegate(pDelegate -> {
      if (delegateListener != null && pDelegate != null && pDelegate.isValid())
        pDelegate.removeListener(delegateListener);
      return null;
    });

    delegateListener = null;
    // If the delegate was already removed (e.g. because a parent node reacted to the
    // delegate being removed and triggered this node's removal in turn), suppress
    // the write-through inside DelegatingNode.remove() to avoid calling remove() on
    // an already-invalidated delegate whose internal state (delegate field) is null.
    if (executeReadOnDelegate(INode::isValid) != Boolean.TRUE)
      _runWithoutWriteThrough(super::remove);
    else
      super.remove();
    writeOnDelegate = null;
  }

  @Override
  protected DelegatingNode createChild(INode pDelegate)
  {
    return new UpdateableDelegatingNode(getHierarchy(), this, pDelegate);
  }

  @Override
  protected void alignToDelegate()
  {
    executeReadOnDelegate(pDelegate -> {
      if (pDelegate != null && pDelegate.isValid())
      {
        if (delegateListener == null)
          delegateListener = new _DelegateListener();
        pDelegate.removeListener(delegateListener);
        pDelegate.addWeakListener(delegateListener);
      }
      return null;
    });

    _runWithoutWriteThrough(super::alignToDelegate);
  }

  @Override
  protected void executeWriteOnDelegate(@NotNull Set<Object> pAttributes, @NotNull Function<Set<Object>, Consumer<INode>> pOnDelegate)
  {
    if (writeOnDelegate == null || writeOnDelegate.get() != Boolean.FALSE)
      super.executeWriteOnDelegate(addAttribute(pAttributes, _EVENT_BY_DELEGATINGNODE), pOnDelegate);
  }

  /**
   * Executes the given runnable without writing to the underlying delegate
   *
   * @param pRunnable Runnable that will be executed
   */
  private void _runWithoutWriteThrough(@NotNull Runnable pRunnable)
  {
    if (writeOnDelegate == null)
      writeOnDelegate = new ThreadLocal<>();

    try
    {
      writeOnDelegate.set(false);
      pRunnable.run();
    }
    finally
    {
      writeOnDelegate.remove();
    }
  }

  /**
   * Listener auf dem Delegate, um Änderungen zu propagieren
   */
  private class _DelegateListener extends PropertyPitEventAdapter<IPropertyPitProvider<?, ?, ?>, Object>
  {
    @Override
    public void propertyRemoved(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                                @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                                @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      if (!pSource.getPit().getOwnProperty().getDescription().equals(getProperty().getDescription()))
        throw new IllegalStateException("event fired in wrong listener of property " + new PropertyPath(pSource) +
                                            " (original: " + new PropertyPath(getProperty()) + ")");

      _runWithoutWriteThrough(() -> removeProperty(pPropertyDescription, pAttributes));
    }

    @Override
    public void propertyAdded(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                              @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                              @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      if (!pSource.getPit().getOwnProperty().getDescription().equals(getProperty().getDescription()))
        throw new IllegalStateException("event fired in wrong listener of property " + new PropertyPath(pSource) +
                                            " (original: " + new PropertyPath(getProperty()) + ")");

      _runWithoutWriteThrough(() -> {
        Integer index = null;
        if (pSource instanceof IIndexedMutablePropertyPit<?, ?, ?>)
          index = ((IIndexedMutablePropertyPit<?, ?, ?>) pSource).indexOf(pPropertyDescription);
        addProperty(index, pPropertyDescription, pAttributes);
      });
    }

    @Override
    public void propertyOrderChanged(@NotNull IPropertyPitProvider<?, ?, ?> pSource, @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      if (!pSource.getPit().getOwnProperty().getDescription().equals(getProperty().getDescription()))
        throw new IllegalStateException("event fired in wrong listener of property " + new PropertyPath(pSource) +
                                            " (original: " + new PropertyPath(getProperty()) + ")");

      _runWithoutWriteThrough(() -> executeReadOnDelegate(pDelegate -> {
        reorder(Comparator.<IProperty>comparingInt(pProperty -> pDelegate.indexOf(pProperty.getDescription())), pAttributes);
        return null;
      }));
    }

    @Override
    public void propertyValueWillBeChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @Nullable Object pOldValue,
                                           @Nullable Object pNewValue, @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      UpdateableDelegatingNode node = (UpdateableDelegatingNode) findNode(pProperty.getDescription());
      if (node == null)
        throw new IllegalStateException("Source property was changed, but could not be found in delegate hierarchy " +
                                            "(" + new PropertyPath(pProperty) + ")");

      node.fireValueWillBeChange(node.getValue(), pNewValue, pOnChanged, pAttributes);
    }

    @Override
    public void propertyValueChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @Nullable Object pOldValue,
                                     @Nullable Object pNewValue, @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      UpdateableDelegatingNode node = (UpdateableDelegatingNode) findNode(pProperty.getDescription());
      if (node == null)
        throw new IllegalStateException("Source property was changed, but could not be found in delegate hierarchy " +
                                            "(" + new PropertyPath(pProperty) + ")");

      _runWithoutWriteThrough(node::alignToDelegate);
      node.fireValueChange(pOldValue, node.getValue(), pAttributes);
    }

    @Override
    public void propertyNameChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @NotNull String pOldName,
                                    @NotNull String pNewName, @NotNull Set<Object> pAttributes)
    {
      if (pAttributes.contains(_EVENT_BY_DELEGATINGNODE))
        return;

      if (!pProperty.getDescription().equals(getProperty().getDescription().copy(pNewName)))
        throw new IllegalStateException("event fired in wrong listener of property " + new PropertyPath(pProperty) +
                                            " (original: " + new PropertyPath(getProperty()) + ")");

      _runWithoutWriteThrough(() -> rename(pNewName, pAttributes));
    }
  }
}
