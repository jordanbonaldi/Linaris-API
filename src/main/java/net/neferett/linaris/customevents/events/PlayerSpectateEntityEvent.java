package net.neferett.linaris.customevents.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Les joueur rentre dans une entité avec le mode spectateur
 * @author Likaos
 */

public class PlayerSpectateEntityEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList	handlers	= new HandlerList();
	private boolean						canceled	= false;
	private Entity						entity;
	private int							id;

	public PlayerSpectateEntityEvent(Player player, int entityId) {
		super(player);
		this.id = entityId;
		// Rechercher l'entité, doit être living en théorie
		for (Entity e : player.getWorld().getEntities()) {
			if (e.getEntityId() == entityId) {
				setEntity(e);
				break;
			}
		}
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.canceled = cancel;
	}

	/**
	 * L'entité que le joueur va spectate
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * L'entité que le joueur va spectate
	 * @return
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Le joueur sort du mode specateur
	 * @return
	 */
	public boolean endWithSpectate() {
		return this.id == player.getEntityId();
	}
}
