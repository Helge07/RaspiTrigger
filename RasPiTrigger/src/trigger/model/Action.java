package trigger.model;

import trigger.model.TriggerProject.cmd;
import trigger.model.TriggerProject.device;

public class Action {

	private Integer Step 		= 0;
	private device 	Dev  		= device.none;
	private Integer Port 		= 0;
	private Integer Time 		= 0;
	private Integer TimeScale 	= 0;
	private cmd 	Command 	= cmd.none;
	private Integer Parameter 	= 0;
	
	// constructor
	public Action(Integer _step, device _dev, Integer _port, Integer _time, Integer _ts, cmd _action, Integer _param ) { 
		// instantiate the  class variables s
		Step 		= _step;
		Dev 		= _dev;
		Port 		= _port;
		Time 		= _time;
		TimeScale 	= _ts;
		Command 	= _action;
		Parameter 	= _param;
	} 
	
	// parameterless constructor
	public Action( ) { 
		// instantiate the  class variables s
		Step 		= 0;
		Dev 		= device.none;
		Port 		= 0;
		Time 		= 0;
		TimeScale 	= 0;
		Command 	= cmd.none;
		Parameter 	= 0;
	} 
	
	// define get/set methods for the member variables
	// -----------------------------------------------
    public Integer getStep()          		{ return Step;  	}
    public void setStep(Integer step)   	{ Step = step;   	}
    
    public device getDev()          		{ return Dev;       }
    public void setDev(device dev)   		{ Dev = dev;        }
 
    public Integer getPort()          		{ return Port;  	}
    public void setPort(Integer port)   	{ Port = port;   	}
    
    public Integer getTime()          		{ return Time;  	}
    public void setTime(Integer time)   	{ Time = time;   	}
    
    public Integer getTimeScale()          	{ return TimeScale; }
    public void setTimeScale(Integer tmsc)  { TimeScale = tmsc; }

    public cmd getCommand()         		{ return Command; 	 }
    public void setCommand(cmd command)   	{ Command = command; }

    public Integer getParameter()          	{ return Parameter;  }
    public void setParameter(Integer param) { Parameter = param; }
}
  