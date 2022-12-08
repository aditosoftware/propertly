package de.adito.propertly.core.api;

import de.adito.propertly.test.core.impl.ColoredPitProvider;
import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NodeChildren}.
 *
 * @author r.hartinger, 07.12.2022
 */
class NodeChildrenTest
{
  private NodeChildren nodeChildren;

  /**
   * Creates before each test the node children with two elements. These elements have the names {@code MyFirstNode} and {@code AnotherNode}.
   */
  @BeforeEach
  void setUp()
  {
    nodeChildren = new NodeChildren();
    nodeChildren.add(createSimpleNode("MyFirstNode"));
    nodeChildren.add(createSimpleNode("AnotherNode"));
  }

  /**
   * Utility method for creating a simple node with the given name.
   *
   * @param pName the name of the node
   * @return the created node
   */
  @NotNull
  private INode createSimpleNode(@NotNull String pName)
  {
    Hierarchy<ColoredPitProvider> hierarchyColored = new Hierarchy<>(pName, new ColoredPitProvider());
    return new Node(hierarchyColored, null, hierarchyColored.getProperty().getDescription(), false);
  }


  /**
   * Tests the method find.
   */
  @Nested
  class Find
  {

    /**
     * The given arguments (names in normal case) should find an element.
     *
     * @param pName the name of the element
     */
    @ParameterizedTest
    @ValueSource(strings = {"MyFirstNode", "AnotherNode"})
    void shouldFind(@NotNull String pName)
    {
      assertNotNull(nodeChildren.find(pName));
    }

    /**
     * The given arguments (other cases, other values, null and empty) should not find an element.
     *
     * @param pName the name of the element
     */
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"myfirstnode", "MYFIRSTNODE", "anothernode", "ANOTHERNODE", "SomeOtherNode", "DEFAULT_COLOR"})
    void shouldNotFind(@Nullable String pName)
    {
      assertNull(nodeChildren.find(pName));
    }
  }
}