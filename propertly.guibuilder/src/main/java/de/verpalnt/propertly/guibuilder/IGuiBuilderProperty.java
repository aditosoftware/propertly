package de.verpalnt.propertly.guibuilder;

import javafx.scene.Node;

/**
 * @author PaL
 *         Date: 10.02.13
 *         Time: 18:38
 */
public interface IGuiBuilderProperty<T>
{

  Class<T> getSupportedType();

  T getValue();

  void setValue(T pValue);

  void setValueChangedCallback(Runnable pOnValueChanged);

  Node getComponent();

}
