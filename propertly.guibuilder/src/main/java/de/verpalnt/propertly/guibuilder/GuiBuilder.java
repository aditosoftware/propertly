package de.verpalnt.propertly.guibuilder;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PropertyEventAdapter;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

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

  public Parent build()
  {
    FlowPane pane = new FlowPane(Orientation.VERTICAL);

    List<IProperty> properties = pitProvider.getPit().getProperties();
    for (IProperty property : properties)
    {
      IGuiBuilderProperty guiBuilderProperty = _createGuiBuilderProperty(property);
      Node node = guiBuilderProperty.getComponent();
      Label lbl = new Label(property.getDescription().getName());
      pane.getChildren().add(lbl);
      pane.getChildren().add(node);
    }
    return pane;
  }


  private IGuiBuilderProperty _createGuiBuilderProperty(final IProperty pProperty)
  {
    final IGuiBuilderProperty guiBuilderProperty;
    Class type = pProperty.getType();
    if (type == String.class)
      guiBuilderProperty = new SimpleGuiBuilderProperty();
    else if (type == Integer.class)
      guiBuilderProperty = new SimpleGuiBuilderProperty()
      {
        @Override
        protected Object stringToValue(String pValueAsString)
        {
          return Integer.parseInt(pValueAsString);
        }
      };
    else
      guiBuilderProperty = new SimpleGuiBuilderProperty();

    guiBuilderProperty.setValue(pProperty.getValue());
    guiBuilderProperty.setValueChangedCallback(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          pProperty.setValue(guiBuilderProperty.getValue());
        }
        catch (Exception e)
        {
          guiBuilderProperty.setValue(pProperty.getValue());
        }
      }
    });
    pProperty.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        guiBuilderProperty.setValue(pNewValue);
      }
    });
    return guiBuilderProperty;
  }

}
