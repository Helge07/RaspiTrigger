package trigger.view;

import trigger.MainApp;
import trigger.model.TriggerProject;
import trigger.model.TriggerProject.ctrl;
import trigger.model.TriggerProject.trigger;
import trigger.utilities.TriggerAction;
import trigger.utilities.utilities;
import trigger.model.TriggerProject.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.jar.JarException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;

public class TriggerControlPanelController {
	
	Boolean isLinux=true;
	
	// Parameters for camera and flash settings 
    @FXML
    private ChoiceBox<String> TimeScale1;
    @FXML
    private ChoiceBox<String> TimeScale2;
    @FXML
    private ChoiceBox<String> TimeScale3;

    // Integer Spinner Fields
    @FXML
    private Spinner<Integer> ShutterLagSpinner;
    @FXML
    private Spinner<Integer> ShutterSpeedSpinner;
    @FXML
    private Spinner<Integer> TimeToFlashSpinner ;
    @FXML
    private Spinner<Integer> ValveNumberSpinner ;
    @FXML
    private Spinner<Integer> FlashNumberSpinner;
    
    // Labels for valve parameter columns
    @FXML
    private Label numberDropsLabel;
    @FXML 
    private Label closed1Label;
    @FXML 
    private Label open1Label;
    @FXML 
    private Label closed2Label;
    @FXML 
    private Label open2Label;
    @FXML 
    private Label closed3Label;
    @FXML 
    private Label open3Label;
    
    // Parameters for valve1
    @FXML
    private Label valve1Label;
    @FXML
    private TextField numberDropsValve1;
    @FXML
    private TextField closed11;    
    @FXML
    private TextField open11;    
    @FXML
    private TextField closed12;    
    @FXML
    private TextField open12;   
    @FXML
    private TextField closed13;    
    @FXML
    private TextField open13;
    @FXML 
    private ChoiceBox<String> TimeScale4;
    
    //Parameters for valve2
    @FXML
    private Label valve2Label;
    @FXML
    private TextField numberDropsValve2;    
    @FXML
    private TextField closed21;
    @FXML
    private TextField open21;
    @FXML
    private TextField closed22;    
    @FXML
    private TextField open22;    
    @FXML
    private TextField closed23;   
    @FXML
    private TextField open23;
    @FXML 
    private ChoiceBox<String> TimeScale5;

    // Parameters for valve3
    @FXML
    private Label valve3Label;
    @FXML
    private TextField numberDropsValve3;    
    @FXML
    private TextField closed31;
    @FXML
    private TextField open31;
    @FXML
    private TextField closed32;    
    @FXML
    private TextField open32;    
    @FXML
    private TextField closed33;   
    @FXML
    private TextField open33;
    @FXML 
    private ChoiceBox<String> TimeScale6;

    // output port TextFields
    @FXML
    private TextField device1;
    @FXML
    private TextField device2;
    @FXML
    private TextField device3;
    @FXML
    private TextField device4;
    @FXML
    private TextField device5;
    @FXML
    private TextField device6;
    @FXML
    private TextField device7;
    @FXML
    private TextField device8;
 
	// 'Enable output' Checkbox and indicator circle
    @FXML
    private CheckBox EnableOutput;
    @FXML
    private Circle OutputsActive;
    
    // timing diagram 
    @FXML
    private LineChart<Integer, Integer> timingDiagram;
    @FXML
    private ToggleGroup  toggleGroup1;
    @FXML
    private ToggleGroup  toggleGroup2;
    @FXML
    private Toggle radioBulbMode_1;
    @FXML
    private Toggle radioBulbMode_2;
    @FXML
    private Toggle radioCameraTrigger_1;
    @FXML
    private Toggle radioCameraTrigger_2;
    @FXML
    private Label ShutterLagLabel;
    @FXML
    private Label TimeToFlashLabel;
    @FXML
    private Button purgeBtn1;
    @FXML
    private Button purgeBtn2;
    @FXML
    private Button purgeBtn3;
    @FXML
    private TextArea projectComment;

    private int nrAddedValveSeries=0;		// number of added ValveSeries already added (must be <=3)
    private int nrShutterSeries=0;			// number of added ShutterSeries already added (must be <=1)
    private int nrFlashSeries=0;  			// number of added FlashSeries already added (must be <=1)
    
    // Diagram line series
    XYChart.Series <Integer, Integer> ShutterSeries = new XYChart.Series<Integer, Integer>();
    XYChart.Series <Integer, Integer> FlashSeries   = new XYChart.Series<Integer, Integer>();
    XYChart.Series <Integer, Integer> ValveSeries1  = new XYChart.Series<Integer, Integer>();
    XYChart.Series <Integer, Integer> ValveSeries2  = new XYChart.Series<Integer, Integer>();
    XYChart.Series <Integer, Integer> ValveSeries3  = new XYChart.Series<Integer, Integer>();
    
    // constructor:  
    // called before initialize()
    public TriggerControlPanelController() {
    }

    // initialize 
    // called after the fxml file has been loaded. 
	public void initialize() {
		setupPorts();
		setupValveVisibility();
		setupValveTextFields();
		setupTimeScale();
		setupSpinners();
		setupRadioButtons();
		setupTimingDiagram();
		registerChangelisteners();
		setupProjectComment();
		setupTPvariables();
		
        // prevent GPIO output if not running on the Raspberry Pi
		if (!(System.getProperty("os.name").equals("Linux"))) isLinux=false;
	}
	
	// setupTPvariables
	// initialize the array MainApp.TPvariables with the default project parameters
	private void setupTPvariables(){
		
		MainApp.TPVariables[0][0] = "Nr. of entries :";	MainApp.TPVariables[0][1] = "37";
	
		MainApp.TPVariables[1][0] = "ProjectName";		MainApp.TPVariables[1][1]  = "defaultProject";
		MainApp.TPVariables[2][0] = "ProjectPath";		MainApp.TPVariables[2][1]  =  MainApp.TProject.getProjectPath();
		MainApp.TPVariables[3][0] = "ShutterLag";		MainApp.TPVariables[3][1]  = MainApp.TProject.getShutterLag().toString();
		MainApp.TPVariables[4][0] = "ShutterSpeed";		MainApp.TPVariables[4][1]  = MainApp.TProject.getShutterSpeed().toString();
		MainApp.TPVariables[5][0] = "BulbMode";			MainApp.TPVariables[5][1]  = MainApp.TProject.getBulbMode().toString();
		MainApp.TPVariables[6][0] = "CameraTrigger";	MainApp.TPVariables[6][1]  = MainApp.TProject.getCameraTrigger().toString();
		MainApp.TPVariables[7][0] = "TimeToFlash";		MainApp.TPVariables[7][1]  = MainApp.TProject.getTimeToFlash().toString();
		MainApp.TPVariables[8][0] = "NrFlashes";		MainApp.TPVariables[8][1]  = MainApp.TProject.getNrFlashes().toString();
		MainApp.TPVariables[9][0] = "NrValves";			MainApp.TPVariables[9][1]  = MainApp.TProject.getNrValves().toString();
		MainApp.TPVariables[10][0] = "TimeScale1";		MainApp.TPVariables[10][1] = MainApp.TProject.getTimeScale1Str ();
		
		MainApp.TPVariables[11][0] = "TimeScale2";		MainApp.TPVariables[11][1] = MainApp.TProject.getTimeScale2Str ();
		MainApp.TPVariables[12][0] = "TimeScale3";		MainApp.TPVariables[12][1] = MainApp.TProject.getTimeScale3Str ();;
		MainApp.TPVariables[13][0] = "NrDrops1";		MainApp.TPVariables[13][1] = MainApp.TProject.getNrDrops1().toString();
		MainApp.TPVariables[14][0] = "Closed11";		MainApp.TPVariables[14][1] = MainApp.TProject.getClosed11().toString();
		MainApp.TPVariables[15][0] = "Open11";			MainApp.TPVariables[15][1] = MainApp.TProject.getOpen11().toString();
		MainApp.TPVariables[16][0] = "Closed12";		MainApp.TPVariables[16][1] = MainApp.TProject.getClosed12().toString();
		MainApp.TPVariables[17][0] = "Open12";			MainApp.TPVariables[17][1] = MainApp.TProject.getOpen12().toString();
		MainApp.TPVariables[18][0] = "Closed13";		MainApp.TPVariables[18][1] = MainApp.TProject.getClosed13().toString();
		MainApp.TPVariables[19][0] = "Open13";			MainApp.TPVariables[19][1] = MainApp.TProject.getOpen13().toString();
		MainApp.TPVariables[20][0] = "TimeScale4";		MainApp.TPVariables[20][1] = MainApp.TProject.getTimeScale4Str ();

		MainApp.TPVariables[21][0] = "NrDrops2";		MainApp.TPVariables[21][1] = MainApp.TProject.getNrDrops2().toString();
		MainApp.TPVariables[22][0] = "Closed21";		MainApp.TPVariables[22][1] = MainApp.TProject.getClosed21().toString();
		MainApp.TPVariables[23][0] = "Open21";			MainApp.TPVariables[23][1] = MainApp.TProject.getOpen21().toString();
		MainApp.TPVariables[24][0] = "Closed22";		MainApp.TPVariables[24][1] = MainApp.TProject.getClosed22().toString();
		MainApp.TPVariables[25][0] = "Open22";			MainApp.TPVariables[25][1] = MainApp.TProject.getOpen22().toString();
		MainApp.TPVariables[26][0] = "Closed23";		MainApp.TPVariables[26][1] = MainApp.TProject.getClosed23().toString();
		MainApp.TPVariables[27][0] = "Open23";			MainApp.TPVariables[27][1] = MainApp.TProject.getOpen23().toString();
		MainApp.TPVariables[28][0] = "TimeScale5";		MainApp.TPVariables[28][1] = MainApp.TProject.getTimeScale5Str ();
		
		MainApp.TPVariables[29][0] = "NrDrops3";		MainApp.TPVariables[29][1] = MainApp.TProject.getNrDrops3().toString();
		MainApp.TPVariables[30][0] = "Closed31";		MainApp.TPVariables[30][1] = MainApp.TProject.getClosed31().toString();
		MainApp.TPVariables[31][0] = "Open31";			MainApp.TPVariables[31][1] = MainApp.TProject.getOpen31().toString();
		MainApp.TPVariables[32][0] = "Closed32";		MainApp.TPVariables[32][1] = MainApp.TProject.getClosed32().toString();
		MainApp.TPVariables[33][0] = "Open32";			MainApp.TPVariables[33][1] = MainApp.TProject.getOpen32().toString();
		MainApp.TPVariables[34][0] = "Closed33";		MainApp.TPVariables[34][1] = MainApp.TProject.getClosed33().toString();		
		MainApp.TPVariables[35][0] = "Open33";			MainApp.TPVariables[35][1] = MainApp.TProject.getOpen33().toString();
		MainApp.TPVariables[36][0] = "TimeScale6";		MainApp.TPVariables[36][1] = MainApp.TProject.getTimeScale6Str ();
		
		MainApp.TPVariables[37][0] = "ProjectCommentText";	MainApp.TPVariables[37][1] = MainApp.TProject.getProjectCommentText();
	}

