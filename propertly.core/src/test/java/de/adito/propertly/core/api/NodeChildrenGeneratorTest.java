package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PPPIntrospector;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPit;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.test.core.impl.ColoredPitProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link NodeChildrenGenerator} and the generated NodeChildren classes.
 *
 * @author j.boesl, 07.07.2026
 */
class NodeChildrenGeneratorTest
{

  /**
    * Simple test provider with a known set of static properties.
    */
  public static class TestProvider implements IPropertyPitProvider<IPropertyPitProvider, TestProvider, String>
  {
    static final IPropertyDescription<TestProvider, String> PROP_A =
        new SimplePropertyDescription<>("propA", TestProvider.class, String.class);
    static final IPropertyDescription<TestProvider, String> PROP_B =
        new SimplePropertyDescription<>("propB", TestProvider.class, String.class);
    static final IPropertyDescription<TestProvider, String> PROP_C =
        new SimplePropertyDescription<>("propC", TestProvider.class, String.class);

    private final IPropertyPit<IPropertyPitProvider, TestProvider, String> pit =
        PitFactory.getInstance().create(this);

    @NotNull
    @Override
    public IPropertyPit<IPropertyPitProvider, TestProvider, String> getPit()
    {
      return pit;
    }
  }

  @BeforeEach
  void setUp()
  {
    PPPIntrospector.clearGeneratedClassCache();
  }

