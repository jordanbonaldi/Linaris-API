package net.neferett.linaris.api.server;

import com.google.gson.Gson;

public class ConnectionServerRespond {

	String status;
	String message;
	int errorID;
	
	public ConnectionServerRespond(ConnectionStatus status, String message, int errorID) {
		this.status = status.name();
		this.message = message;
		this.errorID = errorID;
	}
	
	public ConnectionStatus getStatus() {
		return ConnectionStatus.valueOf(status);
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getErrorID() {
		return errorID;
	}
	
	public String getJSON() {
		return new Gson().toJson(this);
	}
	
}
