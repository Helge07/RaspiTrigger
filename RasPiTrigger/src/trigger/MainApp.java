package trigger;

// ********************************************************************
// RasPiTrigger - MainApp.Java
// Interactive control for the RasPiTrigger 
// Copyright: Herbert Kopp
// Last Modification:  25.02.2020
//********************************************************************

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trigger.model.TriggerProject;
import trigger.utilities.TriggerAction;

public class MainApp extends Application {

    public  Stage primaryStage;
    private static BorderPane rootLayout;
    
    public static TriggerProject TProject = new TriggerProject("defaultProject");
    
    // The first dimension must be > number of variables of the class TriggerProject
	// TPVariables[0][1] = number of TProject instance variables
    public static String[][] TPVariables = new String[50][2];	
    
    public static GpioController gpio;
    public static GpioPinDigitalOutput outPort0; 
    public static GpioPinDigitalOutput outPort1; 
    public static GpioPinDigitalOutput outPort2; 
    public static GpioPinDigitalOutput outPort3; 
    public static GpioPinDigitalOutput outPort4; 
    public static GpioPinDigitalOutput outPort5; 
    public static GpioPinDigitalOutput outPort6; 
    public static GpioPinDigitalOutput outPort7; 
    
    public static GpioPinDigitalInput inPort_Start;
    public static GpioPinDigitalInput inPort_Sensor1;
        
	// 'Enable output' Checkbox
    public static Boolean EnableOutput=true;

    // Constructor of class MainApp
    public MainApp() {        }
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
    	
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RasPiTrigger");

        initRootLayout();				// initialize stage and the scene
        showTriggerControlPanel();		// Set TriggerControlPanel into the scene  
        InitializeGPIO();				// initialize necessary input and output Ports
        
		// prepare to free all reserved GPIO Ports when closing the program
        primaryStage.setOnCloseRequest(e -> {
        
	        if (System.getProperty("os.name").equals("Linux")) {
		    	// Exit handling 
			        inPort_Start.removeAllListeners();
			        inPort_Sensor1.removeAllListeners();
			        gpio.shutdown();
			        this.primaryStage.close();
			        e.consume();
			        System.exit(0);
			}	// end if "os.name" == "Linux"
        });		// end of setOnCloseRequest
    }
    
    // Initializes the root layout
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            //scene.getStylesheets().add("trigger/stylesheet.css");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.DECORATED);
            //primaryStage.setOnCloseRequest(e->e.consume());
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Put TriggerControlPanel into the scene
    public static void showTriggerControlPanel() {
        try {
            // Load Trigger Control Panel from TriggerControlPanel.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TriggerControlPanel.fxml"));
            AnchorPane triggerControl = (AnchorPane) loader.load();
            
            // Put the trigger control panel into the scene
           rootLayout.setCenter(triggerControl);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // InitializeGPIO
    // Initialize the input and output ports
    public static void InitializeGPIO() throws InterruptedException {
    	
    	
        // Initialize the GPIO ports
    	
    	// output Ports:
        // portname:		GPIO-Name   wiringPi	Board-Name		Device
    	// ---------        --------    --------    ----------		------
        // outPort0:		GPIO_00 	0	        GPIO17			Camera
        // outPort1:		GPIO_01 	1	        GPIO18			flash1
        // outPort2:		GPIO_02 	2	        GPIO27			flash2
        // outPort3:		GPIO_03 	3	        GPIO22			flash3
        // outPort4:		GPIO_04		4	        GPIO23			----
        // outPort5:		GPIO_05		6	        GPIO24			valve1
        // outPort6:		GPIO_06  	6	        GPIO25			valve2
        // outPort7:		GPIO_07  	7	        GPIO04			valve3	
        
        // input Ports:
        // portname:		GPIO-Name   wiringPi	Board-Name
    	// ---------        --------    --------    ----------
        // inPort_Start		GPIO24	  	12	        GPIO19			START
       	// inPort_Sensor1	GPIO25	  	16	        GPIO26   		SENSOR1

		// register change listeners for the START pin and SENSOR1 pin
		// -----------------------------------------------------------
		if (System.getProperty("os.name").equals("Linux")) {

		    // create gpio controller
			gpio =  GpioFactory.getInstance();
			
		    // Setup GPIO_00, ..., GPIO_06, GPIO26 as outPort0 ... outPort7
		    outPort0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);	// Board name:  GPIO17
		    outPort1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);	// Board name:  GPIO18	
		    outPort2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);	// Board name:  GPIO27
		    outPort3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);	// Board name:  GPIO22
		    outPort4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);	// Board name:  GPIO23
		    outPort5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);	// Board name:  GPIO24
		    outPort6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);	// Board name:  GPIO25
		    outPort7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);	// Board name:  GPIO04
		    
		    // set shutdown options for all pins
		    outPort0.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort4.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort5.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort6.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    outPort7.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    
 		    // Setup output ports
		    MainApp.outPort0.low();
		    MainApp.outPort1.low();
		    MainApp.outPort2.low();
		    MainApp.outPort3.low();
		    MainApp.outPort4.low();
		    MainApp.outPort5.low();
		    MainApp.outPort6.low();
		    MainApp.outPort7.low();
		    
		    
		    
			// Setup input ports
			inPort_Start = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, PinPullResistance.PULL_DOWN);		// Board name:  GPIO19
			inPort_Sensor1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);		// Board name:  GPIO26
			
			inPort_Start.setDebounce(200);
			inPort_Sensor1.setDebounce(200);
		
		    inPort_Start.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    inPort_Sensor1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		    
		    // create and register gpio pin listener for inPort_Start
			inPort_Start.addListener(new GpioPinListenerDigital() {
				@Override
	            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					if (event.getState() == PinState.HIGH) {
						TriggerAction triggerAction = new TriggerAction();
		            	triggerAction.setupTriggerActionList();
		            	try {
							triggerAction.Trigger();
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
			}
			});
	    
			// create and register gpio pin listener for inPort_Sensor1
			inPort_Sensor1.addListener(new GpioPinListenerDigital() {
	            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
						if (event.getState() == PinState.HIGH) {
		            	TriggerAction triggerAction = new TriggerAction();
		            	triggerAction.setupTriggerActionList();
		            	try {
							triggerAction.Trigger();
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
	            }
			});
		}
    }

     //Returns the main stage.
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}


