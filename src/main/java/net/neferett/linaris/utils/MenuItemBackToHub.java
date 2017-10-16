package net.neferett.linaris.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.specialitems.MenuItem;

public class MenuItemBackToHub extends MenuItem {

	public static int	id;

	public MenuItemBackToHub() {
		super("§6Retourner au Hub", new ItemStack(Material.BED), "§7Ou faites §e/hub");
	}

	@Override
	public void inventoryClickEvent(Player player) {
		PlayerUtils.returnToHub(player);
	}

}
