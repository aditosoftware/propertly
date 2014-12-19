package de.adito.propertly.test.guibuilder;

import de.adito.propertly.core.api.IPropertyDescription;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.hierarchy.Hierarchy;
import de.adito.propertly.guibuilder.GuiBuilder;
import de.adito.propertly.test.common.*;
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
    Hierarchy<_TP> hierarchy = new VerifyingHierarchy<_TP>(new Hierarchy<_TP>("root", new _TP()));
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
