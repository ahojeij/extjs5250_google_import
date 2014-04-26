/*
 * Copyright (C) 2014,  Tomislav Milkovic
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */
package hr.ws4is.tn3812;

import hr.ws4is.tn3812.enums.Tn3812Flag;
import hr.ws4is.tn3812.enums.Tn3812ResponseTypeData;
import hr.ws4is.tn3812.enums.Tn3812ResponseCodes;
import hr.ws4is.tn3812.enums.Tn3812ResponseType;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main printer response data processing handler. Used after successful negotiation.
 * This handler is used to process all receiving spools by calling registered listeners (drivers) 
 */
class IncomingDataProcessing extends BasicCompletionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(IncomingDataProcessing.class);

	boolean negotiate = true;
	
	public IncomingDataProcessing(Tn3812Context ctx) {
		super(ctx);
	}	

	public void failed(Throwable exc, Void attachment) {
	    exc.printStackTrace();
	}

	
	public void completed(Integer result, Void attachment) {	
		
		if(handleClosed(result, attachment)) {
			return;
		}
		
		boolean status = false;
		try{
			ByteBuffer buffer = ctx.getInBuffer(); 
			buffer.flip();
			status = process(buffer);
			buffer.clear();	
		} finally{			
			if(status  == false){
				IncomingDataNegotiation handler = ctx.getIncomingDataHandler();
				handler.reset();
				BasicCompletionHandler.startReading(ctx, handler);
			}else{
				BasicCompletionHandler.startReading(ctx, this);	
			}
				
		}
		
	}
	
