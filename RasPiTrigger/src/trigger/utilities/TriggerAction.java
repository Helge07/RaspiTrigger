package trigger.utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.*;

import trigger.MainApp;
import trigger.model.Action;
import trigger.model.TriggerProject.cmd;
import trigger.model.TriggerProject.ctrl;
import trigger.model.TriggerProject.device;
	
public class TriggerAction 
{ 
	Action tmpAction = new trigger.model.Action();
	
	Boolean isLinux=true;
	
	int actionNr;
	int CameraPortNr   = 0;
	int FlashPortNr    = 1;
	int ValvePortNr    = 5;
	int timescale      = MainApp.TProject.getTimeScale1();
	int currentTime    = 0;
	int startTimeDrop3 = 0;
	
    // parameters of the valve1 timing
	int lck11   = MainApp.TProject.getClosed11();
	int opn11   = MainApp.TProject.getOpen11();
	int lck12   = MainApp.TProject.getClosed12();
	int opn12   = MainApp.TProject.getOpen12();
	int lck13   = MainApp.TProject.getClosed13();
	int opn13   = MainApp.TProject.getOpen13(); 
    
    // parameters of the valve2 timing
	int lck21   = MainApp.TProject.getClosed21();
	int opn21   = MainApp.TProject.getOpen21();
	int lck22   = MainApp.TProject.getClosed22();
	int opn22   = MainApp.TProject.getOpen22();
	int lck23   = MainApp.TProject.getClosed23();
	int opn23   = MainApp.TProject.getOpen23(); 
  
    // parameters of the valve3 timing
	static int lck31   = MainApp.TProject.getClosed31();
	int opn31   = MainApp.TProject.getOpen31();
	int lck32   = MainApp.TProject.getClosed32();
	int opn32   = MainApp.TProject.getOpen32();
	int lck33   = MainApp.TProject.getClosed33();
	int opn33   = MainApp.TProject.getOpen33(); 	
    
    // Array list with a sequence of trigger actions
    ArrayList<Action> TriggerActions  = new ArrayList<>();
	String[] 		  TriggerCommands = new String[20];
	
	int cmdIndex;

    // Generate TriggerActions from the TProject parameters and sort the by time of execution
	public void setupTriggerActionList () { 

		actionNr = 0;
		TriggerActions.clear();
	
		// 'openShutter' command
		// Time = TimeToFlash - ShutterLag 
		tmpAction.setDev(device.camera);
		tmpAction.setPort(CameraPortNr);
		tmpAction.setTime(MainApp.TProject.getTimeToFlash() - MainApp.TProject.getShutterLag());
		tmpAction.setTimeScale(timescale);
		tmpAction.setCommand(cmd.openShutter);
		tmpAction.setParameter(0);
		TriggerActions.add(new Action ( tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), 
										tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));

