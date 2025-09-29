package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.ColoredPitProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;

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
   * Tests {@link NodeChildren#clear()}.
   */
  @Nested
  class Clear
  {
    /**
     * Ensures that {@link NodeChildren#clear()} removes all elements and leaves no iteration results.
     */
    @Test
    void shouldRemoveAllElements()
    {
      nodeChildren.clear();

      assertAll(
          () -> assertTrue(nodeChildren.asList().isEmpty(), "asList must be empty after clear"),
          () -> assertFalse(nodeChildren.iterator().hasNext(), "iterator must have no next after clear")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#add(INode)} and {@link NodeChildren#add(Integer, INode)}.
   */
  @Nested
  class Add
  {
    /**
     * Verifies that {@link NodeChildren#add(INode)} appends a unique element at the end.
     */
    @Test
    void shouldAppendUnique()
    {
      INode third = createSimpleNode("Third");
      nodeChildren.add(third);

      assertAll(
          () -> assertEquals(3, nodeChildren.asList().size(), "size must be 3 after append"),
          () -> assertEquals(third, nodeChildren.get(2), "appended element must be at last index"),
          () -> assertNotNull(nodeChildren.find("Third"), "find must return appended element by name")
      );
    }

    /**
     * Verifies that {@link NodeChildren#add(INode)} ignores duplicate names without modifying the list.
     */
    @Test
    void shouldIgnoreDuplicate()
    {
      nodeChildren.add(createSimpleNode("MyFirstNode"));

      assertAll(
          () -> assertEquals(2, nodeChildren.asList().size(), "size must stay 2 when duplicate is added"),
          () -> assertEquals("MyFirstNode", nodeChildren.get(0).getProperty().getName(), "first element must remain MyFirstNode"),
          () -> assertEquals("AnotherNode", nodeChildren.get(1).getProperty().getName(), "second element must remain AnotherNode")
      );
    }

    /**
     * Inserts a node at a specific index using {@link NodeChildren#add(Integer, INode)}.
     */
    @Test
    void shouldInsertAtIndex()
    {
      INode inserted = createSimpleNode("Inserted");
      nodeChildren.add(0, inserted);

      assertAll(
          () -> assertEquals(3, nodeChildren.asList().size(), "size must be 3 after insert"),
          () -> assertEquals(inserted, nodeChildren.get(0), "inserted node must be at index 0"),
          () -> assertEquals("MyFirstNode", nodeChildren.get(1).getProperty().getName(), "former index 0 must move to 1"),
          () -> assertEquals("AnotherNode", nodeChildren.get(2).getProperty().getName(), "former index 1 must move to 2")
      );
    }

    /**
     * Verifies {@link NodeChildren#add(Integer, INode)} throws on out-of-bounds index.
     */
    @Test
    void shouldThrowOnOutOfBounds()
    {
      INode node = createSimpleNode("X");

      assertAll(
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.add(-1, node), "negative index must throw"),
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.add(5, node), "too large index must throw")
      );
    }

    /**
     * Verifies that {@link NodeChildren#add(Integer, INode)} ignores a duplicate name.
     */
    @Test
    void shouldIgnoreDuplicateName()
    {
      nodeChildren.add(0, createSimpleNode("AnotherNode"));

      assertAll(
          () -> assertEquals(2, nodeChildren.asList().size(), "size must remain 2 when duplicate is inserted"),
          () -> assertEquals("MyFirstNode", nodeChildren.get(0).getProperty().getName(), "order must remain unchanged at index 0"),
          () -> assertEquals("AnotherNode", nodeChildren.get(1).getProperty().getName(), "order must remain unchanged at index 1")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#remove(INode)} and {@link NodeChildren#remove(int)}
   */
  @Nested
  class Remove
  {
    /**
     * Ensures {@link NodeChildren#remove(INode)} deletes an existing node and updates lookup.
     */
    @Test
    void shouldRemoveExisting()
    {
      INode victim = nodeChildren.get(0);
      boolean removed = nodeChildren.remove(victim);

      assertAll(
          () -> assertTrue(removed, "remove must return true for existing node"),
          () -> assertNull(nodeChildren.find("MyFirstNode"), "old name must no longer be found"),
          () -> assertEquals(1, nodeChildren.asList().size(), "size must be 1 after removal"),
          () -> assertEquals("AnotherNode", nodeChildren.get(0).getProperty().getName(), "remaining element must be AnotherNode")
      );
    }

    /**
     * Ensures {@link NodeChildren#remove(INode)} returns false for foreign nodes and keeps list unchanged.
     */
    @Test
    void shouldReturnFalseForForeignNode()
    {
      boolean removed = nodeChildren.remove(createSimpleNode("Foreign"));

      assertAll(
          () -> assertFalse(removed, "remove must return false for foreign node"),
          () -> assertEquals(2, nodeChildren.asList().size(), "size must remain 2")
      );
    }

    /**
     * Removes a node by index using {@link NodeChildren#remove(int)}.
     */
    @Test
    void shouldRemoveAtIndex()
    {
      nodeChildren.remove(0);

      assertAll(
          () -> assertEquals(1, nodeChildren.asList().size(), "size must be 1 after removal"),
          () -> assertNull(nodeChildren.find("MyFirstNode"), "removed element must not be found"),
          () -> assertNotNull(nodeChildren.find("AnotherNode"), "other element must still be present")
      );
    }

    /**
     * Verifies {@link NodeChildren#remove(int)} throws for invalid index.
     */
    @Test
    void shouldThrowOnInvalidIndex()
    {
      assertAll(
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.remove(-1), "negative index must throw"),
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.remove(2), "too large index must throw")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#rename(IPropertyDescription, String)}.
   */
  @Nested
  class Rename
  {
    private INode existingNode;
    private IPropertyDescription<?, ?> existingNodeDescription;

    @BeforeEach
    void setUp()
    {
      // build index map first to satisfy internal assertion in rename()
      existingNode = nodeChildren.find("MyFirstNode");
      assertNotNull(existingNode, "precondition: node must exist");
      existingNodeDescription = existingNode.getProperty().getDescription();
      assertTrue(nodeChildren.indexOf(existingNodeDescription) >= 0, "precondition: index must be known");
    }

    /**
     * Renames an element using {@link NodeChildren#rename(IPropertyDescription, String)} and keeps the same node.
     */
    @Test
    void shouldRenameToNewName()
    {
      nodeChildren.rename(existingNodeDescription, "Renamed");

      assertAll(
          () -> assertNull(nodeChildren.find("MyFirstNode"), "old name must no longer resolve"),
          () -> assertEquals(existingNode, nodeChildren.find("Renamed"), "new name must resolve to same node instance")
      );
    }

    /**
     * Verifies {@link NodeChildren#rename(IPropertyDescription, String)} throws on duplicate target name.
     */
    @Test
    void shouldThrowOnDuplicateTarget()
    {
      assertThrows(RuntimeException.class,
                   () -> nodeChildren.rename(existingNodeDescription, "AnotherNode"),
                   "renaming to existing name must throw");
    }

    /**
     * Ensures the index for the old description is unknown after {@link NodeChildren#rename(IPropertyDescription, String)}.
     */
    @Test
    void indexOfWithOldDescriptionShouldBeUnknownAfterRename()
    {
      int before = nodeChildren.indexOf(existingNodeDescription);
      nodeChildren.rename(existingNodeDescription, "Renamed");

      assertAll(
          () -> assertTrue(before >= 0, "precondition: index must be known before rename"),
          () -> assertEquals(-1, nodeChildren.indexOf(existingNodeDescription), "old description must no longer resolve to an index")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#indexOf(IPropertyDescription)}.
   */
  @Nested
  class IndexOf
  {
    /**
     * Existing descriptions should return their current indices in {@link NodeChildren#indexOf(IPropertyDescription)}.
     */
    @Test
    void shouldReturnIndexForExisting()
    {
      IPropertyDescription<?, ?> descriptionNode0 = nodeChildren.get(0).getProperty().getDescription();
      IPropertyDescription<?, ?> descriptionNode1 = nodeChildren.get(1).getProperty().getDescription();

      assertAll(
          () -> assertEquals(0, nodeChildren.indexOf(descriptionNode0), "first description must map to index 0"),
          () -> assertEquals(1, nodeChildren.indexOf(descriptionNode1), "second description must map to index 1")
      );
    }

    /**
     * Unknown descriptions should return -1 in {@link NodeChildren#indexOf(IPropertyDescription)}.
     */
    @Test
    void shouldReturnMinusOneForUnknown()
    {
      INode foreign = createSimpleNode("Foreign");
      assertEquals(-1, nodeChildren.indexOf(foreign.getProperty().getDescription()),
                   "unknown description must return -1");
    }

    /**
     * Indices should reflect reordering after {@link NodeChildren#reorder(Comparator)}.
     */
    @Test
    void shouldReflectReorder()
    {
      nodeChildren.add(createSimpleNode("Zed"));
      nodeChildren.add(createSimpleNode("Alpha"));

      nodeChildren.reorder(Comparator.comparing(pProperty -> ((IProperty<?, ?>) pProperty).getName()));

      assertAll(
          () -> assertEquals("Alpha", nodeChildren.get(0).getProperty().getName(), "index 0 must be Alpha"),
          () -> assertEquals("AnotherNode", nodeChildren.get(1).getProperty().getName(), "index 1 must be AnotherNode"),
          () -> assertEquals("MyFirstNode", nodeChildren.get(2).getProperty().getName(), "index 2 must be MyFirstNode"),
          () -> assertEquals("Zed", nodeChildren.get(3).getProperty().getName(), "index 3 must be Zed"),
          () -> assertEquals(0, nodeChildren.indexOf(nodeChildren.get(0).getProperty().getDescription()),
                             "description of first element must resolve to index 0")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#reorder(Comparator)}.
   */
  @Nested
  class Reorder
  {
    /**
     * Sorting ascending by property name should order elements lexicographically via {@link NodeChildren#reorder(Comparator)}.
     */
    @Test
    void shouldSortAscendingByName()
    {
      nodeChildren.add(createSimpleNode("Zed"));
      nodeChildren.add(createSimpleNode("Alpha"));

      nodeChildren.reorder(Comparator.comparing(pProperty -> ((IProperty<?, ?>) pProperty).getName()));

      List<String> names = new ArrayList<>();
      for (INode child : nodeChildren)
        names.add(child.getProperty().getName());

      assertEquals(Arrays.asList("Alpha", "AnotherNode", "MyFirstNode", "Zed"), names,
                   "ascending reorder must produce expected name order");
    }

    /**
     * Sorting descending by property name should reverse order via {@link NodeChildren#reorder(Comparator)}.
     */
    @Test
    void shouldSortDescendingByName()
    {
      nodeChildren.add(createSimpleNode("Zed"));
      nodeChildren.add(createSimpleNode("Alpha"));

      nodeChildren.reorder((pProperty1, pProperty2) -> {
        String nameProperty1 = ((IProperty<?, ?>) pProperty1).getName();
        String nameProperty2 = ((IProperty<?, ?>) pProperty2).getName();
        return -nameProperty1.compareTo(nameProperty2);
      });

      List<String> names = new ArrayList<>();
      for (INode child : nodeChildren)
        names.add(child.getProperty().getName());

      assertEquals(Arrays.asList("Zed", "MyFirstNode", "AnotherNode", "Alpha"), names,
                   "descending reorder must produce expected name order");
    }
  }

  /**
   * Tests {@link NodeChildren#asList()}.
   */
  @Nested
  class AsList
  {
    /**
     * Confirms {@link NodeChildren#asList()} returns an unmodifiable view that reflects current state.
     */
    @Test
    void shouldReturnUnmodifiableView()
    {
      List<INode> view = nodeChildren.asList();

      assertAll(
          () -> assertEquals(2, view.size(), "initial view size must be 2"),
          () -> assertThrows(UnsupportedOperationException.class, () -> view.add(createSimpleNode("X")), "view must be unmodifiable"),
          () -> {
            nodeChildren.add(createSimpleNode("Later"));
            assertEquals(3, nodeChildren.asList().size(), "view content must reflect underlying list changes");
          }
      );
    }
  }

  /**
   * Tests {@link NodeChildren#find(String)} and {@link NodeChildren#find(IPropertyDescription)}.
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
    void shouldNotFind(@NotNull String pName)
    {
      assertNull(nodeChildren.find(pName), "unknown or case-variant name must not resolve");
    }

    /**
     * Renaming should allow finding by the new name via {@link NodeChildren#find(String)}.
     */
    @Test
    void shouldFindAfterRename()
    {
      INode firstNode = nodeChildren.find("MyFirstNode");
      assertNotNull(firstNode, "precondition: node must exist");

      // ensure index map is built before rename (rename relies on a non-null index)
      assertTrue(nodeChildren.indexOf(firstNode.getProperty().getDescription()) >= 0, "precondition: index must be known");

      nodeChildren.rename(firstNode.getProperty().getDescription(), "Renamed");

      assertAll(
          "find by new name after rename",
          () -> assertNull(nodeChildren.find("MyFirstNode"), "old name must not resolve"),
          () -> assertEquals(firstNode, nodeChildren.find("Renamed"), "new name must resolve to same node instance")
      );
    }

    /**
     * Finds a node by matching description using {@link NodeChildren#find(IPropertyDescription)}.
     */
    @Test
    void shouldFindWithMatchingDescription()
    {
      INode firstNode = nodeChildren.get(0);
      assertEquals(firstNode, nodeChildren.find(firstNode.getProperty().getDescription()),
                   "matching description must return the corresponding node");
    }

    /**
     * Unknown description name should not return a node in {@link NodeChildren#find(IPropertyDescription)}.
     */
    @Test
    void shouldNotFindWithUnknownDescription()
    {
      INode foreign = createSimpleNode("Foreign");
      assertNull(nodeChildren.find(foreign.getProperty().getDescription()),
                 "unknown description must not resolve to an existing node");
    }
  }

  /**
   * Tests {@link NodeChildren#get(int)}.
   */
  @Nested
  class Get
  {
    /**
     * A valid index should return the node at that position via {@link NodeChildren#get(int)}.
     */
    @Test
    void shouldReturnNodeAtIndex()
    {
      assertAll(
          () -> assertEquals("MyFirstNode", nodeChildren.get(0).getProperty().getName(), "index 0 must be MyFirstNode"),
          () -> assertEquals("AnotherNode", nodeChildren.get(1).getProperty().getName(), "index 1 must be AnotherNode")
      );
    }

    /**
     * An invalid index should throw an exception in {@link NodeChildren#get(int)}.
     */
    @Test
    void shouldThrowOnInvalidIndex()
    {
      assertAll(
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.get(-1), "negative index must throw"),
          () -> assertThrows(IndexOutOfBoundsException.class, () -> nodeChildren.get(2), "too large index must throw")
      );
    }
  }

  /**
   * Tests {@link NodeChildren#iterator()}.
   */
  @Nested
  class IteratorTests
  {
    /**
     * Iteration should traverse elements in list order via {@link NodeChildren#iterator()}.
     */
    @Test
    void shouldIterateInOrder()
    {
      List<String> names = new ArrayList<>();
      for (INode child : nodeChildren)
        names.add(child.getProperty().getName());
      assertEquals(Arrays.asList("MyFirstNode", "AnotherNode"), names, "iterator must traverse in list order");
    }

    /**
     * Removing via iterator must not be supported for the unmodifiable view returned by {@link NodeChildren#iterator()}.
     */
    @Test
    void shouldNotSupportRemove()
    {
      Iterator<INode> it = nodeChildren.iterator();
      it.next();
      assertThrows(UnsupportedOperationException.class, it::remove, "iterator remove must throw");
    }
  }
}