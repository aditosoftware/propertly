package de.verpalnt.propertly.test.guibuilder;

import de.verpalnt.propertly.core.hierarchy.Hierarchy;
import de.verpalnt.propertly.guibuilder.GuiBuilder;
import de.verpalnt.propertly.test.common.TProperty;

import javax.swing.*;

/**
 * @author PaL
 *         Date: 27.01.13
 *         Time: 16:03
 */
public class GuiBuilderTest
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    TProperty tProperty = Hierarchy.create("root", new TProperty());
    frame.getContentPane().add(new GuiBuilder(tProperty).build());
    frame.pack();
    frame.setVisible(true);
  }

}
