package net.neferett.linaris.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.neferett.linaris.api.server.ConnectionStatus;

public class MultiConnectionEvent extends Event {

	private static final HandlerList	handlers	= new HandlerList();

	private UUID party;
	private String[] players;
	private int[] ranks;
	private String[] args;
	
	private ConnectionStatus status;
	private String message;
	private int errorID;
	
	public MultiConnectionEvent(UUID uuid, String[] players, int[] ranks, String[] args) {
		this.party = uuid;
		this.players = players;
		this.ranks = ranks;
		this.args = args;
	}
	
	public String[] getArgs() {
		return args;	
	}
	
	public UUID getParty() {
		return party;
	}
	
	public String[] getPlayers() {
		return players;
	}
	
	public int[] getRanks() {
		return ranks;
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
