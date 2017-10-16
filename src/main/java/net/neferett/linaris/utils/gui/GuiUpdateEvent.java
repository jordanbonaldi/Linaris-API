package net.neferett.linaris.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class GuiUpdateEvent extends Event  {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private GuiScreen gui;
	//private boolean cancelled;
	
	public GuiUpdateEvent(Player player, GuiScreen gui, boolean who) {
		super(who);
		this.player = player;
		this.gui = gui;
	}

	public GuiScreen getGui() {
		return gui;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public HandlerList getHandlers()
	{
	  return handlers;
	}
	  
	public static HandlerList getHandlerList()
	{
	  return handlers;
	}

/*
	@Override
	public boolean isCancelled() {
		return cancelled;
	}


	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}*/
}
