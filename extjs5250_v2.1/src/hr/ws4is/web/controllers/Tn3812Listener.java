package hr.ws4is.web.controllers;

import java.io.File;
import java.nio.ByteBuffer;

import javax.enterprise.inject.Vetoed;

import hr.ws4is.data.tn3812.Tn3812Response;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;
import hr.ws4is.websocket.WebSocketSession;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketResponse;

@Vetoed
public class Tn3812Listener implements ITn3812DataListener {

	ITn3812Context config;
	WebSocketSession wsSession;
	
	public Tn3812Listener(WebSocketSession wsSession) {
		super();
		this.wsSession = wsSession;
	}

	@Override
	public void onInit(ITn3812Context config) {
		this.config = config;
	}

	@Override
	public void onHeader(ByteBuffer data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFirstChain(ByteBuffer data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onChain(ByteBuffer data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLastChain(ByteBuffer data) {
		File file = (File) config.getData(File.class.getCanonicalName());
		if(file==null){
			//TODO log error
			return;
		}
		WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
		Tn3812Response resp = new Tn3812Response(true, null);
		resp.setPrinterName(config.getconfiguration().getDevName());
		resp.setReportName(file.getName());
		wsResponse.setData(resp);		
		wsSession.sendResponse(wsResponse, true);		
	}
	
	@Override
	public void onClosed() {
		
	}	

}