	// setupPorts
	// allocate the output ports to the devices
	// update the displayed device allocation 
	// update the values and the visibility of the valve parameter fields
	private void setupPorts() {
		int index = 0;
		
		MainApp.TProject.setPort(index++, device.camera);
		
		// allocate the output port allocation
		switch ( MainApp.TProject.getNrFlashes()) {
	        case 0: MainApp.TProject.setPort(index++, device.none);
				 	MainApp.TProject.setPort(index++, device.none);
				 	MainApp.TProject.setPort(index++, device.none);
				 	break;
			 
	        case 1: MainApp.TProject.setPort(index++, device.flash1);
				 	MainApp.TProject.setPort(index++, device.none);
				 	MainApp.TProject.setPort(index++, device.none);
			 break;
	
	        case 2:  MainApp.TProject.setPort(index++, device.flash1);
	        		 MainApp.TProject.setPort(index++, device.flash2);
	        		 MainApp.TProject.setPort(index++, device.none);

	        		 break;
	        		 
	        case 3:  MainApp.TProject.setPort(index++, device.flash1);
			 	 	 MainApp.TProject.setPort(index++, device.flash2);
			 	  	 MainApp.TProject.setPort(index++, device.flash3);
			 		 break;
		}
		index=4;
		switch ( MainApp.TProject.getNrValves()) {
           case 0:   MainApp.TProject.setPort(index++, device.none);
			 	 	 MainApp.TProject.setPort(index++, device.none);
			 	 	 MainApp.TProject.setPort(index++, device.none);
			 	 	 break;
		
		    case 1:  MainApp.TProject.setPort(index++, device.valve1);
			 		 MainApp.TProject.setPort(index++, device.none);
			 		 MainApp.TProject.setPort(index++, device.none);
		             break;

	        case 2:  MainApp.TProject.setPort(index++, device.valve1);
	        		 MainApp.TProject.setPort(index++, device.valve2);
			 	  	 MainApp.TProject.setPort(index++, device.none);
	        		 break;
	        		 
	        case 3:  MainApp.TProject.setPort(index++, device.valve1);
			 	 	 MainApp.TProject.setPort(index++, device.valve2);
			 	  	 MainApp.TProject.setPort(index++, device.valve3);
			 		 break;
		}
		// the rest of the ports is not used
		for (int i = index; i<8; i++) { 
			MainApp.TProject.setPort(i, device.none); 
		}
		
		// clear TextFields for all devices
		device1.setText("");
		device2.setText("");
		device3.setText("");
		device4.setText("");
		device5.setText("");
		device6.setText("");
		device7.setText("");
		device8.setText("");
		
		// set TextFields of the used devices
		if (MainApp.TProject.getPort(0)!=device.none) { device1.setText(MainApp.TProject.getPort(0).toString()); 
														device1.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(0) + ";");
													  }
		if (MainApp.TProject.getPort(1)!=device.none) { device2.setText(MainApp.TProject.getPort(1).toString());
														device2.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(1) + ";"); 
													  }
		if (MainApp.TProject.getPort(2)!=device.none) { device3.setText(MainApp.TProject.getPort(2).toString());
														device3.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(2) + ";"); 
													  }
		if (MainApp.TProject.getPort(3)!=device.none) { device4.setText(MainApp.TProject.getPort(3).toString());
														device4.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(3) + ";"); 
													  }
		if (MainApp.TProject.getPort(4)!=device.none) { device5.setText(MainApp.TProject.getPort(4).toString());
														device5.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(4) + ";"); 
													  }
		if (MainApp.TProject.getPort(5)!=device.none) { device6.setText(MainApp.TProject.getPort(5).toString());
														device6.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(5) + ";"); 
													  }
		if (MainApp.TProject.getPort(6)!=device.none) { device7.setText(MainApp.TProject.getPort(6).toString());
														device7.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(6) + ";"); 
													  }
		if (MainApp.TProject.getPort(7)!=device.none) { device8.setText(MainApp.TProject.getPort(7).toString());
														device8.setStyle("-fx-text-fill: " + MainApp.TProject.getPortcolors(7) + ";"); 
													  }
	}

	// setupValveVisibility
	// set the visibility of all Fields in the valve settings area
	private void setupValveVisibility() {
			
		// reset the visibility of the valve parameter fields
		numberDropsLabel.setVisible(false);;
		closed1Label.setVisible(false);
		open1Label.setVisible(false);
		closed2Label.setVisible(false);
		open2Label.setVisible(false);
		closed3Label.setVisible(false);
		open3Label.setVisible(false);
		
		valve1Label.setVisible(false);  
		numberDropsValve1.setVisible(false);  
		closed11.setVisible(false);  
		open11.setVisible(false); 	   
		closed12.setVisible(false);  
		open12.setVisible(false); 
		closed13.setVisible(false);  
		open13.setVisible(false);
		TimeScale4.setVisible(false);
		
		valve2Label.setVisible(false);  
		numberDropsValve2.setVisible(false);  
		closed21.setVisible(false);  
		open21.setVisible(false); 	   
		closed22.setVisible(false);  
		open22.setVisible(false); 
		closed23.setVisible(false);  
		open23.setVisible(false);
		TimeScale5.setVisible(false);
		
		valve3Label.setVisible(false);  
		numberDropsValve3.setVisible(false);  
		closed31.setVisible(false);  
		open31.setVisible(false); 	   
		closed32.setVisible(false);  
		open32.setVisible(false); 
		closed33.setVisible(false);  
		open33.setVisible(false);
		TimeScale6.setVisible(false);
		
		// set visibility of the valve parameter fields for NrValves >0
		if (MainApp.TProject.getNrValves()>0) {
			
			// show the column labels 
			numberDropsLabel.setVisible(true);
			closed1Label.setVisible(true);
			open1Label.setVisible(true);
			closed2Label.setVisible(true);
			open2Label.setVisible(true);
			closed3Label.setVisible(true);
			open3Label.setVisible(true);
			
			// show valve1 parameter fields
		    valve1Label.setVisible(true);  
		    numberDropsValve1.setVisible(true);  
		    closed11.setVisible(true);  
		    open11.setVisible(true); 	   
		    closed12.setVisible(true);  
		    open12.setVisible(true); 
		    closed13.setVisible(true);  
		    open13.setVisible(true);
		    TimeScale4.setVisible(true);
		}
		// if NrValves > 2 or 3: show valve2 parameter fields
		if (MainApp.TProject.getNrValves()>1) {
		    valve2Label.setVisible(true);  
		    numberDropsValve2.setVisible(true);  
		    closed21.setVisible(true);  
		    open21.setVisible(true); 	   
		    closed22.setVisible(true);  
		    open22.setVisible(true); 
		    closed23.setVisible(true);  
		    open23.setVisible(true);
		    TimeScale5.setVisible(true);
		}
		// if NrValves = 3: show valve3 parameter fields
		if (MainApp.TProject.getNrValves()>2){
		    valve3Label.setVisible(true);  
		    numberDropsValve3.setVisible(true);  
		    closed31.setVisible(true);  
		    open31.setVisible(true); 	   
		    closed32.setVisible(true);  
		    open32.setVisible(true); 
		    closed33.setVisible(true);  
		    open33.setVisible(true);
		    TimeScale6.setVisible(true);
		}
	}

	// setupValveTextFields
	// synchronize the valve GUI fields with the model
	private void setupValveTextFields() {
		
		if (MainApp.TProject.getBulbMode() == ctrl.on) { ShutterSpeedSpinner.setOpacity(1.0); } 
		else { ShutterSpeedSpinner.setOpacity(0.3); }

		numberDropsValve1.textProperty().setValue(MainApp.TProject.getNrDrops1().toString());
		closed11.textProperty().setValue(MainApp.TProject.getClosed11().toString());
		open11.textProperty().setValue(MainApp.TProject.getOpen11().toString());
		closed12.textProperty().setValue(MainApp.TProject.getClosed12().toString());
		open12.textProperty().setValue(MainApp.TProject.getOpen12().toString());
		closed13.textProperty().setValue(MainApp.TProject.getClosed13().toString());
		open13.textProperty().setValue(MainApp.TProject.getOpen13().toString());
	
		numberDropsValve2.textProperty().setValue(MainApp.TProject.getNrDrops2().toString());
		closed21.textProperty().setValue(MainApp.TProject.getClosed21().toString());
		open21.textProperty().setValue(MainApp.TProject.getOpen21().toString());
		closed22.textProperty().setValue(MainApp.TProject.getClosed22().toString());
		open22.textProperty().setValue(MainApp.TProject.getOpen22().toString());
		closed23.textProperty().setValue(MainApp.TProject.getClosed23().toString());
		open23.textProperty().setValue(MainApp.TProject.getOpen23().toString());

		numberDropsValve3.textProperty().setValue(MainApp.TProject.getNrDrops3().toString());
		closed31.textProperty().setValue(MainApp.TProject.getClosed31().toString());
		open31.textProperty().setValue(MainApp.TProject.getOpen31().toString());
		closed32.textProperty().setValue(MainApp.TProject.getClosed32().toString());
		open32.textProperty().setValue(MainApp.TProject.getOpen32().toString());
		closed33.textProperty().setValue(MainApp.TProject.getClosed33().toString());
		open33.textProperty().setValue(MainApp.TProject.getOpen33().toString());
		
	}
		
	// setupTimeScale
	// synchronize the valve GUI fields with the model
	private void setupTimeScale() {
    	// set all timescale variables of the user interface to the selected value
    	TimeScale1.getSelectionModel().select(MainApp.TProject.getTimeScale1());
    	TimeScale2.getSelectionModel().select(MainApp.TProject.getTimeScale2());
    	TimeScale3.getSelectionModel().select(MainApp.TProject.getTimeScale3());
    	TimeScale4.getSelectionModel().select(MainApp.TProject.getTimeScale4());
    	TimeScale5.getSelectionModel().select(MainApp.TProject.getTimeScale5());
    	TimeScale6.getSelectionModel().select(MainApp.TProject.getTimeScale6());	
	}

	// setupSpinners
	// set the Spinner values according to the values in TProject
	void setupSpinners() {
		ShutterLagSpinner.getValueFactory().setValue(MainApp.TProject.getShutterLag());
		ShutterSpeedSpinner.getValueFactory().setValue(MainApp.TProject.getShutterSpeed());
		TimeToFlashSpinner.getValueFactory().setValue(MainApp.TProject.getTimeToFlash());
		FlashNumberSpinner.getValueFactory().setValue(MainApp.TProject.getNrFlashes());
		ValveNumberSpinner.getValueFactory().setValue(MainApp.TProject.getNrValves());
	}
	
	// SetupRadioButtons
	// set the RadioButtons RadioBulbMode_x and radioCameraTrigger_x according to the values in TProject
	// if BulbMode=false, the ShutterSpeed field is made noneditable 
	void setupRadioButtons() {
		//setup the radio buttons 'radioBulbMode_x
		if (MainApp.TProject.getBulbMode() == ctrl.on) {
			toggleGroup2.selectToggle(radioBulbMode_1);
			ShutterSpeedSpinner.setOpacity(1.0);
			ShutterSpeedSpinner.setEditable(true);
		} else {
			toggleGroup2.selectToggle(radioBulbMode_2);
			ShutterSpeedSpinner.setOpacity(0.3);
			ShutterSpeedSpinner.setEditable(false);
		}
		// setup the radio buttons 'radioCameraTrigger_x'
		if (MainApp.TProject.getCameraTrigger() == trigger.start) {
			radioCameraTrigger_1.setSelected(true);
		} else {
			radioCameraTrigger_2.setSelected(true);
		}
	}
		
	// SetupProjectComment
	// put the comment text into the TextArea "projectComment" of the 	utilities Tab
	void setupProjectComment() {
		projectComment.setText(MainApp.TProject.getProjectCommentText().replace("__",  "\n"));	
	}
	
	// setupTimingDiagram()
	// setup the timing diagram according to the parameters
	// given by the current project
	private void setupTimingDiagram() {
		
		if(MainApp.TProject.getTimeToFlash() < MainApp.TProject.getShutterLag()) return;
		
        // define the layout of the timing diagram
        timingDiagram.autosize();
        timingDiagram.setAnimated(false);
        timingDiagram.setCreateSymbols(false);
        timingDiagram.setLegendVisible(false);
        timingDiagram.setHorizontalGridLinesVisible(true);
        timingDiagram.setMaxHeight(400.0);
        
        // get the parameters of the shutter timing from the model
        Integer shutterLag    = MainApp.TProject.getShutterLag();
        Integer shutterSpeed  = MainApp.TProject.getShutterSpeed();
        Integer TimeToFlash   = MainApp.TProject.getTimeToFlash();
        
        // parameters of the valve1 timing
        Integer lck11   = MainApp.TProject.getClosed11();
        Integer opn11   = MainApp.TProject.getOpen11();
        Integer lck12   = MainApp.TProject.getClosed12();
        Integer opn12   = MainApp.TProject.getOpen12();
        Integer lck13   = MainApp.TProject.getClosed13();
        Integer opn13   = MainApp.TProject.getOpen13(); 
        
        // parameters of the valve2 timing
        Integer lck21   = MainApp.TProject.getClosed21();
        Integer opn21   = MainApp.TProject.getOpen21();
        Integer lck22   = MainApp.TProject.getClosed22();
        Integer opn22   = MainApp.TProject.getOpen22();
        Integer lck23   = MainApp.TProject.getClosed23();
        Integer opn23   = MainApp.TProject.getOpen23(); 
      
        // parameters of the valve3 timing
        Integer lck31   = MainApp.TProject.getClosed31();
        Integer opn31   = MainApp.TProject.getOpen31();
        Integer lck32   = MainApp.TProject.getClosed32();
        Integer opn32   = MainApp.TProject.getOpen32();
        Integer lck33   = MainApp.TProject.getClosed33();
        Integer opn33   = MainApp.TProject.getOpen33(); 
        
        // x-length of the diagram lines
        int Drops1 = MainApp.TProject.getNrDrops1();
        int Drops2 = MainApp.TProject.getNrDrops2();
        int Drops3 = MainApp.TProject.getNrDrops3();
        
        // compute length of the diagram x-axis
        int l1=0, l2=0, l3=0, l4=0;      
        l1 = TimeToFlash - shutterLag + shutterSpeed;	
        switch (Drops1) {
	    	case  0	:	l2 = 0;
	    	case  1	:	l2 = lck11 + opn11; break;
	    	case  2	:	l2 = lck11 + opn11 + lck12 + opn12; break;
	    	default	:	l2 = lck11 + opn11 + lck12 + opn12 + (Drops1-2)*(lck13 + opn13); break;
        }
        switch (Drops2) {
	    	case  0	:	l3 = 0;
	    	case  1	:	l3 = lck21 + opn21; break;
	    	case  2	:	l3 = lck21 + opn21 + lck22 + opn22; break;
	    	default	:	l3 = lck21 + opn21 + lck22 + opn22 + (Drops2-2)*(lck23 + opn23); break;
        }
        switch (Drops3) {
	    	case  0	:	l4 = 0;
	    	case  1	:	l4 = lck31 + opn31; break;
	    	case  2	:	l4 = lck31 + opn31 + lck32 + opn32; break;
	    	default	:	l4 = lck31 + opn31 + lck32 + opn32 + (Drops3-2)*(lck33 + opn33); break;
        }
        int maxlength = Math.max( Math.max( Math.max(l1, l2),l3),l4);
        maxlength += maxlength/10;

        // delete all data series from the diagram
        timingDiagram.getData().clear();
        nrAddedValveSeries=0;
        nrShutterSeries=0;
        nrFlashSeries=0;
        
        // points of the shutter curve
        ShutterSeries.getData().clear();
        ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(0, 0));
        ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash - shutterLag,                      0));
        ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash - shutterLag,                      8));
        ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash - shutterLag + shutterSpeed,       8));
        
        if (toggleGroup2.getSelectedToggle() != null) {
            if (MainApp.TProject.getBulbMode()==ctrl.on)  {
                ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash - shutterLag + shutterSpeed,       0));
                ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(maxlength , 0));
            }
            else {
            	ShutterSeries.getData().add(new XYChart.Data<Integer, Integer>(maxlength , 8));
            }      
        };
       
        // points of the flash curve
        FlashSeries.getData().clear();
        FlashSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash,      0));
        FlashSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash,     38));
        FlashSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash + 1, 38));
        FlashSeries.getData().add(new XYChart.Data<Integer, Integer>(TimeToFlash + 1,  0));  
        
        // add ShutterSeries and FlashSeries (if not yet added)
        if (nrShutterSeries==0)  { timingDiagram.getData().add(ShutterSeries); nrShutterSeries=1; }
        if (nrFlashSeries==0)    { timingDiagram.getData().add(FlashSeries);   nrFlashSeries=1; }
        
        // points of the valve1 curve
        ValveSeries1.getData().clear();
        if (Drops1>=1)
        {
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(0,                     			10));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11,             				10));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11,             				18));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11,   					18));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11,     				10));
        }
        if (Drops1>=2) {
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11 + lck12,  			10));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11 + lck12,  			18));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11 + lck12 + opn12,  	18));
	        ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(lck11 + opn11 + lck12 + opn12,  	10));
        }
        Integer delta = lck11 + opn11 + lck12 + opn12 ;
        for (int i=1; i<= Drops1 - 2; i++) {
        	delta = delta+lck13;
        	ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(delta, 							10));
        	ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(delta, 							18));
        	ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(delta + opn13, 					18));
        	ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(delta + opn13, 					10));
        	delta = delta + opn13;
        }
        if (Drops1!=0) ValveSeries1.getData().add(new XYChart.Data<Integer, Integer>(maxlength,    			10));

        // points of the valve2 curve
        ValveSeries2.getData().clear();
        if (Drops2>=1)
        {
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(0,                     			20));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21,             				20));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21,             				28));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21,   					28));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21,     				20));
        }
        if (Drops2>=2) {
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21 + lck22,  			20));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21 + lck22,  			28));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21 + lck22 + opn22,  	28));
	        ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(lck21 + opn21 + lck22 + opn22,  	20));
        }
        delta = lck21 + opn21 + lck22 + opn22 ;
        for (int i=1; i<= Drops2 - 2; i++) {
        	delta = delta+lck23;
        	ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(delta, 							20));
        	ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(delta, 							28));
        	ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(delta + opn23, 					28));
        	ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(delta + opn23, 					20));
        	delta = delta+opn23;
        }
        if (Drops2!=0) ValveSeries2.getData().add(new XYChart.Data<Integer, Integer>(maxlength,    			20));
        
        // points of the valve3 curve
        ValveSeries3.getData().clear();
        if (Drops3>=1)
        {
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(0,                     			30));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31,             				30));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31,             				38));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31,   					38));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31,     				30));
        }
        if (Drops3>=2) {
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31 + lck32,  			30));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31 + lck32,  			38));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31 + lck32 + opn32,  	38));
	        ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(lck31 + opn31 + lck32 + opn32,  	30));
        }
        delta = lck31 + opn31 + lck32 + opn32 ;
        for (int i=1; i<= Drops3 - 2; i++) {
        	delta = delta+lck33;
        	ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(delta, 							30));
        	ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(delta, 							38));
        	ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(delta + opn33, 					38));
        	ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(delta + opn33, 					30));
        	delta = delta + opn33;
        }
        if (Drops3!=0) ValveSeries3.getData().add(new XYChart.Data<Integer, Integer>(maxlength,    			30));

        // add the correct number of ValveSeries to the timing diagram (if not yet added)
        if ((MainApp.TProject.getNrValves() > 0) & (nrAddedValveSeries==0))   { timingDiagram.getData().add(ValveSeries1); nrAddedValveSeries ++; }
        if ((MainApp.TProject.getNrValves() > 1) & (nrAddedValveSeries==1))   { timingDiagram.getData().add(ValveSeries2); nrAddedValveSeries ++; }
        if ((MainApp.TProject.getNrValves() > 2) & (nrAddedValveSeries==2))   { timingDiagram.getData().add(ValveSeries3); nrAddedValveSeries ++; }
                
        // set the lineStyles/stroke-colors of the timingDiagram lines
        //for the camera line
        Node line = ShutterSeries.getNode().lookup(".default-color0.chart-series-line");
        Color color = Color.BLUE;
        String rgb = String.format("%d, %d, %d",
        	    (int) (color.getRed() * 255),
        	    (int) (color.getGreen() * 255),
        	    (int) (color.getBlue() * 255));
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

        // for the flash line
        Node linef = FlashSeries.getNode().lookup(".default-color1.chart-series-line");
        color = Color.GOLD;
        rgb = String.format("%d, %d, %d",
        	    (int) (color.getRed() * 255),
        	    (int) (color.getGreen() * 255),
        	    (int) (color.getBlue() * 0));
        linef.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
        
        // for ValveSeries1
        if (MainApp.TProject.getNrValves() >= 1){
        
	        Node linev1 = ValveSeries1.getNode().lookup(".default-color2.chart-series-line");
	        color = Color.GREEN;
	        rgb = String.format("%d, %d, %d",
	        	    (int) (color.getRed() * 255),
	        	    (int) (color.getGreen() * 255),
	        	    (int) (color.getBlue() * 0));
	        linev1.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
        }   
        // for ValveSeries2
        if (MainApp.TProject.getNrValves() >= 2){
	        Node linev2 = ValveSeries2.getNode().lookup(".default-color3.chart-series-line ");
	        color = Color.RED;
	        rgb = String.format("%d, %d, %d",
	        	    (int) (color.getRed() * 255),
	        	    (int) (color.getGreen() * 255),
	        	    (int) (color.getBlue() * 0));
	        linev2.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
        }
        // for ValveSeries3
        if (MainApp.TProject.getNrValves() == 3) {
	        Node linev3 = ValveSeries3.getNode().lookup(".default-color4.chart-series-line");
	        color = Color.BROWN;
	        rgb = String.format("%d, %d, %d",
	        	    (int) (color.getRed() * 255),
	        	    (int) (color.getGreen() * 255),
	        	    (int) (color.getBlue() * 0));
	        linev3.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
        }
	}
	
	// registerChangeListeners
	// interactive modification of TextFields are detected 
	// the new values are forced to be Integer values.
	// if not, the old value is restored
	private void registerChangelisteners() {
		
		// register change listeners for camera and flash parameters
		// the change listeners enforce that the input consist of digits only
		// and update the corresponding Integer value of the Model 'TProject'
		// ------------------------------------------------------------------

		// handle Editor input for Shutter Lag
		ShutterLagSpinner.getEditor().textProperty().addListener(
			(observable, oldvalue, newvalue) -> {
			
			    SpinnerValueFactory<Integer> valueFactory = ShutterLagSpinner.getValueFactory();
			    
			    // handle non numeric input characters
	    		for (int i=newvalue.length()-1; i>=0; i--) 
		    		if (!(utilities.isDigit(newvalue.charAt(i)))) { 
		    			valueFactory.setValue(0); 
		    			ShutterLagSpinner.getEditor().setText("0"); 
				        MainApp.TProject.setShutterLag(0);
				        setupTimingDiagram();
				        return;
		    		};
		    	// handle numeric editor input
				if (valueFactory != null) {
					javafx.util.StringConverter<Integer> converter = valueFactory.getConverter();
				    if (converter != null) {
				        try {
				            Integer value = converter.fromString(newvalue);
				            if (value != null)
				                valueFactory.setValue(value);
				            else
				                valueFactory.setValue(0);
				        } catch (NumberFormatException ex) {
				        	ShutterLagSpinner.getEditor().setText(converter.toString(valueFactory.getValue()));
				        }
				        if (!("".contentEquals(ShutterLagSpinner.getEditor().getText()))) {
				        	MainApp.TProject.setShutterLag(Integer.parseInt(ShutterLagSpinner.getEditor().getText()));
				        	setupTimingDiagram();
				        }
				    }
				}
			});
		// handler spinner input for Shutter Lag
		ShutterLagSpinner.valueProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	 // if ShutterLag > TimeToFlash display warning, don't accept the modification
				 if (newvalue > MainApp.TProject.getTimeToFlash() ) { 
					 ShutterLagLabel.setText("ShutterLag > TimeToFlash!");
					 ShutterLagLabel.setTextFill(Color.RED);
				 }
				 // accept new ShutterLag
				 else {
					 ShutterLagLabel.setText("ShutterLag");
					 ShutterLagLabel.setTextFill(Color.BLACK);
					 MainApp.TProject.setShutterLag(newvalue);
					 setupTimingDiagram();
				 }
		    } );	
		// handle Editor input for Shutter Speed
		ShutterSpeedSpinner.getEditor().textProperty().addListener(
			(observable, oldvalue, newvalue) -> {
		
			    SpinnerValueFactory<Integer> valueFactory = ShutterSpeedSpinner.getValueFactory();
			    
			    // handle nonnumeric input characters
	    		for (int i=newvalue.length()-1; i>=0; i--) 
		    		if (!(utilities.isDigit(newvalue.charAt(i)))) { 
		    			valueFactory.setValue(0); 
		    			ShutterSpeedSpinner.getEditor().setText("0");
		    			MainApp.TProject.setShutterSpeed(0);
		    			setupTimingDiagram();
		    			return;	
		    		};		  
		    	// handle numeric editor input	
				if (valueFactory != null) {
					javafx.util.StringConverter<Integer> converter = valueFactory.getConverter();
				    if (converter != null) {
				        try {
				            Integer value = converter.fromString(newvalue);
				            if (value != null)
				                valueFactory.setValue(value);
				            else
				                valueFactory.setValue(0);
				        } catch (NumberFormatException ex) {
				        	ShutterSpeedSpinner.getEditor().setText(converter.toString(valueFactory.getValue()));
				        }
				        if (!("".contentEquals(ShutterSpeedSpinner.getEditor().getText()))) {
				        	MainApp.TProject.setShutterSpeed(Integer.parseInt(ShutterSpeedSpinner.getEditor().getText()));
				        	setupTimingDiagram();
				        }
				        
				    }
				}
			});
		
		// handler spinner input for Shutter Speed
		ShutterSpeedSpinner.valueProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
				 MainApp.TProject.setShutterSpeed(newvalue);
				 setupTimingDiagram();
	    } );	
		
		// handle numeric editor input for TimeToFlash
		TimeToFlashSpinner.getEditor().textProperty().addListener(
			(observable, oldvalue, newvalue) -> {

				SpinnerValueFactory<Integer> valueFactory = TimeToFlashSpinner.getValueFactory();
				
				// handle nonnumeric input characters	
	    		for (int i=newvalue.length()-1; i>=0; i--) 
		    		if (!(utilities.isDigit(newvalue.charAt(i)))) { 
		    			valueFactory.setValue(0); 
		    			TimeToFlashSpinner.getEditor().setText("0");
		    			MainApp.TProject.setTimeToFlash(0);
		    			setupTimingDiagram();
		    			return;	
		    		};
		    	// handle numeric editor input
				if (valueFactory != null) {
					javafx.util.StringConverter<Integer> converter = valueFactory.getConverter();
				    if (converter != null) {
				        try {
				            Integer value = converter.fromString(newvalue);
				            if (value != null)
				                valueFactory.setValue(value);
				            else
				                valueFactory.setValue(0);
				        } catch (NumberFormatException ex) {
				        	TimeToFlashSpinner.getEditor().setText(converter.toString(valueFactory.getValue()));
				        }
				        if (!("".contentEquals(TimeToFlashSpinner.getEditor().getText()))) {
				        	MainApp.TProject.setTimeToFlash(Integer.parseInt(TimeToFlashSpinner.getEditor().getText()));
				        	setupTimingDiagram();
				        }
				    }
				}
			});
		
		TimeToFlashSpinner.valueProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
				 if (("".contentEquals(TimeToFlashSpinner.getEditor().textProperty().getValue() ) ) ) {
					 System.out.println("TimeToFlash is empty"); 
					 TimeToFlashSpinner.getEditor().textProperty().setValue(oldvalue.toString());
					 setupTimingDiagram();
				 }
				 else {
					 if (newvalue < MainApp.TProject.getShutterLag() ) { 
						 TimeToFlashLabel.setText("Set TimeToFlash > ShutterLag!");
						 TimeToFlashLabel.setTextFill(Color.RED);
					 }
					 else {
						 TimeToFlashLabel.setText("TimeToFlash");
						 TimeToFlashLabel.setTextFill(Color.BLACK);
						 MainApp.TProject.setTimeToFlash(newvalue);
						 setupTimingDiagram();
					 } 
				 }
		    } );
		
		FlashNumberSpinner.valueProperty().addListener(
			(observable, oldvalue, newvalue) -> { 
		    	MainApp.TProject.setNrFlashes(FlashNumberSpinner.getValue());
		    	setupPorts();
		    	setupTimingDiagram();
		    });
		
		ValveNumberSpinner.valueProperty().addListener(
			(observable, oldvalue, newvalue) -> { 
		    	MainApp.TProject.setNrValves(ValveNumberSpinner.getValue());
		    	setupPorts();	
		    	setupValveVisibility();
		    	setupTimingDiagram();
		    });
		
		TimeScale1.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> {  		    	
		    	synchronizeTimescale(newvalue.intValue());    	
		    } );
		
		TimeScale2.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> {  		
		    	synchronizeTimescale(newvalue.intValue());			    		
		    } );
		
		TimeScale3.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	synchronizeTimescale(newvalue.intValue());		    		
		    } );

		//  register change listeners for valve1
		// -------------------------------------
		numberDropsValve1.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { numberDropsValve1.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { numberDropsValve1.setText(oldvalue); break;	};
			    MainApp.TProject.setNrDrops1(Integer.parseInt(newvalue));
			    setupTimingDiagram();
		     } );
		
		closed11.textProperty().addListener(
			(observable, oldvalue, newvalue) -> { 
				if (newvalue.length()==0) { closed11.setText("0"); }
				else
					for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed11.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed11(Integer.parseInt(closed11.getText()));
			    setupTimingDiagram();
			   
			} );
		open11.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open11.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open11.setText(oldvalue); break;	};
			  MainApp.TProject.setOpen11(Integer.parseInt(open11.getText()));
			  setupTimingDiagram();
		    } );
		closed12.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed12.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed12.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed12(Integer.parseInt(closed12.getText()));
			    setupTimingDiagram(); 
		     } );
		open12.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open12.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open12.setText(oldvalue); break;	};
			    MainApp.TProject.setOpen12(Integer.parseInt(open12.getText()));
			    setupTimingDiagram(); 
		    } );
		closed13.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed13.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed13.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed13(Integer.parseInt(closed13.getText()));
			    setupTimingDiagram(); 
		    } );
		open13.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open13.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open13.setText(oldvalue); break;	};
			    MainApp.TProject.setOpen13(Integer.parseInt(open13.getText()));	
			    setupTimingDiagram(); 
		    } );	
		
		TimeScale4.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	synchronizeTimescale(newvalue.intValue());	    		
		    } );	
		
		//  register change listeners for valve2
		// -------------------------------------
		numberDropsValve2.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { numberDropsValve2.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { numberDropsValve2.setText(oldvalue); break;	};
			   MainApp.TProject.setNrDrops2(Integer.parseInt(newvalue)); 		
			   setupTimingDiagram();
		    } );
		closed21.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed21.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed21.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed21(Integer.parseInt(closed21.getText()));	
			    setupTimingDiagram();
		    } );
		open21.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open21.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open21.setText(oldvalue); break;	};   		
			    MainApp.TProject.setOpen21(Integer.parseInt(open21.getText()));
			    setupTimingDiagram();
		    } );
		closed22.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed22.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed22.setText(oldvalue); break;	}
		    	MainApp.TProject.setClosed22(Integer.parseInt(closed22.getText()));
		    	setupTimingDiagram();
		    } );
		open22.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open22.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open22.setText(oldvalue); break;	}
		    	MainApp.TProject.setOpen22(Integer.parseInt(open22.getText()));
		    	setupTimingDiagram();
		    } );
		closed23.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed23.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed23.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed23(Integer.parseInt(closed23.getText()));	
			    setupTimingDiagram();
		    } );
		open23.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open23.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open23.setText(oldvalue); break;	};
			   MainApp.TProject.setOpen23(Integer.parseInt(open23.getText()));	
			   setupTimingDiagram();
		    } );	
		TimeScale5.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 	    	
		    	synchronizeTimescale(newvalue.intValue());		    		
		    } );
				
		//  register change listeners for valve3
		// -------------------------------------
		numberDropsValve3.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { numberDropsValve3.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { numberDropsValve3.setText(oldvalue); break;	};
	    		MainApp.TProject.setNrDrops3(Integer.parseInt(newvalue));
	    		setupTimingDiagram();
		    } );
		closed31.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed31.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed31.setText(oldvalue); break;	};
	    		MainApp.TProject.setClosed31(Integer.parseInt(closed31.getText()));
	    		setupTimingDiagram();
		    } );
		open31.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open31.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open31.setText(oldvalue); break;	};
	    		MainApp.TProject.setOpen31(Integer.parseInt(open31.getText()));	
	    		setupTimingDiagram();
		    } );
		closed32.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed32.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed32.setText(oldvalue); break;	};
			    MainApp.TProject.setClosed32(Integer.parseInt(closed32.getText()));
			    setupTimingDiagram();
		    } );
		open32.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { open32.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open32.setText(oldvalue); break;	};
			    MainApp.TProject.setOpen32(Integer.parseInt(open32.getText()));
			    setupTimingDiagram();
		    } );
		closed33.textProperty().addListener(
		    (observable, oldvalue, newvalue) -> { 
		    	if (newvalue.length()==0) { closed33.setText("0"); }
		    	else
		    		for (int i=newvalue.length()-1; i>=0; i--) 
			    		if (!(utilities.isDigit(newvalue.charAt(i)))) { closed33.setText(oldvalue); break;	};	    		
	    		MainApp.TProject.setClosed33(Integer.parseInt(closed33.getText()));
	    		setupTimingDiagram();
		    } );
		open33.textProperty().addListener(
			    (observable, oldvalue, newvalue) -> { 
			    	if (newvalue.length()==0) { open33.setText("0"); }
			    	else
			    		for (int i=newvalue.length()-1; i>=0; i--) 
				    		if (!(utilities.isDigit(newvalue.charAt(i)))) { open33.setText(oldvalue); break;	};
				    MainApp.TProject.setOpen33(Integer.parseInt(open33.getText()));
				    setupTimingDiagram();
			    } );		
		       		
		TimeScale6.getSelectionModel().selectedIndexProperty().addListener(
		    (observable, oldvalue, newvalue) -> {     	
		    	synchronizeTimescale(newvalue.intValue());
		    } );
		
		purgeBtn1.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) 
	        	OpenValve(1);
	        else 
	        	CloseValve(1);
	    });
		
		purgeBtn2.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) 
	        	OpenValve(2);
	        else 
	        	CloseValve(2);
	    });
		
		purgeBtn3.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) 
	        	OpenValve(3);
	        else 
	        	CloseValve(3);
	    });
		
		projectComment.textProperty().addListener(
			    (observable, oldvalue, newvalue) -> { 
				    MainApp.TProject.setProjectCommentText(newvalue);
			    } );
	
	}
	
	// synchronizeTimescale
	// synchronize the timescale value for the objects of TProject and for all GUI timescale select Fields
	private  void synchronizeTimescale(int ts) {
		// set all timescale object of TProject to the new value
		MainApp.TProject.setTimeScale1(ts);
		MainApp.TProject.setTimeScale2(ts);
		MainApp.TProject.setTimeScale3(ts);
		MainApp.TProject.setTimeScale4(ts);
		MainApp.TProject.setTimeScale5(ts);
		MainApp.TProject.setTimeScale6(ts);
		
		// set all timescale values of the GUI to the selected value
		TimeScale1.getSelectionModel().select(ts);
		TimeScale2.getSelectionModel().select(ts);
		TimeScale3.getSelectionModel().select(ts);
		TimeScale4.getSelectionModel().select(ts);
		TimeScale5.getSelectionModel().select(ts);
		TimeScale6.getSelectionModel().select(ts);	 
	}
	    
    // handleRadioBulbMode
    // set the TProject variable and call setupRadioButtons () and setupTimingDiagram()
    @FXML
    private void handleRadioBulbMode() {
		if (radioBulbMode_1.selectedProperty().get()==true) 
			MainApp.TProject.setBulbMode(ctrl.on);
		else
			MainApp.TProject.setBulbMode(ctrl.off);
		setupRadioButtons();
		setupTimingDiagram();
    }
 
    // handleRadioCameraTrigger
    // set the TProject variable a call setupRadioButtons () and setupTimingDiagram()
    @FXML
    private void handleRadioCameraTrigger() {
		if (radioCameraTrigger_1.selectedProperty().get()==true)
			MainApp.TProject.setCameraTrigger(trigger.start); 
		else 
			MainApp.TProject.setCameraTrigger(trigger.sensor1);
		setupRadioButtons();
		setupTimingDiagram();
    }
        
    // handleReadProject
    // Called when the user clicks the ReadProject button.
    // Reads the current project data from a file
    @FXML
    public void handleReadProject() throws IOException {
    	ReadTriggerProject();
    	DeserializeTriggerProject();
    	
    	// Update the GUI Fields
		setupPorts();
		setupValveVisibility();
		setupValveTextFields();
		setupTimeScale();
		setupRadioButtons();
		setupSpinners();
		setupTimingDiagram();
		setupProjectComment();
    }
    
    // handleWriteProject
    // Writes the current project data to a text file
    @FXML
    private void handleWriteProject() throws FileNotFoundException, IOException {
    	SerializeTriggerProject();
    	try {
			WriteTriggerProject();
		} catch (JarException e) {
			System.out.println("File not found error while writing out the Trigger project");
			e.printStackTrace();
		}
    }
    
	// WriteTriggerProject
	// Write all TPProject variables to a text file
    // The values are taken as serialised values from 'TPVariables'
	public static void WriteTriggerProject () throws IOException, JarException {

		// create a File chooser and a dialog box for output file selection
        FileChooser filchooser = new FileChooser(); 
		File file = new File(".");
		String currentDirectory = file.getAbsolutePath();
		file = new File(currentDirectory);
		filchooser.setInitialDirectory(file);
		filchooser.setInitialFileName("*.prj");
		
		// show file selection dialog
		Window stage = new Stage();
		File outputFile = filchooser.showSaveDialog(stage ); 
		
		// write out the project file
        if (outputFile != null) {
            try {
                FileWriter filewriter = new FileWriter(outputFile);
                BufferedWriter bufferdwriter = new BufferedWriter(filewriter);
                String nrTPVariables     = MainApp.TPVariables[0][1];
                
                // number of the objects to be output
                int numLines = Integer.parseInt(nrTPVariables);

                // write File Header Line
                bufferdwriter.write("---------- RasPiTrigger Project ----------\n");
                // write all TProject objects to a file: one line for each object
                // line format:  <object name> <object value> 
            	for (int i=1; i<= numLines; i++) {
	        		bufferdwriter.write (utilities.extend(MainApp.TPVariables[i][0],25));
	        		bufferdwriter.write (MainApp.TPVariables[i][1] + "\n");
            	}
            	bufferdwriter.flush();
            	bufferdwriter.close();
            } catch (IOException ex) {
            	System.out.println("Cannot write project to file " + outputFile.toString() );
	        }finally {	
	        }
        }
	}
	
	// ReadTriggerProject
	// Read project data from a text file and put them into TPVariables[][]
	public static void ReadTriggerProject() throws IOException {

		// create a File chooser and a dialog box for output file selection
        FileChooser filchooser = new FileChooser(); 
		String currentDirectory;
		File file = new File(".");
		currentDirectory = file.getAbsolutePath();
		file = new File(currentDirectory);
		filchooser.setInitialDirectory(file);
		filchooser.setInitialFileName("*.prj");
		
		// show file selection dialog
		Window stage = new Stage();
		File inputFile = filchooser.showOpenDialog(stage ); 
		  
		// Read all lines and put them into the StringArray
		if (inputFile != null) {
		    FileReader fileReader = new FileReader(inputFile);
		    BufferedReader bufferedReader = new BufferedReader(fileReader);
		   
		    String line = bufferedReader.readLine();
			if (line.contentEquals("---------- RasPiTrigger Project ----------" + System.lineSeparator())) {
				System.out.println("Input File is not RasPiTrigger Projectfile!");
				bufferedReader.close();
				return;
			} 
			int  numLines = 0;
		    do
		    {
				line = bufferedReader.readLine();
				if (line != null) {
					numLines++;
					MainApp.TPVariables[numLines][0] = line.substring(0,20);
					MainApp.TPVariables[numLines][1] = line.substring(20);
				}
		    }
		    while (line != null) ;
		    bufferedReader.close();
		
		    MainApp.TPVariables[0][0] = "Nr. of entries : " ;
		    MainApp.TPVariables[0][1] = String.valueOf(numLines);
		}
	}
	
	// String[][] SerializeTriggerProject()
	// setup a String array with the names and values of the TProject variables
	private void SerializeTriggerProject() {
		
    	MainApp.TPVariables[0][0]   = "number of entries";
    	MainApp.TPVariables[0][1]   = "37";
		
		MainApp.TPVariables[1][0]   = "ProjectName";
		MainApp.TPVariables[1][1]   = MainApp.TProject.getProjectName();

		MainApp.TPVariables[2][0]   = "ProjectPath";
		MainApp.TPVariables[2][1]   = MainApp.TProject.getProjectPath();
		
		MainApp.TPVariables[3][0]   = "ShutterLag";
		MainApp.TPVariables[3][1]   = MainApp.TProject.getShutterLag().toString();
		
		MainApp.TPVariables[4][0]   = "ShutterSpeed";
		MainApp.TPVariables[4][1]   = MainApp.TProject.getShutterSpeed().toString();

		MainApp.TPVariables[5][0]  = "BulbMode";
		MainApp.TPVariables[5][1]  = MainApp.TProject.getBulbMode().name();
		
		MainApp.TPVariables[6][0]  = "CameraTrigger";
		MainApp.TPVariables[6][1]  = MainApp.TProject.getCameraTrigger().name();

		MainApp.TPVariables[7][0]  = "TimeToFlash";
		MainApp.TPVariables[7][1]  = MainApp.TProject.getTimeToFlash().toString();
		
		MainApp.TPVariables[8][0]  = "NrFlashes";
		MainApp.TPVariables[8][1]  = MainApp.TProject.getNrFlashes().toString();
		
		MainApp.TPVariables[9][0]  = "NrValves";
		MainApp.TPVariables[9][1]  = MainApp.TProject.getNrValves().toString();

		MainApp.TPVariables[10][0]  = "TimeScale1";
		switch(MainApp.TProject.getTimeScale1()){
	        case 0: MainApp.TPVariables[10][1]  = "us"; break;
	        case 1: MainApp.TPVariables[10][1]  = "ms"; break;
	        case 2: MainApp.TPVariables[10][1]  = "s"; break;
	        case 3: MainApp.TPVariables[10][1]  = "min"; break;
		}
		MainApp.TPVariables[11][0]  = "TimeScale2";
        MainApp.TPVariables[11][1]  =  MainApp.TPVariables[10][1];		// TimeScale ChoiceBoxes are synchronized !!

		MainApp.TPVariables[12][0]  = "TimeScale3";
	    MainApp.TPVariables[12][1]  =  MainApp.TPVariables[10][1];		// TimeScale ChoiceBoxes are synchronized !!

		MainApp.TPVariables[13][0]  = "NrDrops1";
		MainApp.TPVariables[13][1]  = MainApp.TProject.getNrDrops1().toString();

		MainApp.TPVariables[14][0]  = "Closed11";
		MainApp.TPVariables[14][1]  = MainApp.TProject.getClosed11().toString();

		MainApp.TPVariables[15][0]  = "Open11";
		MainApp.TPVariables[15][1]  = MainApp.TProject.getOpen11().toString();

		MainApp.TPVariables[16][0]  = "Closed12";
		MainApp.TPVariables[16][1]  = MainApp.TProject.getClosed12().toString();

		MainApp.TPVariables[17][0]  = "Open12";
		MainApp.TPVariables[17][1]  = MainApp.TProject.getOpen12().toString();

		MainApp.TPVariables[18][0]  = "Closed13";
		MainApp.TPVariables[18][1]  = MainApp.TProject.getClosed13().toString();

		MainApp.TPVariables[19][0]  = "Open13";
		MainApp.TPVariables[19][1]  = MainApp.TProject.getOpen13().toString();

		MainApp.TPVariables[20][0]  = "TimeScale4";
		MainApp.TPVariables[20][1]  = MainApp.TPVariables[10][1];		// TimeScale ChoiceBoxes are synchronized !!

		MainApp.TPVariables[21][0]  = "NrDrops2";
		MainApp.TPVariables[21][1]  = MainApp.TProject.getNrDrops2().toString();

		MainApp.TPVariables[22][0]  = "Closed21";
		MainApp.TPVariables[22][1]  = MainApp.TProject.getClosed21().toString();

		MainApp.TPVariables[23][0]  = "Open21";
		MainApp.TPVariables[23][1]  = MainApp.TProject.getOpen21().toString();

		MainApp.TPVariables[24][0]  = "Closed22";
		MainApp.TPVariables[24][1]  = MainApp.TProject.getClosed22().toString();

		MainApp.TPVariables[25][0]  = "Open22";
		MainApp.TPVariables[25][1]  = MainApp.TProject.getOpen22().toString();

		MainApp.TPVariables[26][0]  = "Closed23";
		MainApp.TPVariables[26][1]  = MainApp.TProject.getClosed23().toString();

		MainApp.TPVariables[27][0]  = "Open23";
		MainApp.TPVariables[27][1]  = MainApp.TProject.getOpen23().toString();

		MainApp.TPVariables[28][0]  = "TimeScale5";
		MainApp.TPVariables[28][1]  = MainApp.TPVariables[10][1];		// TimeScale ChoiceBoxes are synchronized !!

		MainApp.TPVariables[29][0]  = "NrDrops3";
		MainApp.TPVariables[29][1]  = MainApp.TProject.getNrDrops3().toString();
		
		MainApp.TPVariables[30][0]  = "Closed31";
		MainApp.TPVariables[30][1]  = MainApp.TProject.getClosed31().toString();
		
		MainApp.TPVariables[31][0]  = "Open31";
		MainApp.TPVariables[31][1]  = MainApp.TProject.getOpen31().toString();
		
		MainApp.TPVariables[32][0]  = "Closed32";
		MainApp.TPVariables[32][1]  = MainApp.TProject.getClosed32().toString();
		
		MainApp.TPVariables[33][0]  = "Open32";
		MainApp.TPVariables[33][1]  = MainApp.TProject.getOpen32().toString();
		
		MainApp.TPVariables[34][0]  = "Closed33";
		MainApp.TPVariables[34][1]  = MainApp.TProject.getClosed33().toString();
		
		MainApp.TPVariables[35][0]  = "Open33";
		MainApp.TPVariables[35][1]  = MainApp.TProject.getOpen33().toString();
		
		MainApp.TPVariables[36][0]  = "TimeScale6";
		MainApp.TPVariables[36][1]  = MainApp.TPVariables[10][1];	

		MainApp.TPVariables[37][0]  = "ProjectCommentText";
		MainApp.TPVariables[37][1]  = MainApp.TProject.getProjectCommentText(); 
}	
	
	// DeserializeTriggerProject()
	// Take the values for the TProject variables from the global array TPVariables[][]
	private void DeserializeTriggerProject() {

		//[1,1]: ProjectName
		MainApp.TProject.setProjectName(MainApp.TPVariables[1][1].toString().substring(5));	

		// [2,1]: ProjectPath
		MainApp.TProject.setProjectPath(MainApp.TPVariables[2][1].toString().substring(5));	
		// [3,1]: ShutterLag
		MainApp.TProject.setShutterLag(Integer.parseInt((MainApp.TPVariables[3][1]).toString().trim()));
		// [4,1] ShutterSpeed
		MainApp.TProject.setShutterSpeed(Integer.parseInt((MainApp.TPVariables[4][1]).toString().trim()));	
		// [5,1] BulbModel
		if ((MainApp.TPVariables[5][1].trim()).equals("on")) {
			MainApp.TProject.setBulbMode(ctrl.on);
		}
		else {
			MainApp.TProject.setBulbMode(ctrl.off);
		}
		// [6,1] CameraTrigger
		if ((MainApp.TPVariables[6][1].trim()).equals("start")) 
			MainApp.TProject.setCameraTrigger(trigger.start);
		else 
			MainApp.TProject.setCameraTrigger(trigger.sensor1);
		// [7,1]: Time to Flash
		MainApp.TProject.setTimeToFlash(Integer.parseInt((MainApp.TPVariables[7][1]).toString().trim()));	
		// [8,1]: NrFlashes
		MainApp.TProject.setNrFlashes(Integer.parseInt((MainApp.TPVariables[8][1]).toString().trim()));	
		// [9,1]: NrValves
		MainApp.TProject.setNrValves(Integer.parseInt((MainApp.TPVariables[9][1]).toString().trim()));	
		// [10,1]: TimeScale1
		switch ((MainApp.TPVariables[10][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale1(0); break;
        case "ms" : MainApp.TProject.setTimeScale1(1); break;
        case "s"  : MainApp.TProject.setTimeScale1(2); break;
        case "min": MainApp.TProject.setTimeScale1(3); break;
		}
		// [11,1]: TimeScale2
		switch ((MainApp.TPVariables[11][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale2(0); break;
        case "ms" : MainApp.TProject.setTimeScale2(1); break;
        case "s"  : MainApp.TProject.setTimeScale2(2); break;
        case "min": MainApp.TProject.setTimeScale2(3); break;
		}
		// [12,1]: TimeScale3
		switch ((MainApp.TPVariables[12][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale3(0); break;
        case "ms" : MainApp.TProject.setTimeScale3(1); break;
        case "s"  : MainApp.TProject.setTimeScale3(2); break;
        case "min": MainApp.TProject.setTimeScale3(3); break;
		}
		// [13,1] NrDrops1
		MainApp.TProject.setNrDrops1(Integer.parseInt(((MainApp.TPVariables[13][1]).toString()).trim()));	
		// [14,1] Closed11
		MainApp.TProject.setClosed11(Integer.parseInt(((MainApp.TPVariables[14][1]).toString()).trim()));	
		// [15,1] Open11
		MainApp.TProject.setOpen11(Integer.parseInt(((MainApp.TPVariables[15][1]).toString()).trim()));	
		// [16,1] Closed12
		MainApp.TProject.setClosed12(Integer.parseInt(((MainApp.TPVariables[16][1]).toString()).trim()));	
		// [17,1] Open12
		MainApp.TProject.setOpen12(Integer.parseInt(((MainApp.TPVariables[17][1]).toString()).trim()));	
		// [18,1] Closed13
		MainApp.TProject.setClosed13(Integer.parseInt(((MainApp.TPVariables[18][1]).toString()).trim()));	
		// [19,1] Open13
		MainApp.TProject.setOpen13(Integer.parseInt(((MainApp.TPVariables[19][1]).toString()).trim()));	
		// [20,1]: TimeScale4
		switch ((MainApp.TPVariables[20][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale4(0); break;
        case "ms" : MainApp.TProject.setTimeScale4(1); break;
        case "s"  : MainApp.TProject.setTimeScale4(2); break;
        case "min": MainApp.TProject.setTimeScale4(3); break;
		}
		// [21,1] NrDrops2
		MainApp.TProject.setNrDrops2(Integer.parseInt(((MainApp.TPVariables[21][1]).toString()).trim()));	
		// [22,1] Closed21
		MainApp.TProject.setClosed21(Integer.parseInt(((MainApp.TPVariables[22][1]).toString()).trim()));	
		// [23,1] Open21
		MainApp.TProject.setOpen21(Integer.parseInt(((MainApp.TPVariables[23][1]).toString()).trim()));	
		// [24,1] Closed22
		MainApp.TProject.setClosed22(Integer.parseInt(((MainApp.TPVariables[24][1]).toString()).trim()));	
		// [25,1] Open22
		MainApp.TProject.setOpen22(Integer.parseInt(((MainApp.TPVariables[25][1]).toString()).trim()));	
		// [26,1] Closed23
		MainApp.TProject.setClosed23(Integer.parseInt(((MainApp.TPVariables[26][1]).toString()).trim()));	
		// [27,1] Open23
		MainApp.TProject.setOpen23(Integer.parseInt(((MainApp.TPVariables[27][1]).toString()).trim()));	
		// [28,1]: TimeScale5
		switch ((MainApp.TPVariables[28][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale5(0); break;
        case "ms" : MainApp.TProject.setTimeScale5(1); break;
        case "s"  : MainApp.TProject.setTimeScale5(2); break;
        case "min": MainApp.TProject.setTimeScale5(3); break;
		}
		// [29,1] NrDrops3
		MainApp.TProject.setNrDrops3(Integer.parseInt(((MainApp.TPVariables[29][1]).toString()).trim()));	
		// [30,1] Closed31
		MainApp.TProject.setClosed31(Integer.parseInt(((MainApp.TPVariables[30][1]).toString()).trim()));	
		// [31,1] Open31
		MainApp.TProject.setOpen31(Integer.parseInt(((MainApp.TPVariables[31][1]).toString()).trim()));	
		// [32,1] Closed32
		MainApp.TProject.setClosed32(Integer.parseInt(((MainApp.TPVariables[32][1]).toString()).trim()));	
		// [33,1] Open32
		MainApp.TProject.setOpen32(Integer.parseInt(((MainApp.TPVariables[33][1]).toString()).trim()));	
		// [34,1] Closed33
		MainApp.TProject.setClosed33(Integer.parseInt(((MainApp.TPVariables[34][1]).toString()).trim()));	
		// [35,1] Open33
		MainApp.TProject.setOpen33(Integer.parseInt(((MainApp.TPVariables[35][1]).toString()).trim()));	
		
		// [36,1]: TimeScale6
		switch ((MainApp.TPVariables[36][1]).trim().toString()){
        case "us" : MainApp.TProject.setTimeScale6(0); break;
        case "ms" : MainApp.TProject.setTimeScale6(1); break;
        case "s"  : MainApp.TProject.setTimeScale6(2); break;
        case "min": MainApp.TProject.setTimeScale6(3); break;
		}
		// [37,1] ProjectCommentText
		// 6 blanks in front of the input text must be removed 
		MainApp.TProject.setProjectCommentText(MainApp.TPVariables[37][1].substring(5));	
	}

    // handleSTART   
    // Called when the user clicks the START button.
    @FXML
    private void handleSTART() throws IOException, InterruptedException {
    	
    	if (!EnableOutput.isSelected()) return;
    	
    	TriggerAction triggerAction = new TriggerAction();
    	triggerAction.setupTriggerActionList();
    	triggerAction.Trigger();
    }
    
    // handleEnableOutput         
    // Called when the user clicks the EnableOutput checkbox.
    // enables/disables the output ports 
    @FXML
    private void handleEnableOutput() {
	    if (EnableOutput.isSelected()) {
	    	OutputsActive.fillProperty().setValue(Color.rgb(0, 255, 0));
	    	OutputsActive.setStrokeWidth(0);
	    	MainApp.EnableOutput = true;
	    }
	    else {
	    	OutputsActive.fillProperty().setValue(Color.rgb(255, 0, 0));
	    	OutputsActive.setStrokeWidth(0);
	    	MainApp.EnableOutput = false;
	    }  	
    }

    // handleReset
    // rest all parameters to their default values
    @FXML
    private void handleReset() {

    	MainApp.TProject = new TriggerProject("defaultProject");	
    	initialize();
    	
    	// restore the default project file
    	File outputFile = new File("./defaultProject.prj");
        try {
            FileWriter filewriter = new FileWriter(outputFile);
            BufferedWriter bufferdwriter = new BufferedWriter(filewriter);
            String nrTPVariables     = MainApp.TPVariables[0][1];
            
            // number of the objects to be output
            int numLines = Integer.parseInt(nrTPVariables);

            // write File Header Line
            bufferdwriter.write("---------- RasPiTrigger Project ----------\n");
            // write all TProject objects to a file: one line for each object
            // line format:  <object name> <object value> 
        	for (int i=1; i<= numLines; i++) {
        		bufferdwriter.write (utilities.extend(MainApp.TPVariables[i][0],25));
        		bufferdwriter.write (MainApp.TPVariables[i][1] + "\n");
        	}
        	bufferdwriter.flush();
        	bufferdwriter.close();
        } catch (IOException ex) {
        	System.out.println("Cannot write project to file " + outputFile.toString() );
        }
    }
    
    // OpenValve
    // opens an closed valve when the purge button is pressed
    private void OpenValve(int valveNr) {
    	if (!MainApp.EnableOutput) return;
    	if(!isLinux) return;
    	switch (valveNr) {
    	case 1: MainApp.outPort5.high(); break;
    	case 2:	MainApp.outPort6.high(); break; 
    	case 3:	MainApp.outPort7.high(); break;
    	}
    }
    	
    // CloseValve
    // closes an open valve when the purge button is released
    private void CloseValve(int valveNr) {
    	if (!MainApp.EnableOutput) return;
    	if(!isLinux) return;
    	switch (valveNr) {
    	case 1: MainApp.outPort5.low(); break;
    	case 2:	MainApp.outPort6.low(); break; 
    	case 3:	MainApp.outPort7.low(); break;
    	}
    } 
}


	


