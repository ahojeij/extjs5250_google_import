package hr.ws4is.tn5250;

import hr.ws4is.data.tn5250.Tn5250ScreenElement;
import hr.ws4is.data.tn5250.Tn5250ScreenRequest;
import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.websocket.WebSocketSession;

public interface ITn5250Session {

	/*process key request*/
	public abstract void process(Tn5250ScreenRequest request,
			Tn5250ScreenElement[] fields);

	/*resends last screen*/
	public abstract Tn5250ScreenResponse refresh();

	public abstract String getDisplayId();

	public abstract void setDisplayId(String displayId);

	public abstract String getDisplayName();

	public abstract void setDisplayName(String displayName);

	public abstract String getHostName();

	public abstract void setHostName(String hostName);

	public abstract void disconnect();

	public abstract boolean isConnected();

	public abstract String toString();

	public abstract void updateWebSocketSession(WebSocketSession wsSession);

}