package tn3812;

import java.io.File;
import java.nio.ByteBuffer;

import javax.enterprise.inject.Vetoed;

import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;
import hr.ws4is.websocket.WebSocketSession;

@Vetoed
public class MyListener implements ITn3812DataListener {

	ITn3812Context config;

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
		System.out.println("printed");
		File f = (File) config.getData(File.class.getCanonicalName());
		if(f==null){
			//TODO log error
			return;
		}
		//TODO report to websocket
		System.out.println(f);
	}
	
	@Override
	public void onClosed() {
		
	}	

}
