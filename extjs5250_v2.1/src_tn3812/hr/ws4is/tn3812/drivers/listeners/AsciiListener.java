package hr.ws4is.tn3812.drivers.listeners;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import hr.ws4is.tn3812.drivers.listeners.IProcessorListener;
import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.scs.SCSStyleType;
import hr.ws4is.tn3812.interfaces.ITn3812Context;

final class AsciiListener implements IProcessorListener {

	ByteBuffer buffer;
	
	@Override
	public void onFormFeed() {
		System.out.println("*** onFormFeed");
	}

	@Override
	public void onLineFeed() {
		System.out.println("*** onLineFeed");
	}

	@Override
	public void onHorizontalMove() {
		System.out.println("*** onHorizontalMove");
	}

	@Override
	public void onVerticalMove() {
		System.out.println("*** onVerticalMove");
	}

	@Override
	public void onNewLine() {
		//System.out.println("*** onNewLine");
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		try {
			System.out.println(new String(data,"Cp870"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onFontStyleChange(SCSStyleType type) {
		System.out.println("*** onFontStyleChange");
	}

	@Override
	public void onStart(IControls controls) {
		System.out.println("*** onStart");
	}

	@Override
	public void onFinish(ITn3812Context context) {
		System.out.println("*** onFinish");
	}

	@Override
	public void onData(ByteBuffer buffer) {
		this.buffer = buffer;
	}

}
