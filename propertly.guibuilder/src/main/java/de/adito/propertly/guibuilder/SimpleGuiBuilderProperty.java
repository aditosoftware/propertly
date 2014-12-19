package de.adito.propertly.guibuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * @author PaL
 *         Date: 10.02.13
 *         Time: 20:13
 */
public class SimpleGuiBuilderProperty implements IGuiBuilderProperty<Object>
{

  private final TextField textField;
  private Object value;
  private Runnable callback;

  public SimpleGuiBuilderProperty()
  {
    textField = new TextField();
    textField.focusedProperty().addListener(new ChangeListener<Boolean>()
    {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
      {
        if (!newValue)
          valueChanged(textField.getText());
      }
    });
    textField.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent pActionEvent)
      {
        valueChanged(textField.getText());
      }
    });
  }

  @Override
  public Class<Object> getSupportedType()
  {
    return Object.class;
  }

  @Override
  public Object getValue()
  {
    return value;
  }

  @Override
  public void setValue(Object pValue)
  {
    value = pValue;
    updateComponent();
  }

  @Override
  public void setValueChangedCallback(Runnable pOnValueChanged)
  {
    callback = pOnValueChanged;
  }

  @Override
  public Node getComponent()
  {
    return textField;
  }

  protected void updateComponent()
  {
    String s = valueToString(value);
    textField.setText(s == null ? "" : s);
  }

  protected void valueChanged(String pValue)
  {
    try
    {
      value = stringToValue(pValue);
      callback.run();
    }
    catch (Exception e)
    {
      updateComponent();
    }
  }

  protected String valueToString(Object pValue)
  {
    return pValue == null ? null : pValue.toString();
  }

  protected Object stringToValue(String pValueAsString)
  {
    return pValueAsString;
  }

}
