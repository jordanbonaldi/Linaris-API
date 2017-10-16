package net.neferett.linaris.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.PlayersHandler.Players;

public class PlayerDamageEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private final Entity					damager;
	private final EntityDamageByEntityEvent	e;

	private final Player					entity;

	public PlayerDamageEvent(final EntityDamageByEntityEvent e) {
		this.e = e;
		this.damager = e.getDamager();
		this.entity = (Player) e.getEntity();
	}

	public boolean DamagerIsPlayer() {
		return this.damager instanceof Player;
	}

	public Entity getDamager() {
		return this.damager;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Players getPlayer() {
		return PlayerManager.get().getPlayer(this.entity);
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(final boolean arg0) {
		this.e.setCancelled(arg0);
	}

}
