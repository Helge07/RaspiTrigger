

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Das Beispiel zeigt einen Schieberegler, eine Checkbox, eine Choicebox und
 * eine Zeichengläche. Mit Sieberegler, Checkbox und Choicebox können
 * verschiedene Eigenschaften des in der Zeichenfläche gezeichneten Strichs
 * eingestellt werden.
 * Bei der Strukturierung des Programms wurde Wert auf eine klare Trennung
 * von Datenmodell und Ansicht gelegt.
 * @author 
 */
public class FXSliderGraphicsDemo04 extends Application {
    
    private final ApplicationModel model = new ApplicationModel();
    private final ApplicationView view = new ApplicationView(model);
    
   // @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Slider Demo");
        primaryStage.setScene(new Scene(view, 300, 320));
        primaryStage.setMinWidth(280);
        primaryStage.setMinHeight(280);
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
