package net.neferett.linaris.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.inventory.VirtualInventory;

class SpecialItemInventoryClickListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		InventoryHolder holder = event.getInventory().getHolder();
		if (holder instanceof VirtualInventory || holder instanceof Player) {
			// Virtual on ne touche pas les objets
			if (holder instanceof VirtualInventory) event.setCancelled(true);
			ItemStack itemStack = event.getCurrentItem();
			SpecialItem item = SpecialItem.getSpecialItem(itemStack);
			if (item != null && item.isInventoryClickable()) {
				event.setCancelled(true);
				ClickType click = event.getClick();
				if (click.equals(ClickType.RIGHT)) item.rightClickEvent(event);
				else if (click.equals(ClickType.LEFT)) item.leftClickEvent(event);
				else if (click.equals(ClickType.MIDDLE)) item.middleClickEvent(event);
				else item.inventoryClickEvent(event);
			}
			else return;
		}
		/*
		 * FIX Temporaire des specials items not movables
		 */
		if (event.isCancelled()) return;
		ItemStack itemStack = event.getCurrentItem();
		SpecialItem item = SpecialItem.getSpecialItem(itemStack);
		if (item != null && !item.isMovable()) event.setCancelled(true);
	}
}