//  --------------- PRIVATE SECTION --------------- 

	private void sendComplete(){
		ByteBuffer buffer = ctx.getInBuffer(); 
		try {
			buffer.clear();
			buffer.put(Tn3812Constants.PRINT_COMPLETE);
			buffer.put(Tn3812Constants.IAC);
			buffer.put(Tn3812Constants.EOR);
			buffer.flip();
			BasicCompletionHandler.startWriting(ctx);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private boolean process(ByteBuffer buffer){
		short size = buffer.getShort();
		//System.out.println("Size : " + size);

		short gds = buffer.getShort();
		if(!checkShort(gds, Tn3812Constants.GDS)) {
			//System.out.println("GDS OK");
			logger.error("Incomming stream is not recognized!");
			return false;
		}

		boolean status = false;
		Tn3812ResponseType responseType = getResponseType(buffer);
		switch(responseType){
		case STARTUP : 
			status = processStartupResponse(buffer, size);
			break;
		case SERVER_FLOW :
			status = processDataResponse(buffer, size);
			sendComplete();
			break;
		default : 
			logger.error("Invalid stream received!");
		}		
		return status;
	}
	
	@SuppressWarnings("unused")
	private boolean processDataResponse(ByteBuffer buffer, short size){
		byte  headerLength = buffer.get();
		byte flags = buffer.get();
		boolean firstChain = isFlag(flags, Tn3812Flag.FIRST_OF_CHAIN);
		boolean lastChain =  isFlag(flags, Tn3812Flag.LAST_OF_CHAIN);
		boolean header = firstChain && lastChain;
		
		byte reserved = buffer.get();
		byte opcode = buffer.get();
		
		//if not print / print complete
		if(opcode != 1 ){
			// 02 - clear buffers
			buffer.clear();
			return true;
		} 		 
		
		ByteBuffer diagnostic = getBytes(buffer, headerLength-4);
		/*
		Tn3812PrinterCommands prtCmd = getPrinterCommand(diagnostic);
		if(prtCmd != null){
			//todo , process printer command
		}
		*/
		
		buffer.mark(); 
		ByteBuffer data = getBytes(buffer, buffer.limit() - buffer.position()-2);

		try {			
			if(header){
				ctx.fireData(Tn3812ResponseTypeData.HEAD, data);	
			}else if(firstChain){
				ctx.fireData(Tn3812ResponseTypeData.FIRST, data);
			}else if(lastChain){
				ctx.fireData(Tn3812ResponseTypeData.LAST, data);
			} else {
				ctx.fireData(Tn3812ResponseTypeData.CHAIN, data);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.clear();
		}
		return true;
	
	}
	
	
	private boolean isFlag(byte value, Tn3812Flag flag){
		return (value & flag.getValue()) == flag.getValue();
	}
	
	private boolean processStartupResponse(ByteBuffer buffer, short size){
		
		Tn3812ResponseCodes code = getResponseCode(buffer);
		switch(code){
		case I901 : 
		case I902 : 
		case I906 :
			System.out.println(code.getName());
			break;
		default : 			
			ctx.fireData(Tn3812ResponseTypeData.ERROR, ByteBuffer.wrap(code.getName().getBytes()));			
			return false;
		}
		
		ByteBuffer hostName = getBytes(buffer, 8);
		ByteBuffer printerName = getBytes(buffer, 10);

		try {
			String host = new String( hostName.array(),"IBM870");
			String printer = new String( printerName.array(),"IBM870");			
			logger.info("Started session {} for host {} !", host.toUpperCase(), printer.toUpperCase());			
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} 
		
		try {
			buffer.flip();
			ctx.fireData(Tn3812ResponseTypeData.INIT, buffer);
		} finally {
			buffer.clear();
		}
		return true;
		
	}
	
	private Tn3812ResponseType getResponseType(ByteBuffer buffer){
		Tn3812ResponseType responseType = Tn3812ResponseType.UNKNOWN;  
		buffer.mark();
		short dflow = buffer.getShort();
		if(checkShort(dflow, Tn3812Constants.DATA_FLOW_SERVER)) {
			//logger.debug("DATA FROM SERVER");
			responseType = Tn3812ResponseType.SERVER_FLOW;
		} else if ( checkShort(dflow, Tn3812Constants.DATA_FLOW_CLIENT) ){
			//logger.debug("DATA FROM CLIENT");
			responseType = Tn3812ResponseType.CLIENT_FLOW;
		} else {
			buffer.reset();
			if (isStartupResponse(buffer)){
				//logger.debug("STARTUP RESPONSE");
				responseType = Tn3812ResponseType.STARTUP;
			}
		}
		return responseType;
	}

	/*
	private Tn3812PrinterCommands getPrinterCommand(ByteBuffer buffer){
		Tn3812PrinterCommands code = null;
		buffer.mark();
		ByteBuffer valueBuffer = getBytes(buffer, 4);
		
		for (Tn3812PrinterCommands value : Tn3812PrinterCommands.values()){
			ByteBuffer constBuffer = getFromBytes(value.getValue());
			int result = valueBuffer.compareTo(constBuffer);	
			if(result == 0){
				code = value;
				break;
			}
		}
		return code;
	}
	*/
	
	private Tn3812ResponseCodes getResponseCode(ByteBuffer buffer){
		Tn3812ResponseCodes code = null;
		buffer.mark();
		ByteBuffer valueBuffer = getBytes(buffer, 4);
		
		for (Tn3812ResponseCodes value : Tn3812ResponseCodes.values()){
			ByteBuffer constBuffer = getFromBytes(value.getValue());
			int result = valueBuffer.compareTo(constBuffer);	
			if(result == 0){
				code = value;
				break;
			}
		}
		return code;
	}
	
	private boolean isStartupResponse(ByteBuffer buffer){
		buffer.mark();
		ByteBuffer constBuffer = getFromBytes(Tn3812Constants.FIXED);
		ByteBuffer valueBuffer = getBytes(buffer, Tn3812Constants.FIXED.length);
		int result = valueBuffer.compareTo(constBuffer);		
		return result == 0 ;
	}
	
	private ByteBuffer getBytes(ByteBuffer buffer, int size){
		ByteBuffer valueBuffer = ByteBuffer.allocate(size);
	    while (valueBuffer.hasRemaining()){
	    	valueBuffer.put(buffer.get());	     
	    }
		valueBuffer.flip();		
		return valueBuffer;
	}
	
	private boolean checkShort(short dflow, byte[] data){
		ByteBuffer valueBuffer = getFromBytes(data);
		return valueBuffer.getShort() == dflow;
	}
	
	private ByteBuffer getFromBytes(byte[] data){
		ByteBuffer valueBuffer = ByteBuffer.allocate(data.length);
		valueBuffer.put(data);
		valueBuffer.flip();
		return valueBuffer;
	}
	
}