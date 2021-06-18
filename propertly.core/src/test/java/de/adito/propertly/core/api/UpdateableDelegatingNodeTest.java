package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.*;
import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author w.glanzer, 17.06.2021
 */
public class UpdateableDelegatingNodeTest
{
  private DummyModel sourceModel;
  private DummyModel updateableModel;
  private _ReadableListener sourceHierarchyListener;
  private _ReadableListener updateableHierarchyListener;

  @BeforeEach
  void setUp()
  {
    // prepare hierarchies
    Hierarchy<DummyModel> sourceHierarchy = new Hierarchy<>("dummy", new DummyModel());
    Hierarchy<DummyModel> updateableHierarchy = new DelegatingHierarchy<DummyModel>(sourceHierarchy,
                                                              (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode))
    {
    };

    // prepare models
    sourceModel = sourceHierarchy.getValue();
    updateableModel = updateableHierarchy.getValue();
    Assertions.assertNotNull(sourceModel);
    Assertions.assertNotNull(updateableModel);

    // add listeners
    sourceHierarchyListener = new _ReadableListener();
    updateableHierarchyListener = new _ReadableListener();
    sourceHierarchy.addWeakListener(sourceHierarchyListener);
    updateableHierarchy.addWeakListener(updateableHierarchyListener);
  }

  @Test
  void test_source_static_change()
  {
    sourceModel.getProperty(DummyModel.simpleStringProperty).setValue("simpleStringValue");
    Assertions.assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_dynamic_addRemove()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);

    container.removeProperty(subModel.getOwnProperty());

    container.addProperty(new SubModel());
    container.addProperty(new SubModel());

    Assertions.assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_dynamic_change()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);

    for(int i = 0; i < 50; i++)
      subModel.setValue(SubModel.subModelProperty, UUID.randomUUID().toString());

    Assertions.assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_dynamic_rename()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);

    subModel.getOwnProperty().rename("myNewName");

    Assertions.assertNotNull(new PropertyPath(subModel).find(updateableModel.getPit().getHierarchy()));
    Assertions.assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_delegate_static_writeThrough()
  {
    updateableModel.setValue(DummyModel.simpleStringProperty, "setFromUpdateable");
    Assertions.assertEquals(updateableModel.getValue(DummyModel.simpleStringProperty), sourceModel.getValue(DummyModel.simpleStringProperty));
    Assertions.assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  @Test
  void test_delegate_dynamic_writeThrough()
  {
    DummyModel.SubModelContainer container = updateableModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);
    IProperty<SubModel, String> subModelProperty = subModel.getProperty(SubModel.subModelProperty);
    subModelProperty.setValue("subModel_setFromUpdateable");

    IProperty<?, ?> sourceProperty = new PropertyPath(subModelProperty).find(sourceModel.getPit().getHierarchy());
    Assertions.assertNotNull(sourceProperty);
    Assertions.assertEquals(subModelProperty.getValue(), sourceProperty.getValue());
    Assertions.assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  @Test
  void test_delegate_dynamic_remove()
  {
    DummyModel.SubModelContainer container = updateableModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);
    container.removeProperty(subModel.getOwnProperty());
    Assertions.assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  public static class DummyModel extends AbstractPPP<IPropertyPitProvider<?, ?, ?>, DummyModel, Object>
  {
    public static final IPropertyDescription<DummyModel, String> simpleStringProperty = PD.create(DummyModel.class);

    public static final IPropertyDescription<DummyModel, SubModelContainer> subModels = PD.create(DummyModel.class);

    public static class SubModelContainer extends AbstractMutablePPP<DummyModel, SubModelContainer, SubModel>
    {
      public SubModelContainer()
      {
        super(SubModel.class);
      }

      @Override
      public String toString()
      {
        return getClass().getName();
      }
    }

    @Override
    public String toString()
    {
      return getClass().getName();
    }
  }

  public static class SubModel extends AbstractPPP<DummyModel.SubModelContainer, SubModel, Object>
  {
    public static final IPropertyDescription<SubModel, String> subModelProperty = PD.create(SubModel.class);

    @Override
    public String toString()
    {
      return getClass().getName();
    }
  }

  private static class _ReadableListener implements IPropertyPitEventListener<IPropertyPitProvider<?, ?, ?>, Object>
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

}
