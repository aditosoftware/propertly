package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.*;
import org.junit.jupiter.api.*;

import java.util.*;

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
    Assertions.assertNotNull(sourceModel);
    Assertions.assertNotNull(updateableModel);

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
  void test_source_dynamic_initWithPPP()
  {
    //precreate a model to add afterwards
    List<String> addedModelNames = new ArrayList<>();
    SubModel preSubModel = new Hierarchy<>(UUID.randomUUID().toString(), new SubModel()).getValue();
    DummyModel.SubModelContainer preSubSubModels = preSubModel.setValue(SubModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(preSubSubModels);
    for(int i = 0; i < 20; i++)
      addedModelNames.add(preSubSubModels.addProperty(UUID.randomUUID().toString(), new SubModel()).getName());

    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(preSubModel).getValue();
    Assertions.assertNotNull(subModel);
    DummyModel.SubModelContainer subSubModels = subModel.setValue(SubModel.subModels, preSubSubModels);
    Assertions.assertNotNull(subSubModels);

    for (String name : addedModelNames)
    {
      Assertions.assertNotNull(subSubModels.findProperty(name));
      Assertions.assertTrue(Objects.requireNonNull(subSubModels.findProperty(name)).isValid());
    }
  }

  @Test
  void test_source_dynamic_nestedChange()
  {
    DummyModel.SubModelContainer container = sourceModel.setValue(DummyModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(container);
    SubModel subModel = container.addProperty(new SubModel()).getValue();
    Assertions.assertNotNull(subModel);
    DummyModel.SubModelContainer subSubModels = subModel.setValue(SubModel.subModels, new DummyModel.SubModelContainer());
    Assertions.assertNotNull(subSubModels);


    List<String> addedModelNames = new ArrayList<>();
    for(int i = 0; i < 20; i++)
      addedModelNames.add(subSubModels.addProperty(new SubModel()).getName());

    Assertions.assertEquals(sourceHierarchyListener.asString(), updateableHierarchyListener.asString());
    for (String name : addedModelNames)
    {
      Assertions.assertNotNull(subSubModels.findProperty(name));
      Assertions.assertTrue(Objects.requireNonNull(subSubModels.findProperty(name)).isValid());
    }
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
