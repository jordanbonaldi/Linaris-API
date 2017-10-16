package net.neferett.linaris.specialitems;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import net.neferett.linaris.customevents.events.ArrowHitBlockEvent;

/**
 * Doit etendre Special item
 * Permet de récupérer les interactions des flèches tirées par un specialItemBow
 * @author Nicolas
 *
 */
public interface SpecialItemBow {
	/**
	 * Flag posé sur les flèches tirées
	 */
	public static String flag = "SpecialBowFlag";
	/**
	 * player : the player throwing the arrow. (null if it's not a player)
	 * @param player
	 * @param event
	 */
	void arrowHitBlock(Player player, ArrowHitBlockEvent event);
	/**
	 * player : the player throwing the arrow. (null if it's not a player)
	 * arrow : the arrow involved
	 * @param player
	 * @param arrow
	 * @param event
	 */
	void arrowDamageEntity(Player player, Arrow arrow, EntityDamageByEntityEvent event);
	/** 
	 * Called when the arrow is launched (arrow should already have the SpecialBowFlag)
	 * player is the player throwing the arrow. (null if it's not a player)
	 * @param player
	 * @param event
	 */
	void arrowTrowned(Player player, EntityShootBowEvent event);
}