  @Test
  void shouldGenerateClassSuccessfully()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);
    descriptions.add(TestProvider.PROP_C);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    assertNotNull(generatedClass);
    assertTrue(AbstractNodeChildren.class.isAssignableFrom(generatedClass),
               "Generated class must extend AbstractNodeChildren");
    assertFalse(NodeChildren.class.isAssignableFrom(generatedClass),
                "Generated class must NOT extend NodeChildren");
  }

  @Test
  void shouldInstantiateGeneratedClass()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);

    try {
      AbstractNodeChildren instance = generatedClass.getConstructor(INode[].class)
          .newInstance(new Object[]{ new INode[]{ nodeA, nodeB } });
      assertNotNull(instance);
    }
    catch (Exception e) {
      fail("Should be able to instantiate generated class: " + e.getMessage());
    }
  }

  @Test
  void shouldFindNodeByName()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);
    descriptions.add(TestProvider.PROP_C);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);
    INode nodeC = new Node(hierarchy, null, TestProvider.PROP_C, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB, nodeC);

    assertEquals(nodeA, instance.find("propA"), "Should find node by name 'propA'");
    assertEquals(nodeB, instance.find("propB"), "Should find node by name 'propB'");
    assertEquals(nodeC, instance.find("propC"), "Should find node by name 'propC'");
    assertNull(instance.find("nonExistent"), "Should return null for unknown name");
  }

  @Test
  void shouldGetNodeByIndex()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);
    descriptions.add(TestProvider.PROP_C);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);
    INode nodeC = new Node(hierarchy, null, TestProvider.PROP_C, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB, nodeC);

    assertEquals(nodeA, instance.get(0), "Index 0 must be nodeA");
    assertEquals(nodeB, instance.get(1), "Index 1 must be nodeB");
    assertEquals(nodeC, instance.get(2), "Index 2 must be nodeC");
  }

  @Test
  void shouldReturnCorrectIndexOf()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);
    descriptions.add(TestProvider.PROP_C);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);
    INode nodeC = new Node(hierarchy, null, TestProvider.PROP_C, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB, nodeC);

    assertEquals(0, instance.indexOf(TestProvider.PROP_A), "propA must be at index 0");
    assertEquals(1, instance.indexOf(TestProvider.PROP_B), "propB must be at index 1");
    assertEquals(2, instance.indexOf(TestProvider.PROP_C), "propC must be at index 2");
  }

  @Test
  void shouldReturnAsList()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB);

    List<INode> list = instance.asList();
    assertEquals(2, list.size(), "List size must be 2");
    assertEquals(nodeA, list.get(0), "First element must be nodeA");
    assertEquals(nodeB, list.get(1), "Second element must be nodeB");
  }

  @Test
  void shouldClear()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB);

    instance.clear();
    assertNull(instance.find("propA"), "propA must not be found after clear");
    assertNull(instance.find("propB"), "propB must not be found after clear");
    assertTrue(instance.asList().isEmpty(), "asList must be empty after clear");
  }

  @Test
  void shouldIterateInOrder()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);
    descriptions.add(TestProvider.PROP_B);
    descriptions.add(TestProvider.PROP_C);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);
    INode nodeB = new Node(hierarchy, null, TestProvider.PROP_B, false);
    INode nodeC = new Node(hierarchy, null, TestProvider.PROP_C, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA, nodeB, nodeC);

    List<INode> collected = new ArrayList<>();
    for (INode node : instance) {
      collected.add(node);
    }

    assertEquals(3, collected.size(), "Iterator must traverse 3 elements");
    assertEquals(nodeA, collected.get(0), "First element must be nodeA");
    assertEquals(nodeB, collected.get(1), "Second element must be nodeB");
    assertEquals(nodeC, collected.get(2), "Third element must be nodeC");
  }

  @Test
  void shouldThrowOnAdd()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA);

    assertThrows(UnsupportedOperationException.class, () -> instance.add(nodeA),
                "add(INode) must throw UnsupportedOperationException");
  }

  @Test
  void shouldThrowOnRemove()
  {
    Set<IPropertyDescription> descriptions = new LinkedHashSet<>();
    descriptions.add(TestProvider.PROP_A);

    Class<? extends AbstractNodeChildren> generatedClass = NodeChildrenGenerator.generate(descriptions);

    Hierarchy<TestProvider> hierarchy = new Hierarchy<>("test", new TestProvider());
    INode nodeA = new Node(hierarchy, null, TestProvider.PROP_A, false);

    AbstractNodeChildren instance = createInstance(generatedClass, nodeA);

    assertThrows(UnsupportedOperationException.class, () -> instance.remove(nodeA),
                "remove(INode) must throw UnsupportedOperationException");
    assertThrows(UnsupportedOperationException.class, () -> instance.remove(0),
                "remove(int) must throw UnsupportedOperationException");
  }

  @Test
  void shouldCacheGeneratedClass()
  {
    Class<? extends AbstractNodeChildren> cls1 = PPPIntrospector.getGeneratedClass(ColoredPitProvider.class);
    Class<? extends AbstractNodeChildren> cls2 = PPPIntrospector.getGeneratedClass(ColoredPitProvider.class);

    assertSame(cls1, cls2, "Generated class must be cached per provider class");
  }

  @Test
  void shouldWorkWithColoredPitProvider()
  {
    // Verify that the generated class works with the real ColoredPitProvider
    Class<? extends AbstractNodeChildren> generatedClass = PPPIntrospector.getGeneratedClass(ColoredPitProvider.class);
    assertNotNull(generatedClass, "Generated class must not be null");

    Set<IPropertyDescription> descriptions = PPPIntrospector.get(ColoredPitProvider.class);
    assertFalse(descriptions.isEmpty(), "ColoredPitProvider must have static properties");

    // Instantiate with actual nodes
    Hierarchy<ColoredPitProvider> hierarchy = new Hierarchy<>("test", new ColoredPitProvider());
    List<INode> nodes = new ArrayList<>();
    for (IPropertyDescription desc : descriptions) {
      nodes.add(new Node(hierarchy, null, desc, false));
    }

    AbstractNodeChildren instance = createInstance(generatedClass, nodes.toArray(new INode[0]));
    assertNotNull(instance, "Instance must not be null");

    // Verify find works for each property
    for (INode node : nodes) {
      String name = node.getProperty().getName();
      INode found = instance.find(name);
      assertNotNull(found, "Must find node by name: " + name);
      assertEquals(node, found, "Found node must be the same instance");
    }
  }

  // --- Helper methods ---

  private AbstractNodeChildren createInstance(Class<? extends AbstractNodeChildren> pClass, INode... pNodes)
  {
    try {
      return pClass.getConstructor(INode[].class).newInstance(new Object[]{ pNodes });
    }
    catch (Exception e) {
      fail("Should be able to instantiate: " + e.getMessage());
      return null;
    }
  }

  // --- Helper: Simple property description for testing ---

  private static class SimplePropertyDescription<S extends IPropertyPitProvider, V>
      implements IPropertyDescription<S, V>
  {
    private final String name;
    private final Class<S> sourceType;
    private final Class<? extends V> type;

    SimplePropertyDescription(String pName, Class<S> pSourceType, Class<? extends V> pType)
    {
      this.name = pName;
      this.sourceType = pSourceType;
      this.type = pType;
    }

    @Override
    public Class<S> getSourceType()
    {
      return sourceType;
    }

    @Override
    public Class<? extends V> getType()
    {
      return type;
    }

    @Override
    public String getName()
    {
      return name;
    }

    @Override
    public IPropertyDescription<S, V> copy(String pNewName)
    {
      return new SimplePropertyDescription<>(pNewName, sourceType, type);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> pAnnotationType)
    {
      return null;
    }

    @Override
    public Annotation[] getAnnotations()
    {
      return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
      return new Annotation[0];
    }
  }
}
