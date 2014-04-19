package hr.ws4is.tn3812.drivers.listeners;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import hr.ws4is.tn3812.drivers.listeners.IProcessorListener;
import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.SCSStyleType;

final class AsciiListener implements IProcessorListener {

	ByteBuffer buffer;
	
	@Override
	public void onFormFeed(IControls controls) {
		System.out.println("*** onFormFeed");
	}

	@Override
	public void onLineFeed(IControls controls) {
		System.out.println("*** onLineFeed");
	}

	@Override
	public void onHorizontalMove(IControls controls) {
		System.out.println("*** onHorizontalMove");
	}

	@Override
	public void onVerticalMove(IControls controls) {
		System.out.println("*** onVerticalMove");
	}

	@Override
	public void onNewLine(IControls controls) {
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
	public void onFontStyleChange(IControls controls, SCSStyleType type) {
		System.out.println("*** onFontStyleChange");
	}

	@Override
	public void onStart(IControls controls) {
		System.out.println("*** onStart");
	}

	@Override
	public void onFinish(IControls controls) {
		System.out.println("*** onFinish");
	}

	@Override
	public void onData(ByteBuffer buffer, IControls controls) {
		this.buffer = buffer;
	}

}
