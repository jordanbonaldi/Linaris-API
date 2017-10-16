package net.neferett.linaris.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.neferett.linaris.metadatas.Flags;

class SpecialItemProjectileLauncheurListener implements Listener {
	/**
	 * Declenche SpecialItemBow.arrowDamageEntity
	 * Si la fleche est lancée par un SpecialItemBow
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Projectile))return;
		Projectile projectile = (Projectile) event.getDamager();
		if(!projectile.isValid())return;
		if(!Flags.hasFlag(projectile, SpecialItemProjectileLauncheur.flag))return;
		SpecialItem special = SpecialItem.get(Flags.readIntegerFlag(projectile, SpecialItemProjectileLauncheur.flag));
		if(special == null || !(special instanceof SpecialItemProjectileLauncheur))return;
		Player player = (projectile.getShooter() instanceof Player)?(Player)projectile.getShooter():null;
		((SpecialItemProjectileLauncheur)special).projectileDamageEntity(player, projectile, event);
	}
	/**
	 * Declenche SpecialItemBow.arrowHitBlock
	 * Si la fleche est lancée par un SpecialItemBow
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onProjectileHitBlock(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Player player = projectile.getShooter() instanceof Player ? ((Player)projectile.getShooter()) : null;
		if(!Flags.hasFlag(projectile, SpecialItemProjectileLauncheur.flag))return;
		SpecialItem special = SpecialItem.get(Flags.readIntegerFlag(projectile, SpecialItemProjectileLauncheur.flag));
		if(special == null || !(special instanceof SpecialItemProjectileLauncheur))return;
		((SpecialItemProjectileLauncheur)special).projectileHitBlock(player, event);
	}
}
