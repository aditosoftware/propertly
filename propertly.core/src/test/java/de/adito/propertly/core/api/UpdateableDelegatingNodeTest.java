package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.*;
import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author w.glanzer, 17.06.2021
 */
public class UpdateableDelegatingNodeTest
{
  private DummyModel sourceModel;
  private DummyModel updateableModel;
  private ReadablePropertyPitEventListener sourceHierarchyListener;
  private ReadablePropertyPitEventListener updateableHierarchyListener;

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
    assertNotNull(sourceModel);
    assertNotNull(updateableModel);

    // add listeners
    sourceHierarchyListener = new ReadablePropertyPitEventListener();
    updateableHierarchyListener = new ReadablePropertyPitEventListener();
    sourceHierarchy.addWeakListener(sourceHierarchyListener);
    updateableHierarchy.addWeakListener(updateableHierarchyListener);
  }

  @Test
  void test_source_static_change()
  {
    sourceModel.getProperty(DummyModel.simpleStringProperty).setValue("simpleStringValue");
    assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_static_nestedTypeChange()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);
    SubModel subSubModel = (SubModel) subModel.setValue(SubModel.staticSubModel, new SubModel());
    assertNotNull(subSubModel);
    DummyModel.SubModelContainer subSubSubModelContainer = subSubModel.setValue(SubModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(subSubSubModelContainer);
    subSubSubModelContainer.addProperty(new SubModel());
    subSubSubModelContainer.addProperty(new SubModel());

    // change type of submodel with predefined data
    OtherSubModel otherSubSubModelTmp = new Hierarchy<>("dummy", new OtherSubModel()).getValue();
    DummyModel.SubModelContainer otherSubSubSubModelContainer = otherSubSubModelTmp.setValue(OtherSubModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(otherSubSubSubModelContainer);
    otherSubSubSubModelContainer.addProperty("newModel", new SubModel());
    subModel.setValue(SubModel.staticSubModel, otherSubSubModelTmp);

    // validate, that the static-subSubModel has the correct, recalculated, children
    IProperty<?, ?> subModelProp = new PropertyPath(subModel).find(updateableModel.getPit().getHierarchy());
    assertNotNull(subModelProp);
    SubModel updatedSubModel = (SubModel) subModelProp.getValue();
    assertNotNull(updatedSubModel);
    ISubModel<?> updatedSubSubModel = updatedSubModel.getValue(SubModel.staticSubModel);
    assertNotNull(updatedSubSubModel);
    assertEquals(OtherSubModel.class, updatedSubSubModel.getClass());
    DummyModel.SubModelContainer updatedSubSubSubModelContainer = ((OtherSubModel) updatedSubSubModel).getValue(OtherSubModel.subModels);
    assertNotNull(updatedSubSubSubModelContainer);
    assertEquals(1, updatedSubSubSubModelContainer.getValues().size());
    assertNotNull(updatedSubSubSubModelContainer.findProperty("newModel"));
    Assertions.assertTrue(Objects.requireNonNull(updatedSubSubSubModelContainer.findProperty("newModel")).isValid());
  }

  @Test
  void test_source_dynamic_addRemove()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);

    container.removeProperty(subModel.getOwnProperty());

    container.addProperty(new SubModel());
    container.addProperty(new SubModel());

    assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_dynamic_change()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);

    for (int i = 0; i < 50; i++)
      subModel.setValue(SubModel.subModelProperty, UUID.randomUUID().toString());

    assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_source_dynamic_initWithPPP()
  {
    //precreate a model to add afterwards
    List<String> addedModelNames = new ArrayList<>();
    SubModel preSubModel = new Hierarchy<>(UUID.randomUUID().toString(), new SubModel()).getValue();
    DummyModel.SubModelContainer preSubSubModels = preSubModel.setValue(SubModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(preSubSubModels);
    for (int i = 0; i < 20; i++)
      addedModelNames.add(preSubSubModels.addProperty(UUID.randomUUID().toString(), new SubModel()).getName());

    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(preSubModel).getValue();
    assertNotNull(subModel);
    DummyModel.SubModelContainer subSubModels = subModel.setValue(SubModel.subModels, preSubSubModels);
    assertNotNull(subSubModels);

    for (String name : addedModelNames)
    {
      assertNotNull(subSubModels.findProperty(name));
      Assertions.assertTrue(Objects.requireNonNull(subSubModels.findProperty(name)).isValid());
    }
  }

  @Test
  void test_source_dynamic_nestedChange()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);
    DummyModel.SubModelContainer subSubModels = subModel.setValue(SubModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(subSubModels);


    List<String> addedModelNames = new ArrayList<>();
    for (int i = 0; i < 20; i++)
      addedModelNames.add(subSubModels.addProperty(new SubModel()).getName());

    assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
    for (String name : addedModelNames)
    {
      assertNotNull(subSubModels.findProperty(name));
      Assertions.assertTrue(Objects.requireNonNull(subSubModels.findProperty(name)).isValid());
    }
  }

  @Test
  void test_source_dynamic_rename()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);

    subModel.getOwnProperty().rename("myNewName");

    assertNotNull(new PropertyPath(subModel).find(updateableModel.getPit().getHierarchy()));
    assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
  }

  @Test
  void test_delegate_static_writeThrough()
  {
    updateableModel.setValue(DummyModel.simpleStringProperty, "setFromUpdateable");
    assertEquals(updateableModel.getValue(DummyModel.simpleStringProperty), sourceModel.getValue(DummyModel.simpleStringProperty));
    assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  @Test
  void test_delegate_dynamic_writeThrough()
  {
    DummyModel.SubModelContainer container = updateableModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);
    IProperty<SubModel, String> subModelProperty = subModel.getProperty(SubModel.subModelProperty);
    subModelProperty.setValue("subModel_setFromUpdateable");

    IProperty<?, ?> sourceProperty = new PropertyPath(subModelProperty).find(sourceModel.getPit().getHierarchy());
    assertNotNull(sourceProperty);
    assertEquals(subModelProperty.getValue(), sourceProperty.getValue());
    assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  @Test
  void test_delegate_dynamic_remove()
  {
    DummyModel.SubModelContainer container = updateableModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    assertNotNull(subModel);
    container.removeProperty(subModel.getOwnProperty());
    assertEquals(updateableHierarchyListener.asString(), sourceHierarchyListener.asString());
  }

  /**
   * Verifies that in a two-level UpdateableDelegatingNode stack (source → levelA → levelB),
   * a dynamic property added to the source container is reflected in levelB,
   * and that subsequently setting a value on that property also propagates correctly to levelB.
   */
  @Test
  void test_twoLevel_delegation_dynamicAddThenSetValue()
  {
    test_twoLevel_operation(
        // nothing to do here, everything we need is already done in the test_twoLevel_operation method
        (pSubModel, pContainer) -> {
        },
        // just check if it was done correctly
        pContainer -> assertEquals(1, pContainer.getValues().size(),
                                   "levelB should have the dynamically added SubModel in its container")
    );
  }

  /**
   * Verifies that a dynamic property removed in the source is also removed in levelB of a
   * two-level (source → levelA → levelB) UpdateableDelegatingNode stack.
   */
  @Test
  void test_twoLevel_delegation_dynamicRemove()
  {
    test_twoLevel_operation(
        (pSubModel, pContainer) -> pContainer.removeProperty(pSubModel.getOwnProperty()),
        pContainer -> assertEquals(0, pContainer.getValues().size(),
                                   "levelB should reflect the removal of the dynamic SubModel")
    );
  }


  /**
   * Verifies that a property removed by index in the source is also removed in levelB of a
   * two-level (source → levelA → levelB) UpdateableDelegatingNode stack.
   */
  @Test
  void test_twoLevel_delegation_remove_by_index()
  {
    test_twoLevel_indexed_operation(
        pContainer -> pContainer.removeProperty(0),
        // we expect one value to still be there since test_twoLevel_indexed_operation adds two models
        pContainer -> assertEquals(1, pContainer.getValues().size(),
                                   "levelB should reflect the removal of the dynamic SubModel")
    );
  }

  /**
   * Verifies that renaming a dynamic property in the source is reflected in levelB of a
   * two-level (source → levelA → levelB) UpdateableDelegatingNode stack.
   */
  @Test
  void test_twoLevel_delegation_rename()
  {
    test_twoLevel_operation(
        (pSubModel, pContainer) -> pSubModel.getOwnProperty().rename("renamedModel"),
        pContainer -> assertNotNull(pContainer.findProperty("renamedModel"),
                                    "levelB should reflect the rename of the dynamic SubModel")
    );
  }

  /**
   * Verifies that a reorder operation in the source is also removed in levelB of a
   * two-level (source → levelA → levelB) UpdateableDelegatingNode stack.
   */
  @Test
  void test_twoLevel_delegation_reorder()
  {
    List<String> expectedOrder = new ArrayList<>();
    expectedOrder.add("firstModel");
    expectedOrder.add("secondModel");

    test_twoLevel_indexed_operation(
        pContainer -> pContainer.reorder(Comparator.comparing(IProperty::getName)),
        pContainerToCheck -> {
          assertEquals(
              expectedOrder,
              pContainerToCheck.getValues().stream()
                  .map(pISubModel -> pISubModel.getPit().getOwnProperty().getName()).collect(Collectors.toList())
          );
        }

    );
  }

  /**
   * Helper method for testing operations on a two-Level UpdateableDelegatingNode stack, while using an {@link AbstractIndexedMutablePPP}.
   *
   * @param pOperationToCheck the operation to check
   * @param pAssertion        the assertion that checks if it was done correctly
   */
  private void test_twoLevel_indexed_operation(
      @NotNull Consumer<DummyModel.SubModelIndexedContainer> pOperationToCheck,
      @NotNull Consumer<DummyModel.SubModelIndexedContainer> pAssertion)
  {
    test_twoLevel(
        (pSrcModel, pLevelBModel) -> {
          DummyModel.SubModelIndexedContainer container = pSrcModel.setValue(DummyModel.subModelsIndexed, new DummyModel.SubModelIndexedContainer());
          assertNotNull(container);
          SubModel subModel = container.addProperty(new SubModel()).getValue();
          assertNotNull(subModel);
          subModel.getOwnProperty().rename("secondModel");
          SubModel subModel2 = container.addProperty(new SubModel()).getValue();
          assertNotNull(subModel2);
          subModel2.getOwnProperty().rename("firstModel");

          assertDoesNotThrow(() -> pOperationToCheck.accept(container));

          DummyModel.SubModelIndexedContainer levelBContainerIndexed = pLevelBModel.getValue(DummyModel.subModelsIndexed);
          assertNotNull(levelBContainerIndexed);
          pAssertion.accept(levelBContainerIndexed);
        }
    );
  }

  /**
   * Helper method for testing operations on a two-Level UpdateableDelegatingNode stack, while using an {@link AbstractMutablePPP}.
   *
   * @param pOperationToCheck the operation to check
   * @param pAssertion        the assertion that checks if it was done correctly
   */
  private void test_twoLevel_operation(@NotNull BiConsumer<SubModel, DummyModel.SubModelContainer> pOperationToCheck, @NotNull Consumer<DummyModel.SubModelContainer> pAssertion)
  {
    test_twoLevel(
        (pSrcModel, pLevelBModel) -> {
          DummyModel.SubModelContainer container = pSrcModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
          assertNotNull(container);
          IProperty<DummyModel.SubModelContainer, SubModel> dynProp = container.addProperty(new SubModel());
          SubModel subModel = dynProp.getValue();
          assertNotNull(subModel);

          assertDoesNotThrow(() -> pOperationToCheck.accept(subModel, container));

          DummyModel.SubModelContainer levelBContainer = pLevelBModel.getValue(DummyModel.subModels);
          assertNotNull(levelBContainer);
          pAssertion.accept(levelBContainer);
        }
    );
  }

  /**
   * Helper method for testing operations on a two-Level UpdateableDelegatingNode stack.
   *
   * @param pCheck the check to perform
   */
  private void test_twoLevel(@NotNull BiConsumer<DummyModel, DummyModel> pCheck)
  {
    Hierarchy<DummyModel> sourceHierarchy = new Hierarchy<>("dummy", new DummyModel());
    Hierarchy<DummyModel> levelAHierarchy = new DelegatingHierarchy<DummyModel>(
        sourceHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode)
    )
    {
    };
    Hierarchy<DummyModel> levelBHierarchy = new DelegatingHierarchy<DummyModel>(
        levelAHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode)
    )
    {
    };

    DummyModel srcModel = sourceHierarchy.getValue();
    DummyModel levelBModel = levelBHierarchy.getValue();
    assertNotNull(srcModel);
    assertNotNull(levelBModel);

    ReadablePropertyPitEventListener srcListener = new ReadablePropertyPitEventListener();
    ReadablePropertyPitEventListener levelBListener = new ReadablePropertyPitEventListener();
    sourceHierarchy.addWeakListener(srcListener);
    levelBHierarchy.addWeakListener(levelBListener);

    pCheck.accept(srcModel, levelBModel);

    assertEquals(srcListener.asString(), levelBListener.asString());
  }

  /**
   * Verifies that after constructing an outer hierarchy over a lazy middle hierarchy,
   * value changes in the source still propagate correctly through to the outer hierarchy.
   */
  @Test
  void test_valueChangeDuringAlignToDelegate_sourceChangePropagatesAfterConstruction()
  {
    checkWithLazyLoadingNode((pSrcModel, pOuterModel) -> {
      pSrcModel.setValue(DummyModel.simpleStringProperty, "hello");
      assertEquals("hello", pOuterModel.getValue(DummyModel.simpleStringProperty));
    });
  }

  /**
   * Verifies that after constructing an outer hierarchy over a lazy middle hierarchy,
   * writes to the outer hierarchy propagate back through the lazy middle layer to the source.
   */
  @Test
  void test_valueChangeDuringAlignToDelegate_writeThroughAfterConstruction()
  {
    checkWithLazyLoadingNode((pSrcModel, pOuterModel) -> {
      pOuterModel.setValue(DummyModel.simpleStringProperty, "fromOuter");
      assertEquals("fromOuter", pSrcModel.getValue(DummyModel.simpleStringProperty));
    });
  }

  /**
   * Verifies that after construction over a lazy middle hierarchy with pre-existing dynamic
   * children, those children are accessible and valid in the outer hierarchy.
   */
  @Test
  void test_valueChangeDuringAlignToDelegate_dynamicChildrenAccessibleAfterConstruction()
  {
    checkWithLazyLoadingNode((pSrcModel, pOuterModel) -> {
      DummyModel.SubModelContainer outerContainer = pOuterModel.getValue(DummyModel.subModels);
      assertNotNull(outerContainer);
      assertEquals(2, outerContainer.getValues().size());
      assertNotNull(outerContainer.findProperty("child1"));
      assertNotNull(outerContainer.findProperty("child2"));
      Assertions.assertTrue(Objects.requireNonNull(outerContainer.findProperty("child1")).isValid());
      Assertions.assertTrue(Objects.requireNonNull(outerContainer.findProperty("child2")).isValid());
    });
  }

  /**
   * Helper method that creates a Node hierarchy with 3 levels: a source level contained in a lazy loading level that in itself is contained in a
   * third level
   *
   * @param pCheck the check to perform on the structure described above,
   *               the first parameter is the model source level, while the second parameter is the model at the third level
   */
  private void checkWithLazyLoadingNode(@NotNull BiConsumer<DummyModel, DummyModel> pCheck)
  {
    Hierarchy<DummyModel> sourceHierarchy = new Hierarchy<>("source", new DummyModel());
    DummyModel.SubModelContainer srcContainer = sourceHierarchy.getValue()
        .setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    assertNotNull(srcContainer);
    srcContainer.addProperty("child1", new SubModel());
    srcContainer.addProperty("child2", new SubModel());

    Hierarchy<DummyModel> middleHierarchy = new DelegatingHierarchy<DummyModel>(
        sourceHierarchy,
        (pHierarchy, pSourceNode) -> new _LazyLoadDelegatingNode(pHierarchy, null, pSourceNode))
    {
    };
    // Check creation of a hierarchy with a lazy loading node does not throw an exception
    Hierarchy<DummyModel> outerHierarchy = assertDoesNotThrow(() -> new DelegatingHierarchy<DummyModel>(
        middleHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode))
    {
    });

    DummyModel srcModel = sourceHierarchy.getValue();
    DummyModel outerModel = outerHierarchy.getValue();
    assertNotNull(srcModel);
    assertNotNull(outerModel);

    pCheck.accept(srcModel, outerModel);
  }


  /**
   * Verifies that changes are propagated correctly in both directions,
   * both to the delegate and up to a node that contains the operated on object as a delegate.
   */
  @Test
  void test_threeLevel_delegation_dynamicAddThenSetValue()
  {
    // source → levelA (UpdateableDelegatingNode) → levelB (UpdateableDelegatingNode)
    Hierarchy<DummyModel> sourceHierarchy = new Hierarchy<>("dummy", new DummyModel());
    sourceHierarchy.getValue().getPit().setValue(DummyModel.subModels, new DummyModel.SubModelContainer());

    Hierarchy<DummyModel> levelAHierarchy = new DelegatingHierarchy<DummyModel>(
        sourceHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode)
    )
    {
    };
    Hierarchy<DummyModel> levelBHierarchy = new DelegatingHierarchy<DummyModel>(
        levelAHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode)
    )
    {
    };
    Hierarchy<DummyModel> levelCHierarchy = new DelegatingHierarchy<DummyModel>(
        levelBHierarchy,
        (pHierarchy, pSourceNode) -> new UpdateableDelegatingNode(pHierarchy, null, pSourceNode)
    )
    {
    };

    DummyModel srcModel = sourceHierarchy.getValue();
    DummyModel levelBModel = levelBHierarchy.getValue();
    DummyModel levelCModel = levelCHierarchy.getValue();

    IProperty<DummyModel.SubModelContainer, SubModel> levelBContainer = levelBModel.getValue(DummyModel.subModels).addProperty("prop", new SubModel());
    assertNotNull(levelBContainer);

    DummyModel.SubModelContainer levelCContainer = levelCModel.getValue(DummyModel.subModels);
    assertNotNull(levelCContainer);
    assertEquals(1, levelCContainer.getValues().size(),
                 "levelC should have the dynamically added SubModel in its container");

    DummyModel.SubModelContainer srcContainer = srcModel.getValue(DummyModel.subModels);
    assertNotNull(srcContainer);
    assertEquals(1, srcContainer.getValues().size(),
                 "Source should have the dynamically added SubModel in its container");
  }

  @Test
  void test_delegate_static_override()
  {
    SubModel submodel = updateableModel.setValue(DummyModel.staticSubModel, new SubModel());
    assertNotNull(submodel);
    submodel.setValue(SubModel.subModelProperty, "something");
    assertEquals("something", submodel.getValue(SubModel.subModelProperty));

    submodel = updateableModel.setValue(DummyModel.staticSubModel, new SubModel());
    assertNotNull(submodel);
    Assertions.assertNull(submodel.getValue(SubModel.subModelProperty));
    submodel.setValue(SubModel.subModelProperty, "something");
    assertEquals("something", submodel.getValue(SubModel.subModelProperty));
  }

  /**
   * An UpdateableDelegatingNode that calls setValue() from getValue() on the first invocation,
   * simulating a lazy loading pattern that might trigger Listeners before the node causing the setValue() (by calling getValue())
   * is fully crated
   */
  private static class _LazyLoadDelegatingNode extends UpdateableDelegatingNode
  {
    private boolean valueRetrieved = false;

    protected _LazyLoadDelegatingNode(@NotNull DelegatingHierarchy pHierarchy, AbstractNode pParent, @NotNull INode pDelegate)
    {
      super(pHierarchy, pParent, pDelegate);
    }

    @Override
    @Nullable
    public Object getValue()
    {
      Object value = super.getValue();
      if (!valueRetrieved)
      {
        valueRetrieved = true;
        // the _runWithoutWriteThrough() call is necessary since it makes sure that
        // DelegatingNode#setValue doesn't clear all the listeners and therefore
        // prevent the issue of triggering listeners while creating the object
        _runWithoutWriteThrough(() -> setValue(value, new HashSet<>()));
      }
      return value;
    }

    @Override
    @NotNull
    protected DelegatingNode createChild(@NotNull INode pDelegate)
    {
      return new _LazyLoadDelegatingNode(getHierarchy(), this, pDelegate);
    }
  }

  public static class DummyModel extends AbstractPPP<IPropertyPitProvider<?, ?, ?>, DummyModel, Object>
  {
    public static final IPropertyDescription<DummyModel, String> simpleStringProperty = PD.create(DummyModel.class);

    public static final IPropertyDescription<DummyModel, SubModel> staticSubModel = PD.create(DummyModel.class);

    public static final IPropertyDescription<DummyModel, SubModelContainer> subModels = PD.create(DummyModel.class);

    public static final IPropertyDescription<DummyModel, SubModelIndexedContainer> subModelsIndexed = PD.create(DummyModel.class);

    public static class SubModelContainer extends AbstractMutablePPP<IPropertyPitProvider<?, ?, ?>, SubModelContainer, ISubModel<?>>
    {
      public SubModelContainer()
      {
        super((Class) SubModel.class);
      }

      @Override
      public String toString()
      {
        return getClass().getName();
      }
    }

    public static class SubModelIndexedContainer extends AbstractIndexedMutablePPP<IPropertyPitProvider<?, ?, ?>, SubModelIndexedContainer, ISubModel<?>>
    {
      public SubModelIndexedContainer()
      {
        super((Class) SubModel.class);
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

  public interface ISubModel<S extends IPropertyPitProvider<DummyModel.SubModelContainer, S, Object>>
      extends IPropertyPitProvider<DummyModel.SubModelContainer, S, Object>
  {
    IPropertyDescription<ISubModel, String> subModelProperty = PD.create(ISubModel.class);
  }

  public static class SubModel extends AbstractPPP<DummyModel.SubModelContainer, SubModel, Object> implements ISubModel<SubModel>
  {
    public static final IPropertyDescription<SubModel, DummyModel.SubModelContainer> subModels = PD.create(SubModel.class);

    public static final IPropertyDescription<SubModel, ISubModel> staticSubModel = PD.create(SubModel.class);

    @Override
    public String toString()
    {
      return getClass().getName();
    }
  }

  public static class OtherSubModel extends AbstractPPP<DummyModel.SubModelContainer, OtherSubModel, Object> implements ISubModel<OtherSubModel>
  {
    public static final IPropertyDescription<OtherSubModel, DummyModel.SubModelContainer> subModels = PD.create(OtherSubModel.class);

    @Override
    public String toString()
    {
      return getClass().getName();
    }
  }

}
