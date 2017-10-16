package net.neferett.linaris.customevents.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Les joueurs qui vont arriver sur le serveur via le join
 * @author Likaos
 */

public class IncomingPlayersEvent extends Event {

	private static final HandlerList	handlers	= new HandlerList();

	private List<String>				players		= new ArrayList<String>();

	public IncomingPlayersEvent(List<String> players) {
		this.players = players;
	}

	public List<String> getPlayers() {
		return players;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
