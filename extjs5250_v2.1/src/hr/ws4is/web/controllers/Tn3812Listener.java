package hr.ws4is.web.controllers;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import hr.ws4is.data.tn3812.Tn3812Response;
import hr.ws4is.tn3812.interfaces.ITn3812Config;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;
import hr.ws4is.web.TnWebHelper;
import hr.ws4is.websocket.WebSocketSession;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketResponse;

@Vetoed
public class Tn3812Listener implements ITn3812DataListener {

    private ITn3812Context context;
    private WebSocketSession wsSession;

    public Tn3812Listener(final WebSocketSession wsSession) {
        super();
        this.wsSession = wsSession;
    }

    @Override
    public final void onInit(final ITn3812Context context) {
        this.context = context;
        // store to web session
        final Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(wsSession);
        final Tn3812Response resp = getResponse(true, null, "connected");
        final ITn3812Config config = context.getconfiguration();
        sessions.put(config.getDevName(), context);
        sendResponse(resp);
    }

    @Override
    public void onHeader(final ByteBuffer data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFirstChain(final ByteBuffer data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onChain(final ByteBuffer data) {
        // TODO Auto-generated method stub
    }

    @Override
    public final void onLastChain(final ByteBuffer data) {
        File file = (File) context.getData(File.class.getCanonicalName());
        if (file == null) {
            // TODO log error
            return;
        }

        final Tn3812Response resp = getResponse(true, null, "report");
        resp.setReportName(file.getName());
        sendResponse(resp);
    }

    @Override
    public final void onClosed() {
        final Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(wsSession);
        final Tn3812Response resp = getResponse(true, null, "closed");
        final ITn3812Config config = context.getconfiguration();
        sessions.remove(config.getDevName());
        sendResponse(resp);
    }

    @Override
    public final void onError(final ITn3812Context config, final ByteBuffer data) {
        final Tn3812Response resp = getResponse(false, new String(data.array()), "error");
        sendResponse(resp);
    }

    private Tn3812Response getResponse(final boolean status, final String msg, final String type) {
        final Tn3812Response resp = new Tn3812Response(status, msg);
        final ITn3812Config config = context.getconfiguration();
        resp.setPrinterName(config.getDevName());
        resp.setTnType(type);
        return resp;
    }

    private void sendResponse(final Tn3812Response resp) {
        final WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
        wsResponse.setData(resp);
        wsSession.sendResponse(wsResponse, true);
    }

    @Override
    public final void onRemoved() {

    }
}
