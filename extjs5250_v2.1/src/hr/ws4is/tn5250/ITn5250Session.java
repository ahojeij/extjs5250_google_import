package hr.ws4is.tn5250;

import hr.ws4is.data.tn5250.Tn5250ScreenElement;
import hr.ws4is.data.tn5250.Tn5250ScreenRequest;
import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.websocket.WebSocketSession;

public interface ITn5250Session {

    /* process key request */
    void process(Tn5250ScreenRequest request, Tn5250ScreenElement[] fields);

    /* resends last screen */
    Tn5250ScreenResponse refresh();

    String getDisplayId();

    void setDisplayId(String displayId);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getHostName();

    void setHostName(String hostName);

    void disconnect();

    boolean isConnected();

    String toString();

    void updateWebSocketSession(WebSocketSession wsSession);

}