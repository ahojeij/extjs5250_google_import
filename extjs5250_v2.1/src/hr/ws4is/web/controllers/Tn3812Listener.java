package hr.ws4is.web.controllers;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import hr.ws4is.data.tn3812.Tn3812Response;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;
import hr.ws4is.web.TnWebHelper;
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
		//store to web session
		final Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(wsSession); 
		sessions.put(config.getconfiguration().getDevName(),config);
		Tn3812Response resp = getResponse(true,null,"connected");
		sendResponse(resp);
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
		
		Tn3812Response resp = getResponse(true, null,"report");
		resp.setReportName(file.getName());
		sendResponse(resp);		
	}
	
	@Override
	public void onClosed() {
		final Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(wsSession);
		sessions.remove(config.getconfiguration().getDevName());
		
		Tn3812Response resp = getResponse(true, null,"closed");
		sendResponse(resp);		
	}

	@Override
	public void onError(ITn3812Context config, ByteBuffer data) {		
		Tn3812Response resp = getResponse(false, new String(data.array()),"error");
		sendResponse(resp);
	}	

	private Tn3812Response getResponse(boolean status, String msg, String type){
		Tn3812Response resp = new Tn3812Response(status, msg);
		resp.setPrinterName(config.getconfiguration().getDevName());
		resp.setTnType(type);
		return resp;
	}
	
	private void sendResponse(Tn3812Response resp){
		WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
		wsResponse.setData(resp);		
		wsSession.sendResponse(wsResponse, true);	
	}

	@Override
	public void onRemoved() {
		config = null;
		wsSession = null;
	}
}
