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
package hr.ws4is.tn5250;

import javax.enterprise.inject.Vetoed;

import hr.ws4is.data.TnConstants;
import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.websocket.WebSocketSession;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.event.ScreenListener;
import org.tn5250j.event.ScreenOIAListener;
import org.tn5250j.event.SessionChangeEvent;
import org.tn5250j.event.SessionListener;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenOIA;

/**
 * Session5250 screen change listener. Whenever screen changes, new websocket
 * event is sent to the browser for screen refresh
 */
@Vetoed
class Tn5250SessionListener implements SessionListener, ScreenListener, ScreenOIAListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tn5250SessionListener.class);

    /* used for data sending to websocket */
    private final String displayID;
    private WebSocketSession wsSession;

    /* internal properties */
    private Session5250 session;
    private Screen5250 screen;
    private ScreenOIA oia;

    private int screenSize;
    private int OIAlevel;
    private int OIAchange;
    private boolean connected;

    public Tn5250SessionListener(final WebSocketSession wsSession, final String displayId) {
        this.wsSession = wsSession;
        this.displayID = displayId;
        this.screenSize = 80;
    }

    public void onSessionChanged(final SessionChangeEvent arg0) {
        // connect/disconnect

        boolean process = wsSession != null && wsSession.isOpen() && !connected;
        process = process && arg0.getState() == TN5250jConstants.STATE_CONNECTED;

        // System.out.println("SESSION_CHANGED :" + arg0.getState());
        if (process) {
            connected = true;
            session = (Session5250) arg0.getSource();
            screen = session.getScreen();
            screen.addScreenListener(this);
            oia = screen.getOIA();
            oia.addOIAListener(this);
            sendData();
        }
    }

    public void onScreenSizeChanged(final int rows, final int cols) {
        // System.out.println("SCREEN_SIZE_CHANGED" + rows + "/" + cols);
        this.screenSize = cols;
    }

    public void onScreenChanged(final int inUpdate, final int startRow, final int startCol, final int endRow, final int endCol) {
        // System.out.println("SCREEN_CHANGED " + inUpdate);

        // screen changed
        if (inUpdate == 1) {
            sendData();
        }

        // cursor changed
        if (inUpdate == 3 && OIAlevel == 7 && OIAchange == 2) {
            OIAchange = 0;
            OIAlevel = 0;
        }
    }

    /*
     * private String[] desc =
     * {"0","KEYS_BUFFERED","KEYBOARD_LOCKED","MESSAGELIGHT"
     * ,"SCRIPT","BELL","CLEAR_SCREEN","INPUTINHIBITED","CURSOR"} ; private
     * String[] lev =
     * {"0","INPUT_INHIBITED","NOT_INHIBITED","MESSAGE_LIGHT_ON","MESSAGE_LIGHT_OFF"
     * ,"AUDIBLE_BELL","INSERT_MODE","KEYBOARD","CLEAR_SCREEN","SCREEN_SIZE",
     * "INPUT_ERROR","KEYS_BUFFERED","SCRIPT"} ;
     * 
     * System.out.println("OIA_CHANGED " + desc[arg1] + " ->> " + arg1);
     * System.out.println("   INHIBITIED " + arg0.getInputInhibited());
     * System.out.println("   COMMCHECK  " + arg0.getCommCheckCode());
     * System.out.println("   LEVEL      " + lev[OIAlevel] + " ->> " + OIAlevel
     * ); System.out.println("   MACHCHECK  " + arg0.getMachineCheckCode());
     * System.out.println("   PROGCHECK  " + arg0.getProgCheckCode());
     * System.out.println("   TEXT       " + arg0.getInhibitedText());
     * System.out.println("   KBDLOK     " + arg0.isKeyBoardLocked());
     * System.out.println("   MSGW       " + arg0.isMessageWait());
     */
    public void onOIAChanged(final ScreenOIA arg0, final int arg1) {
        // status linija - lock/wait/error/msgw ....
        OIAchange = arg1;
        OIAlevel = arg0.getLevel();

        if (arg0.getInputInhibited() == 0 && (OIAlevel == 3 || OIAlevel == 4 || OIAlevel == 7)) {
            sendData();
        }
    }

    private Tn5250ScreenResponse getResponseScreen() {
        final Tn5250ScreenResponse response = new Tn5250ScreenResponse(true, null);
        final boolean locked = oia != null ? oia.isKeyBoardLocked() : false;
        final boolean msgw = oia != null ? oia.isMessageWait() : false;
        response.setSize(screenSize);
        response.setLocked(locked);
        response.setMsgw(msgw);
        response.setDisplayID(displayID);
        if (!session.isConnected()) {
            response.setMsg(TnConstants.NOT_CONNECTED);
            response.setClearScr(true);
            response.setLocked(true);
        }

        Tn5250StreamProcessor.refresh(session, response);

        return response;
    }

    /*
     * inUpdate = 1 , indikator za crtanje ekrana screen element set statuse oia
     * radi lockanja ekrana (screen.getOIA().getInputInhibited() ==
     * ScreenOIA.INPUTINHIBITED_SYSTEM_WAIT && screen.getOIA().getLevel() !=
     * ScreenOIA.OIA_LEVEL_INPUT_ERROR)
     */
    private void sendData() {

        try {
            if (wsSession != null && wsSession.isOpen()) {
                final Tn5250ScreenResponse responseScreen = getResponseScreen();
                final WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
                wsResponse.setData(responseScreen);
                wsSession.sendResponse(wsResponse, true);
            } else {
                unregister();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void unregister() {
        oia.removeOIAListener(this);
        screen.removeScreenListener(this);
        session.removeSessionListener(this);
    }

}
