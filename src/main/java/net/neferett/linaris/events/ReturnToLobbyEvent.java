package net.neferett.linaris.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReturnToLobbyEvent extends Event {

	private static final HandlerList	handlers	= new HandlerList();

	private Player target;
	
	public ReturnToLobbyEvent(Player player) {
		this.target = player;
	}
	
	public Player getTarget() {
		return target;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
