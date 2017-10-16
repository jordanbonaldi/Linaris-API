package net.neferett.linaris.specialitems;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.customevents.events.ArrowHitBlockEvent;
import net.neferett.linaris.metadatas.Flags;

class SpecialItemBowListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		Arrow arrow = (Arrow) event.getProjectile();
		Player player = event.getEntity() instanceof Player ? (Player)event.getEntity() :null;
		ItemStack item = event.getBow();
		if(item == null) return;
		SpecialItem special = SpecialItem.getSpecialItem(item);
		if(!(special instanceof SpecialItemBow)) return;
		Flags.setIntegerFlag(arrow, SpecialItemBow.flag, special.getId());
		((SpecialItemBow)special).arrowTrowned(player, event);
	}
	/**
	 * Declenche SpecialItemBow.arrowDamageEntity
	 * Si la fleche est lancée par un SpecialItemBow
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Arrow))return;
		Arrow arrow = (Arrow) event.getDamager();
		if(!arrow.isValid())return;
		if(!Flags.hasFlag(arrow, SpecialItemBow.flag))return;
		SpecialItem special = SpecialItem.get(Flags.readIntegerFlag(arrow, SpecialItemBow.flag));
		if(special == null || !(special instanceof SpecialItemBow))return;
		Player player = (arrow.getShooter() instanceof Player)?(Player)arrow.getShooter():null;
		((SpecialItemBow)special).arrowDamageEntity(player, arrow, event);
	}
	/**
	 * Declenche SpecialItemBow.arrowHitBlock
	 * Si la fleche est lancée par un SpecialItemBow
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onArrowHitBlock(ArrowHitBlockEvent event) {
		Arrow arrow = event.getArrow();
		Player player = arrow.getShooter() instanceof Player ? ((Player)arrow.getShooter()) : null;
		if(!arrow.isValid())return;
		if(!Flags.hasFlag(arrow, SpecialItemBow.flag))return;
		SpecialItem special = SpecialItem.get(Flags.readIntegerFlag(arrow, SpecialItemBow.flag));
		if(special == null || !(special instanceof SpecialItemBow))return;
		((SpecialItemBow)special).arrowHitBlock(player, event);
	}
}
