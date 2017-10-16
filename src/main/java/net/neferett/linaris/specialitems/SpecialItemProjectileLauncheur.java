package net.neferett.linaris.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;


/**
 * Doit etendre Special item
 * Permet de récupérer les interactions des projectiles tirées par un SpecialItemProjectileLauncheur
 * Pour que le processus fonctionne, il faut poser le flag de l'item associé à l'id du Special Item
 * 
 * use ProjectilesUtils.launchProjectile(id, player, spawn, class);
 *  
 * @author Nicolas
 *
 */
public interface SpecialItemProjectileLauncheur {
	/**
	 * Flag posé sur les flèches tirées
	 */
	public static String flag = "SpecialItemProjectileLauncheurFlag";
	/**
	 * player : the player throwing the arrow. (null if it's not a player)
	 * @param player
	 * @param event
	 */
	void projectileHitBlock(Player player, ProjectileHitEvent event);
	/**
	 * player : the player throwing the arrow. (null if it's not a player)
	 * arrow : the arrow involved
	 * @param player
	 * @param arrow
	 * @param event
	 */
	void projectileDamageEntity(Player player, Projectile projectile, EntityDamageByEntityEvent event);
}
