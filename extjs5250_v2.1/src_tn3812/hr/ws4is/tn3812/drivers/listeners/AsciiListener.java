package hr.ws4is.tn3812.drivers.listeners;

import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.scs.SCSStyleType;
import hr.ws4is.tn3812.interfaces.ITn3812Context;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is part of printing driver. 
 * Listeners methods are called on data parsing processor events.  
 */
final class AsciiListener implements IProcessorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsciiListener.class);

    private ByteBuffer buffer;

    @Override
    public void onFormFeed() {
        LOGGER.info("*** onFormFeed");
    }

    @Override
    public void onLineFeed() {
        LOGGER.info("*** onLineFeed");
    }

    @Override
    public void onHorizontalMove() {
        LOGGER.info("*** onHorizontalMove");
    }

    @Override
    public void onVerticalMove() {
        LOGGER.info("*** onVerticalMove");
    }

    @Override
    public void onNewLine() {
        // LOGGER.info("*** onNewLine");
        final byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        try {
            LOGGER.info(new String(data, "Cp870"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFontStyleChange(final SCSStyleType type) {
        LOGGER.info("*** onFontStyleChange");
    }

    @Override
    public void onStart(final IControls controls) {
        LOGGER.info("*** onStart");
    }

    @Override
    public void onFinish(final ITn3812Context context) {
        LOGGER.info("*** onFinish");
    }

    @Override
    public void onData(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

}
