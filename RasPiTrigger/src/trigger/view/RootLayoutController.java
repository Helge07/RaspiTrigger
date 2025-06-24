package trigger.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// The controller for the root layout. The root layout provides the basic   
// application layout containing a menu bar and space where other JavaFX     
// elements can be placed.                                                    

public class RootLayoutController {
    // Creates a new default configuration 
	
    @FXML
    private void handleLoadDefaults() throws IOException {
    }
    
    // Creates a new default configuration 
    @FXML
    private void handleExit() {
    	System.exit(0);
    }
    
    // Creates a new default configuration 
    @FXML
    private void handleHelp() {
    }
    
    // Creates a new default configuration 
    @FXML
    private void handleAbout() {
    	
    	// create an alert 
        Alert a = new Alert(AlertType.NONE); 
        a.setAlertType(AlertType.INFORMATION);
        a.setTitle("About RasPiTrigger");
        a.setHeaderText ("RasPiTrigger" + "\n" + "v1.0  February 2020" + "\n" + "Author: Herbert Kopp");
        a.setContentText("This application can be used and distributed only for non-commercial purposes and with appropriate attributions.\r\n" + "\r\n" + 
        		"See Creative Commons Attribution - Noncommercial - No Derivative Works 3.0:  http://creativecommons.org/licenses/by-nc-nd/3.0/" + "\n");
        a.getDialogPane().setMinHeight(250.0);
        a.getDialogPane().setMinWidth(800.0);
        
        // show the dialog 
        a.show(); 
    	
    }
}