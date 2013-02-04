package de.verpalnt.propertly.guibuilder;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

/**
 * @author PaL
 *         Date: 27.01.13
 *         Time: 15:16
 */
public class GuiBuilder
{

  private IPropertyPitProvider pitProvider;

  public GuiBuilder(IPropertyPitProvider pitProvider)
  {
    this.pitProvider = pitProvider;
  }

  public JComponent build()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    List<IProperty> properties = pitProvider.getPit().getProperties();
    for (IProperty property : properties)
    {
      JComponent comp = _create(property);
      JLabel lbl = new JLabel(property.getDescription().getName());
      lbl.setLabelFor(comp);
      panel.add(lbl);
      panel.add(comp);
    }
    return panel;
  }

  private JComponent _create(final IProperty pProperty)
  {
    final JTextField field = new JTextField();
    field.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        _setValue(pProperty, field.getText());
      }
    });
    field.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        _setValue(pProperty, field.getText());
      }
    });
    field.setText(String.valueOf(pProperty.getValue()));
    return field;
  }

  private void _setValue(IProperty pProperty, Object pValue)
  {
    if (pValue == null)
      pProperty.setValue(null);

    Class type = pProperty.getDescription().getType();
    if (type == String.class)
      pProperty.setValue(pValue.toString());
    else if (type == Integer.class)
      pProperty.setValue(Integer.parseInt(pValue.toString()));
    else
      System.out.println("not supported: " + pValue + " for " + pProperty);
  }

}