		// in case of bulbMode='ctrl.on' the 'closeShutter' is provided by the RasPiTrigger
		// shutter closing time point= TimeToFlash - ShutterLag + ShutterSpeed
		// in case of bulbMode='ctrl off' the camera itself is responsible for the ShutterSpeed
		if (MainApp.TProject.getBulbMode() == ctrl.on) {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.camera);
			tmpAction.setPort(CameraPortNr);
			tmpAction.setTime(MainApp.TProject.getTimeToFlash() - MainApp.TProject.getShutterLag() + MainApp.TProject.getShutterSpeed());
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.closeShutter);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action ( tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));
		}
		
		// 'fireFlash' command for flash1
		// FlashPortNr starts with 1;
		// Time = TimeToFlash
		if (MainApp.TProject.getNrFlashes() >=1 )  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.flash1);
			tmpAction.setPort(FlashPortNr++);
			tmpAction.setTime(MainApp.TProject.getTimeToFlash());
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.fireFlash);
			tmpAction.setParameter(50);	
			TriggerActions.add(new Action ( tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));
		}
		
		// Setup the 'fireFlash' command for flash2
		// FlashPortNr is now 2;
		// Time = TimeToFlash
		if (MainApp.TProject.getNrFlashes() >=2 )  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.flash2);
			tmpAction.setPort(FlashPortNr++);
			tmpAction.setTime(MainApp.TProject.getTimeToFlash());
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.fireFlash);
			tmpAction.setParameter(50);
			TriggerActions.add(new Action (tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));
		}

		// Setup the 'fireFlash' command for flash3
		// FlashPortNr is now 3
		// Time = TimeToFlash
		if (MainApp.TProject.getNrFlashes() == 3)  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.flash3);
			tmpAction.setPort(FlashPortNr);
			tmpAction.setTime(MainApp.TProject.getTimeToFlash());
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.fireFlash);
			tmpAction.setParameter(50);	
			TriggerActions.add(new Action ( tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));
		}
		
		// Generate the trigger commands for valve1, valve2, valv3
		int NrValves = MainApp.TProject.getNrValves();
		if ( NrValves >= 1) {
			// ValvePortNr starts with 5
			generateValve1Commands(ValvePortNr++);
		}
		if ( NrValves >= 2) {
			// use ValvePortNr 6
			generateValve2Commands(ValvePortNr++);
		}
		if ( NrValves == 3) {
			// use ValvePortNr 7
			generateValve3Commands(ValvePortNr);
		}
		
		// Sort the TiggerActionList
		TriggerActions.sort(Comparator.comparingInt(Action::getTime));
		
	}
	private  void generateValve1Commands(int portNr) {
				
		int Drops1 	= MainApp.TProject.getNrDrops1();
		
		if (Drops1 == 0)  return;

		// first open and close command for valve1
		// Time = lck11
		if (Drops1 >=1)  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve1);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck11);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.openValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve1);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck11 + opn11);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.closeValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
		}
		
		// second open and close command for valve1
		// Time = lck11 + opn11 + lck12
		if (Drops1 >=2)  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve1);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck11 + opn11 + lck12);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.openValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve1);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck11 +opn11+lck12+opn12);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.closeValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
		}

		// all following open commands for valve1
		if (Drops1 >=3)  {
	        startTimeDrop3 = lck11 + opn11 + lck12 + opn12;
	        for (int i=1; i<= Drops1 - 2; i++) {
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve1);
				tmpAction.setPort(portNr);
				tmpAction.setTime(startTimeDrop3+lck13);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.openValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve1);
				tmpAction.setPort(portNr);
				tmpAction.setTime(startTimeDrop3 + lck13 + opn13);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.closeValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
				startTimeDrop3  += opn13+lck13;
	        }
		}
	}
	
	private  void generateValve2Commands(int portNr) {
		
		int Drops2 = MainApp.TProject.getNrDrops2();
		if (Drops2 ==0)  return;
		
		// first open and close command for valve2
		// Time = lck21
		if (Drops2 >=1)  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve2);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck21);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.openValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve2);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck21 + opn21);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.closeValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
		}
		
		// second open and close command for valve2
		// Time = lck21 + opn21 + lck22
		if (Drops2 >=2)  {
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve2);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck21 +opn21+lck22);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.openValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			tmpAction.setStep(actionNr++);
			tmpAction.setDev(device.valve2);
			tmpAction.setPort(portNr);
			tmpAction.setTime(lck21 +opn21+lck22+opn22);
			tmpAction.setTimeScale(timescale);
			tmpAction.setCommand(cmd.closeValve);
			tmpAction.setParameter(0);
			TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
											tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
		}

		// all following open commands for valve1
		if (Drops2 >=3)  {
	        startTimeDrop3 = lck21 + opn21 + lck22 + opn22;
	        for (int i=1; i<= Drops2 - 2; i++) {
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve2);
				tmpAction.setPort(portNr);
				tmpAction.setTime(startTimeDrop3+lck23);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.openValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));			
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve2);
				tmpAction.setPort(portNr);
				tmpAction.setTime(startTimeDrop3 + lck23 + 	opn23);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.closeValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
				startTimeDrop3  += opn23+lck23;			
	        }
		}
	}
	
	private  void generateValve3Commands(int portNr) {
			
			int Drops3 = MainApp.TProject.getNrDrops3();
			if (Drops3 ==0)  return;
			
			// first open and close command for valve2
			// Time = lck31
			if (Drops3 >=1)  {
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve3);
				tmpAction.setPort(portNr);
				tmpAction.setTime(lck31);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.openValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));			
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve3);
				tmpAction.setPort(portNr);
				tmpAction.setTime(lck31 + opn31);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.closeValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			}
			// second open and close command for valve2
			// Time = lck31 + opn31 + lck22
			if (Drops3 >=2)  {
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve3);
				tmpAction.setPort(portNr);
				tmpAction.setTime(lck31 +opn31+lck32);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.openValve);
				tmpAction.setParameter(0);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
												tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
				tmpAction.setStep(actionNr++);
				tmpAction.setDev(device.valve3);
				tmpAction.setPort(portNr);
				tmpAction.setTime(lck31 +opn31 + lck32 + opn32);
				tmpAction.setTimeScale(timescale);
				tmpAction.setCommand(cmd.closeValve);
				tmpAction.setParameter(0);
				// MainApp.TriggerActions.add(tmpAction);
				TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
						tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
			}
			// all following open commands for valve1
			if (Drops3 >=3)  {
		        startTimeDrop3 = lck31 + opn31 + lck32 + opn32;
		        for (int i=1; i<= Drops3 - 2; i++) {
					tmpAction.setStep(actionNr++);
					tmpAction.setDev(device.valve3);
					tmpAction.setPort(portNr);
					tmpAction.setTime(startTimeDrop3 + lck33);
					tmpAction.setTimeScale(timescale);
					tmpAction.setCommand(cmd.openValve);
					tmpAction.setParameter(0);
					// MainApp.TriggerActions.add(tmpAction);
					TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(), tmpAction.getTime(), 
													tmpAction.getTimeScale(),tmpAction.getCommand(), tmpAction.getParameter() ));
					tmpAction.setStep(actionNr++);
					tmpAction.setDev(device.valve3);
					tmpAction.setPort(portNr);
					tmpAction.setTime(startTimeDrop3 + lck33 + opn33);
					tmpAction.setTimeScale(timescale);
					tmpAction.setCommand(cmd.closeValve);
					tmpAction.setParameter(0);
					TriggerActions.add(new Action (	tmpAction.getStep(), tmpAction.getDev(), tmpAction.getPort(),tmpAction.getTime(), 
													tmpAction.getTimeScale(),tmpAction.getCommand(),tmpAction.getParameter() ));
					startTimeDrop3  += opn33+lck23;
		        }
			}
		}
	        
	// Generate the Trigger program from the sorted TriggerActions and 
	// execute the sorted  TriggerActions as GPIO program
	public void Trigger() throws IOException, InterruptedException  {
		
        // prevent GPIO output if not running on the Raspberry Pi
		if (!(System.getProperty("os.name").equals("Linux"))) isLinux=false;

    	// make a time stamp before any action is performed
		SimpleDateFormat sdf = new SimpleDateFormat("m.ss.SSS");
		Calendar globalCal = Calendar.getInstance();		
		StringBuilder globalSB = new StringBuilder("Start time: ");
		globalSB.append(sdf.format(globalCal.getTime()) + "  ");

	    // Loop over all trigger actions in 'TriggerActions'
	    System.out.println("\n------------------------------------------------");
	    System.out.println("Trigger sequence starts ....\nStart time(min.sec.msec): "+ sdf.format(globalCal.getTime())  + "\n");
	    cmdIndex = 0;
	    TriggerActions.forEach(TriggerActions -> {

            // put the current trigger command into the String array 'TriggerCommands'
            int timeScale  	= TriggerActions.getTimeScale();		
            int actionTime 	= TriggerActions.getTime();	
            long delay 		= (long)(actionTime - currentTime);
            currentTime 	= actionTime;
            
 	    	// make a time stamp before the action is performed
    		//SimpleDateFormat sdf = new SimpleDateFormat("m.ss.SSS");
    		Calendar cal = Calendar.getInstance();		
    		StringBuilder sb = new StringBuilder("time (min.sec.msec): ");
    		sb.append(sdf.format(cal.getTime()) + "  ");
            
            // wait until the current command is 
            if (actionTime >= currentTime) {
            	switch (timeScale) {
            	case 0:	try {  // timescale = microseconds
	            		if(delay==0)            sb.append("Wait   " + delay + " us\t");
	        			else if (delay <100 )   sb.append("Wait  " + delay + " us\t");
	        			else                    sb.append("Wait " + delay + " us\t");
            			Thread.sleep(delay/1000);
					} catch (InterruptedException e) {
						System.out.println("TriggerAction.Trigger: Line 459");
						e.printStackTrace();
					} break;
            	case 1:	try {  // timescale = milliseconds
            			if(delay==0)            sb.append("Wait   " + delay + " ms\t");
            			else if (delay <100 )   sb.append("Wait  " + delay + " ms\t");
            			else                    sb.append("Wait " + delay + " ms\t");
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						System.out.println("TriggerAction.Trigger: Line 465");
						e.printStackTrace();
					} break;		
            	case 2:	try {  // timescale = seconds
	            		if(delay==0)            sb.append("Wait   " + delay + " s\t");
	        			else if (delay <100 )   sb.append("Wait  " + delay + " s\t");
	        			else                    sb.append("Wait " + delay + " s\t");
            			Thread.sleep(delay*1000);
					} catch (InterruptedException e) {
						System.out.println("TriggerAction.Trigger: Line 471");
						e.printStackTrace();
					} 		break;
            	case 3:	try {  // timescale = minutes
	            		if(delay==0)            sb.append("Wait   " + delay + " min\t");
	        			else if (delay <100 )   sb.append("Wait  " + delay + " min\t");
	        			else                    sb.append("Wait " + delay + " min\t");
            			Thread.sleep(delay * 1000 * 60);
					} catch (InterruptedException e) {
						System.out.println("TriggerAction.Trigger: Line 477");
						e.printStackTrace();
					} 		break;
            	}
            	switch (TriggerActions.getCommand()) {
				  
				  case	openShutter  	: 	// Port 0:  --> HIGH 
					  						sb.append("open Shutter");
					  						if (isLinux==true) MainApp.outPort0.high();
					  						break;
					  						
				  case	closeShutter	: 	//PORT 0 --> LOW
					  						sb.append("close Shutter");
					  						if (isLinux==true) MainApp.outPort0.low();
					  						break;			  						
					  						
				  case	fireFlash		:	{ 	if (TriggerActions.getPort() == 1) 	{
					  
//					  for (int i=0; i<100; i++) {

						  
						// fire flash on OutPort1
						if (isLinux==true) for (int i=0; i<1000; i++) MainApp.outPort1.high();  ////
						sb.append("start Flash on port 1");
						// if NrFlashes == 1, wait 50 ms and stop Flash  1
						if (MainApp.TProject.getNrFlashes() == 1)
						{				
							try {
								Thread.sleep(0);
							} catch (InterruptedException e) {
								System.out.println("TriggerAction.Trigger: Line 484");
								e.printStackTrace();
							}
							sb.append("\n\t\t\t\t\t\tdelay 0 us    ");
							if (isLinux==true) MainApp.outPort1.low();	
							sb.append("\n\t\t\t\t\t\treset Flash on port 1");	
						}
						
//					  }
						
				  	}
					else if (TriggerActions.getPort() == 2) { 
						if (isLinux==true) MainApp.outPort2.high();
						sb.append("start Flash on port 2");
						
						if (MainApp.TProject.getNrFlashes() == 2)
						{				
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								System.out.println("TriggerAction.Trigger: Line 484");
								e.printStackTrace();
							}
							sb.append("\n\t\t\t\t\t\tdelay 50 us    ");
							if (isLinux==true) {
								MainApp.outPort1.low();	
								MainApp.outPort2.low();	
							}
							sb.append("\n\t\t\t\t\t\treset Flash on port 1");	
							sb.append("\n\t\t\t\t\t\treset Flash on port 2");	
						}
					}
					else if (TriggerActions.getPort() == 3) { 
						// fire flash on OutPort3
						if (isLinux==true) MainApp.outPort3.high();
						sb.append("start Flash on port 3");
						
						if (MainApp.TProject.getNrFlashes() == 3)
						{				
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								System.out.println("TriggerAction.Trigger: Line 484");
								e.printStackTrace();
							}
							sb.append("\n\t\t\t\t\t\tdelay 50 us    ");
							if (isLinux==true) {
								MainApp.outPort1.low();	
								MainApp.outPort2.low();	
								MainApp.outPort3.low();	
							}
							sb.append("\n\t\t\t\t\t\treset Flash on port 1");	
							sb.append("\n\t\t\t\t\t\treset Flash on port 2");	
							sb.append("\n\t\t\t\t\t\treset Flash on port 3");	
						}	
					}
					break;
				 }
  
				  case	openValve		:	// Open Valve on port 'TriggerActions.getPort()'
					  						switch (TriggerActions.getPort()) {
						  						case 5: if (isLinux==true) MainApp.outPort5.high(); 
						  								sb.append("open valve on outPort 5   ");
						  								break;			// open valve on OutPort5
						  						case 6: if (isLinux==true) MainApp.outPort6.high();
						  								sb.append("open valve on outPort 6   ");
						  								break;			// open valve on OutPort6
						  						case 7: if (isLinux==true) MainApp.outPort7.high();
						  								sb.append("open valve on outPort 7   ");break;			// open valve on OutPort6
						  						default: System.out.println("openening a valve on port >= 7 cannot occur!!");
					  						};
					  						break;
				  
				  case	closeValve		:	switch (TriggerActions.getPort()) {
												case 5: if (isLinux==true) MainApp.outPort5.low(); 
														sb.append("close valve on outPort 5   ");
														break;			// close valve on OutPort5
												case 6: if (isLinux==true) MainApp.outPort6.low(); 
														sb.append("close valve on outPort 6   ");
														break;			// close valve on OutPort6
												case 7: if (isLinux==true) MainApp.outPort7.low();
														sb.append("close valve on outPort 7   ");
														break;			// close valve on OutPort6
												default: System.out.println("openening a valve on port >= 7 cannot occur!!");
											};
											break;
				  
				  default:					System.out.println("unknown Trigger action!!"); 
	  
				};					

	            // write out protocol buffer
	    		System.out.println(sb);

            };  // endif (actionTime >= currentTime)
        });	  // forEach(TriggerActions  -->
	    
	    // output the overall time of the trigger sequence
	    globalCal = Calendar.getInstance();
	    System.out.println("\nEnd time(min.sec.msec) :  " + sdf.format(globalCal.getTime()) + "\nTrigger sequence is finished.");
	    System.out.println("------------------------------------------------\n");

	}    	// end Trigger()
}			// end class TriggerAction
	


	







