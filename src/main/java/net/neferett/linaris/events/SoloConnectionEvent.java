package net.neferett.linaris.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.neferett.linaris.api.server.ConnectionStatus;

public class SoloConnectionEvent extends Event {

	private static final HandlerList	handlers	= new HandlerList();

	private String player;
	private int rank;
	private String[] args;
	
	private ConnectionStatus status;
	private String message;
	private int errorID;
	
	public SoloConnectionEvent(String player, int rank, String[] args) {
		this.player = player;
		this.rank = rank;
		this.args = args;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void allow() {
		status = ConnectionStatus.ALLOW;
	}
	
	public void disallow(int errorID, String message) {
		this.status = ConnectionStatus.DENY;
		this.errorID = errorID;
		this.message = message;
	}
	
	public ConnectionStatus getStatus() {
		return status;
	}
	
	public int getErrorID() {
		return errorID;
	}
	
	public String getMessage() {
		return message;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
