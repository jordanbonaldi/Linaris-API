package net.neferett.linaris.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SpecialItemConsumable {
	public void consumeItemEvent(Player player,ItemStack item);
}
