package de.verpalnt.propertly.guibuilder;

import de.verpalnt.propertly.core.hierarchy.Hierarchy;

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
    TProperty tProperty = Hierarchy.create("root", new TProperty());
    frame.getContentPane().add(new GuiBuilder(tProperty).build());
    frame.pack();
    frame.setVisible(true);
  }

}
