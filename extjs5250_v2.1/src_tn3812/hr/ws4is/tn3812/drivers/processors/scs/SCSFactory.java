package hr.ws4is.tn3812.drivers.processors.scs;

import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.drivers.processors.ProcessorType;

public enum SCSFactory {
	;
	
	public static IProcessor get(ProcessorType type){
		   IProcessor processor = null;
		   switch (type){
		   	case  SCS :  
		   		processor = new SCSProcessor();
		   		break;
		   	case  SCS_LOGGER :
		   		processor = new SCSLogger();
		   		break;
		   	default:
		   		break;
		   }
		   return processor;
	}
}
