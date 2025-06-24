package trigger.model;

import java.io.File;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public final class TriggerProject   {
	
	public enum device			{ sensor, camera, flash1, flash2, flash3, valve1, valve2, valve3, none }
	public enum ctrl     		{ on, off }
	public enum trigger   		{ start, sensor1 }
	public enum cmd				{ openValve, closeValve, openShutter, closeShutter, fireFlash, activatePorts, deactivatePorts, none }
	
	// project parameters
    private final StringProperty 		ProjectName;		// Name of the project 
    private final StringProperty 		ProjectPath;		// Path to the project file
    
    // camera and flash parameters
    private final IntegerProperty 		ShutterLag;			// Shutter Lag
    private final IntegerProperty 		ShutterSpeed;		// bulb exposure time if ShutterCtrl=ON
    private final IntegerProperty 		TimeToFlash;   		// delay until the flashs are triggered
    private final IntegerProperty 		NrFlashes;			// number of flashes 
    private final IntegerProperty 		NrValves;			// number of valves 

    private final IntegerProperty    	TimeScale1;			// TimeScale for ShutterLag
    private final IntegerProperty    	TimeScale2;			// TimeScale for ShutterSpeed
    private final IntegerProperty    	TimeScale3;			// TimeScale for TimeToFlash
    
    // valve1 parameters
    private final IntegerProperty 		NrDrops1;		
    private final IntegerProperty 		Closed11;	
    private final IntegerProperty 		Open11;			
    private final IntegerProperty 		Closed12;				 
    private final IntegerProperty 		Open12;				
    private final IntegerProperty 		Closed13;				 
    private final IntegerProperty 		Open13;
    private final IntegerProperty    	TimeScale4;
    
    // valve2 parameters
    private final IntegerProperty 		NrDrops2;		 
    private final IntegerProperty 		Closed21;		
    private final IntegerProperty 		Open21;		
    private final IntegerProperty 		Closed22;				 
    private final IntegerProperty 		Open22;				
    private final IntegerProperty 		Closed23;				 
    private final IntegerProperty 		Open23;	
    private final IntegerProperty    	TimeScale5;

    // valve3 parameters
    private final IntegerProperty 		NrDrops3;			 
    private final IntegerProperty 		Closed31;			
    private final IntegerProperty 		Open31;				
    private final IntegerProperty 		Closed32;				 
    private final IntegerProperty 		Open32;				
    private final IntegerProperty 		Closed33;				 
    private final IntegerProperty 		Open33;	
    private final IntegerProperty    	TimeScale6;   
    
	// radio button states
	private ctrl 			 			BulbMode;		
	private trigger 			 		CameraTrigger;		

	// port allocation
	final device[] ports 		= new device[8];	
	final String[] portcolors 	= new String[8];
	
	// project comment text
	private final StringProperty ProjectCommentText;

	// constructor:
	// initializes the data fields of a project
	// if no project file exists for the project name, a new project is created
	public TriggerProject (String ProjectName)
	{
		// instantiate the TriggerProject member variables
		this.ProjectName 		 = new SimpleStringProperty(ProjectName);
		this.ProjectPath 		 = new SimpleStringProperty();
		
		this.ShutterLag 		 = new SimpleIntegerProperty();
		this.ShutterSpeed 		 = new SimpleIntegerProperty();
		this.TimeToFlash 	 	 = new SimpleIntegerProperty();
		this.NrFlashes			 = new SimpleIntegerProperty();
		this.NrValves			 = new SimpleIntegerProperty();
		this.TimeScale1			 = new SimpleIntegerProperty(); 
		this.TimeScale2			 = new SimpleIntegerProperty(); 
		this.TimeScale3			 = new SimpleIntegerProperty(); 

		this.NrDrops1			 = new SimpleIntegerProperty();
		this.Closed11			 = new SimpleIntegerProperty();
		this.Open11				 = new SimpleIntegerProperty();
		this.Closed12			 = new SimpleIntegerProperty();
		this.Open12				 = new SimpleIntegerProperty();
		this.Closed13			 = new SimpleIntegerProperty();
		this.Open13				 = new SimpleIntegerProperty();
		this.TimeScale4 		 = new SimpleIntegerProperty();  
		
		this.NrDrops2			 = new SimpleIntegerProperty();
		this.Closed21			 = new SimpleIntegerProperty();
		this.Open21				 = new SimpleIntegerProperty();
		this.Closed22			 = new SimpleIntegerProperty();
		this.Open22				 = new SimpleIntegerProperty();
		this.Closed23			 = new SimpleIntegerProperty();
		this.Open23				 = new SimpleIntegerProperty();	
		this.TimeScale5          = new SimpleIntegerProperty();  
		
		this.NrDrops3			 = new SimpleIntegerProperty();
		this.Closed31			 = new SimpleIntegerProperty();
		this.Open31				 = new SimpleIntegerProperty();
		this.Closed32			 = new SimpleIntegerProperty();
		this.Open32				 = new SimpleIntegerProperty();
		this.Closed33			 = new SimpleIntegerProperty();
		this.Open33				 = new SimpleIntegerProperty();	
		this.TimeScale6          = new SimpleIntegerProperty();  
		
		this.BulbMode		 	 = ctrl.off;
		this.CameraTrigger		 = trigger.start;
		
		this.ProjectCommentText  = new SimpleStringProperty();
		
		initializeMemberVariables();
	}
	
	// setupTPvariables
	// initialize the array TPvariables with the default project parameters
	private void initializeMemberVariables(){
		
		String currentDirectory;
		File file = new File(".");
		currentDirectory = file.getAbsolutePath();
		setProjectPath(currentDirectory);
		setProjectName("defaultProject");
				
		setShutterLag(150);
		setBulbMode(ctrl.on);
		setCameraTrigger(trigger.start);
		setShutterSpeed(250);
		setTimeToFlash(350);
		setNrFlashes(3);
		setNrValves(3);
		
		setNrDrops1(1);
		setClosed11(0);
		setOpen11(55);
		setClosed12(0);
		setOpen12(0);
		setClosed13(0);
		setOpen13(0);
		
		setNrDrops2(0);
		setClosed21(0);
		setOpen21(0);
		setClosed22(0);
		setOpen22(0);
		setClosed23(0);
		setOpen23(0);
		
		setNrDrops3(0);
		setClosed31(0);
		setOpen31(0);
		setClosed32(0);
		setOpen32(0);
		setClosed33(0);
		setOpen33(0);
		
		setTimeScale1 (1);
		setTimeScale2 (1);
		setTimeScale3 (1);
		setTimeScale4 (1);
		setTimeScale5 (1);
		setTimeScale6 (1);
		
		for (int i = 0; i<8; i++) { setPort(i++, device.none); }
		setPort(0, device.camera);
		setPort(1, device.flash1);
		setPort(2, device.valve1);
		
		setProjectCommentText("Default project:  Parameters for a crown.");
	}
	
	// define get/set methods for the member variables
	// -----------------------------------------------
	public  device getPort(int index) { return ports[index]; }
	public  void   setPort(int index, device dev) { ports[index] = dev; setPortcolors(index, deviceColor (dev));}
	private String deviceColor (device dev) {
		if ( (dev == device.camera) ) { return "blue"; }
		else if ( (dev == device.flash1) | (dev == device.flash2) | (dev == device.flash3) ) { return "#CFA700"; }
		else if ( dev == device.valve1) { return "green"; }
		else if ( dev == device.valve2) { return "red"; }
		else if ( dev == device.valve3) { return "saddlebrown"; }
		return "magenta";
	}	
	public  String getPortcolors(int index) 				{ return portcolors[index]; }
	public  void   setPortcolors(int index, String color) 	{ portcolors[index] = color; }
	
	public  ctrl 	getBulbMode() 							{ return BulbMode; }
	public  void    setBulbMode(ctrl control) 				{ BulbMode = control; }

	public  trigger getCameraTrigger() 						{ return CameraTrigger; }
	public  void    setCameraTrigger(trigger cTrigger) 		{ CameraTrigger = cTrigger; }

	public String getProjectName()                          { return ProjectName.get(); }
    public void setProjectName(String ProjectName)          { this.ProjectName.set(ProjectName); }
    
    public String getProjectPath()                          { return ProjectPath.get(); }
    public void setProjectPath(String ProjectPath)          { this.ProjectPath.set(ProjectPath); }
    
    public Integer getShutterLag()                          { return ShutterLag.get(); }
    public void setShutterLag(Integer ShutterLag)           {  this.ShutterLag.set(ShutterLag); } 												
    
    public Integer getShutterSpeed()                        { return ShutterSpeed.get(); }
    public void setShutterSpeed(Integer ShutterSpeed)       { this.ShutterSpeed.set(ShutterSpeed); }
 
    public Integer getTimeToFlash()                         { return TimeToFlash.get(); }
    public void setTimeToFlash(Integer TimeBeforeFlash)     { this.TimeToFlash.set(TimeBeforeFlash); }

    public Integer getNrFlashes()               			{ return NrFlashes.get(); }
    public void setNrFlashes(Integer NrFlashes) 			{ this.NrFlashes.set(NrFlashes); }
    
    public Integer getNrValves()                			{ return NrValves.get(); }
    public void setNrValves(Integer NrValves)   			{ this.NrValves.set(NrValves); }
    
    public Integer getTimeScale1()							{ return  TimeScale1.getValue(); }
    public void setTimeScale1 (Integer index)				{ this.TimeScale1.set(index); }  
	public String getTimeScale1Str () 						{	switch(TimeScale1.getValue()){  
													    			case 0:  return "us"; 
													    			case 1:  return "ms"; 
													    			case 2:  return "s"; 
													    			case 3:  return "min"; 
																}
															  return "ms";	
															} 
    
    public Integer getTimeScale2()							{ return TimeScale2.getValue(); }  
    public void setTimeScale2 (Integer index)				{ this.TimeScale2.set(index); }
	public String getTimeScale2Str () 						{	switch(TimeScale2.getValue()){  
												    				case 0:  return "us"; 
												    				case 1:  return "ms"; 
												    				case 2:  return "s"; 
												    				case 3:  return "min"; 
																}
																return "ms";	
															} 

    public Integer getTimeScale3()							{ return TimeScale3.getValue();  }  
    public void setTimeScale3 (Integer index)				{ this.TimeScale3.set(index);} 
	public String getTimeScale3Str () 						{	switch(TimeScale3.getValue()){  
													    			case 0:  return "us"; 
													    			case 1:  return "ms"; 
													    			case 2:  return "s"; 
													    			case 3:  return "min"; 
																}
																return "ms";	
															}  

    public Integer getNrDrops1()                			{ return NrDrops1.get(); }
    public void setNrDrops1(Integer NrDrops1) 				{ this.NrDrops1.set(NrDrops1); }
    
    public Integer getClosed11()                			{ return Closed11.get(); }
    public void setClosed11(Integer Closed11) 				{ this.Closed11.set(Closed11); }
    
    public Integer getOpen11()                  			{ return Open11.get(); }
    public void setOpen11(Integer Open11) 	    			{ this.Open11.set(Open11); }
    
    public Integer getClosed12()                			{ return Closed12.get(); }
    public void setClosed12(Integer Closed12) 				{ this.Closed12.set(Closed12); }
    
    public Integer getOpen12()                  			{ return Open12.get(); }
    public void setOpen12(Integer Open12) 	    			{ this.Open12.set(Open12); }
 
    public Integer getClosed13()                			{ return Closed13.get(); }
    public void setClosed13(Integer Closed13)   			{ this.Closed13.set(Closed13); }
    
    public Integer getOpen13()                  			{ return Open13.get(); }
    public void setOpen13(Integer Open13) 	    			{ this.Open13.set(Open13); }
    
    public Integer getTimeScale4()							{ return TimeScale4.getValue(); } 
    public void setTimeScale4 (Integer index)				{ this.TimeScale4.set(index); } 
	public String getTimeScale4Str () 						{	switch(TimeScale4.getValue()){  
																    case 0:  return "us"; 
																    case 1:  return "ms"; 
																    case 2:  return "s"; 
																    case 3:  return "min"; 
																}
																return "ms";	
															} 

    public Integer getNrDrops2()                			{ return NrDrops2.get(); }
    public void setNrDrops2(Integer NrDrops2) 				{ this.NrDrops2.set(NrDrops2); }
    
    public Integer getClosed21()                			{ return Closed21.get(); }
    public void setClosed21(Integer Closed21) 				{ this.Closed21.set(Closed21); }
    
    public Integer getOpen21()                  			{ return Open21.get(); }
    public void setOpen21(Integer Open21) 	    			{ this.Open21.set(Open21); }
    
    public Integer getClosed22()                			{ return Closed22.get(); }
    public void setClosed22(Integer Closed22) 				{ this.Closed22.set(Closed22); }
    
    public Integer getOpen22()                  			{ return Open22.get(); }
    public void setOpen22(Integer Open22) 	    			{ this.Open22.set(Open22); }
 
    public Integer getClosed23()                			{ return Closed23.get(); }
    public void setClosed23(Integer Closed23)   			{ this.Closed23.set(Closed23); }
    
    public Integer getOpen23()                  			{ return Open23.get(); }
    public void setOpen23(Integer Open23) 	    			{ this.Open23.set(Open23); }
    
    public Integer getTimeScale5()							{ return TimeScale5.getValue();  }  
    public void setTimeScale5 (Integer index)				{ this.TimeScale5.set(index);} 
	public String getTimeScale5Str () 						{	switch(TimeScale5.getValue()){  
																    case 0:  return "us"; 
																    case 1:  return "ms"; 
																    case 2:  return "s"; 
																    case 3:  return "min"; 
																}
																return "ms";	
															} 

    public Integer getNrDrops3()                			{ return NrDrops3.get(); }
    public void setNrDrops3(Integer NrDrops1) 				{ this.NrDrops3.set(NrDrops1); }
    
    public Integer getClosed31()                			{ return Closed31.get(); }
    public void setClosed31(Integer Closed31) 				{ this.Closed31.set(Closed31); }
    
    public Integer getOpen31()                  			{ return Open31.get(); }
    public void setOpen31(Integer Open31) 	    			{ this.Open31.set(Open31); }
    
    public Integer getClosed32()                			{ return Closed32.get(); }
    public void setClosed32(Integer Closed32) 				{ this.Closed32.set(Closed32); }
    
    public Integer getOpen32()                  			{ return Open32.get(); }
    public void setOpen32(Integer Open32) 	    			{ this.Open32.set(Open32); }
 
    public Integer getClosed33()                			{ return Closed33.get(); }
    public void setClosed33(Integer Closed33)   			{ this.Closed33.set(Closed33); }
    
    public Integer getOpen33()                  			{ return Open33.get(); }
    public void setOpen33(Integer Open33) 	    			{ this.Open33.set(Open33); }
    
    public Integer getTimeScale6()							{ return TimeScale6.getValue(); }  
    public void setTimeScale6 (Integer index)				{ this.TimeScale6.set(index); } 
    public String getTimeScale6Str () 						{	switch(TimeScale6.getValue()){  
												        			case 0:  return "us"; 
												        			case 1:  return "ms"; 
												        			case 2:  return "s"; 
												        			case 3:  return "min"; 
																}
																return "ms";	
											    			}  
    
	public String getProjectCommentText()               	{ return ProjectCommentText.get(); }
    public void setProjectCommentText(String comment)    	{ this.ProjectCommentText.set(comment.replace("\n",  "__")); }
    
}
