package net.neferett.linaris.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MenuItem extends SpecialItem {

	/**
	 * @deprecated utiliser l'autre constructeur
	 */
	public MenuItem(String name, String[] lore, ItemStack itemStack) {
		super(name, lore, itemStack);
		setRightClickable(true);
		setInventoryClickable(true);
		setDroppable(false);
		setMovable(false);
	}

	public MenuItem(String name, ItemStack itemStack, String... lore) {
		super(name, lore, itemStack);
		setRightClickable(true);
		setInventoryClickable(true);
		setDroppable(false);
		setMovable(false);
	}

	@Override
	public void rightClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void leftClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void middleClickEvent(Player player) {
		inventoryClickEvent(player);
	}

}
