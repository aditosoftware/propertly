package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PropertyPit}.
 *
 * @author r.hartinger, 06.12.2022
 */
class PropertyPitTest
{
  private IPropertyPit<IPropertyPitProvider, ColoredPitProvider, Color> pit;

  /**
   * Creates a new PropertyPit before each test.
   */
  @BeforeEach
  void setUp()
  {
    IHierarchy<ColoredPitProvider> hierarchy = new VerifyingHierarchy<>(new Hierarchy<>("root", new ColoredPitProvider()));
    pit = hierarchy.getValue().getPit();
  }


  /**
   * Tests the method findProperty.
   */
  @Nested
  class FindProperty
  {

    /**
     * Tests that a property will be found by searching with {@link IPropertyDescription} and name (in given case and lower and upper case).
     */
    @Test
    void shouldFindProperty()
    {
      IPropertyDescription<ColoredPitProvider, Color> defaultColor = ColoredPitProvider.DEFAULT_COLOR;

      assertAll(() -> assertNotNull(pit.getProperty(defaultColor), "property for description is there"),
                () -> assertNotNull(pit.findProperty(defaultColor), "property for description is found"),
                () -> assertNotNull(pit.findProperty(defaultColor.getName().toLowerCase()), "property for name in lower case is found"),
                () -> assertNotNull(pit.findProperty(defaultColor.getName().toUpperCase()), "property for name in upper case is found"),
                () -> assertNotNull(pit.findProperty("dEfAuLt_CoLoR"), "property for name in weird case is found"));
    }

    /**
     * Tests that no property will be found, if the name used for searching does not exist.
     */
    @Test
    void shouldNotFindProperty()
    {
      assertNull(pit.findProperty("not_existing"));
    }
  }

  /**
   * Tests the method findPropertyWithCase.
   */
  @Nested
  class FindPropertyWithCase
  {

    /**
     * Tests that a property will be found by searching with {@link IPropertyDescription} and name (in given case and lower and upper case).
     */
    @Test
    void shouldFindProperty()
    {
      IPropertyDescription<ColoredPitProvider, Color> defaultColor = ColoredPitProvider.DEFAULT_COLOR;
      assertEquals(defaultColor.getName(), defaultColor.getName().toUpperCase(), "check that the property is full upper case");

      assertAll(() -> assertNotNull(pit.getProperty(defaultColor), "property for description is there"),
                () -> assertNotNull(pit.findPropertyWithCase(defaultColor.getName()), "property for name in upper case is found"));
    }

    /**
     * Tests that no property will be found, if the name used for searching does not exist.
     */
    @ParameterizedTest
    @ValueSource(strings = {"default_color", "dEfAuLt_CoLoR", "not_existing"})
    void shouldNotFindProperty(@NotNull String pName)
    {
      assertNull(pit.findPropertyWithCase(pName));
    }
  }


}