package de.verpalnt.propertly.test.guibuilder;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.common.PD;
import de.verpalnt.propertly.core.hierarchy.Hierarchy;
import de.verpalnt.propertly.guibuilder.GuiBuilder;
import de.verpalnt.propertly.test.common.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 * @author PaL
 *         Date: 27.01.13
 *         Time: 16:03
 */
public class GuiBuilderTest extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }


  @Override
  public void start(Stage pStage) throws Exception
  {
    Hierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new _TP()));
    Parent parent = new GuiBuilder(hierarchy.getValue()).build();
    Scene appScene = new Scene(parent);
    pStage.setScene(appScene);
    pStage.show();
  }

  public static class _TP extends TProperty
  {
    @IntVerifier(minValue = 100)
    public static final IPropertyDescription<_TP, Integer> Y = PD.create(_TP.class);
  }

}
