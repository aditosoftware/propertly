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
    {
      /*
      This flag is set here because otherwise it would block too much.
      Before this flag was set here, it was set in the overrides of the Operational Methods (addProperty, removeProperty, reorder and rename) before the super variant was called.
      e.g. for the removeProperty:
      ---------------------------
        removeProperty(pPropertyDescription, pAttributes)
        {
          pAttributes = new HashSet<>(pAttributes);
          pAttributes.add(_EVENT_BY_DELEGATINGNODE);
          return super.removeProperty(pPropertyDescription, pAttributes);
        }

      This causes the flag to be set before the super method runs.
      Crucially, this means all the listeners will be fired with the flag already in place therefore,
      no Listener will update any DelegatingNode that has us as its delegate.

      for example, the (simplified) super method that was called before:
      ----------------------
         public boolean removeProperty(IPropertyDescription pPropertyDescription,Set<Object> pAttributes)
         {
            fireNodeWillBeRemoved(description, onFinish::add, pAttributes);
            executeWriteOnDelegate(pAttributes, pAttr -> pDelegate -> pDelegate.removeProperty(pPropertyDescription, pAttr));
            fireNodeRemoved(description, pAttributes);
         }
      this is a very simplivied version of the removeProperty of our super class (DelegatingNode)

      Now we can see that if the attribute is set before super is called (as was the case before),
      both fireNodeWillBeRemoved and fireNodeRemoved will fire with the attribute already in place.
      This means all the listeners waiting for this exact event will
      immediately return without doing anything because the flag told them to do so.

      In the above example for the super method you can already see that the new 'executeWriteOnDelegate' variant,
      that now gets the attribute as a separate Parameter and then gives them to the lambda method containing the actual operation.

      This lets us set the '_EVENT_BY_DELEGATINGNODE' flag here instead of before the super, meaning only the delegates know of the flag,
      and there it blocks their listeners from writing back up to us.

      At the same time, our Listeners will never hear of the flag and can still notify any DelegatingNodes, for whom we are their delegate.
      */
      Set<Object> attributes = new HashSet<>(pAttributes);
      attributes.add(_EVENT_BY_DELEGATINGNODE);

      super.executeWriteOnDelegate(attributes, pOnDelegate);
    }
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
