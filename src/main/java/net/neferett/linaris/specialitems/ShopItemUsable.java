package net.neferett.linaris.specialitems;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ShopItemUsable extends ShopItem {

	public ShopItemUsable(String name, String[] lore, ItemStack itemStack, String shopName, String shopItemId) {
		super(name, lore, itemStack, shopName, shopItemId);
	}
	
	public ShopItemUsable(String name, ItemStack itemStack, String shopName, String shopItemId, String... lore) {
		super(name, lore, itemStack, shopName, shopItemId);
	}

	@Override
	public ItemStack getShopItem(Player player) {
		ItemStack item = this.getClonedItem();
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = getLore();
		int itemLevel = getItemLevel(player);
		lore.add("");
		if (itemLevel == 0) lore.add("§cAchetez le sur le Hub !");
		else {
			lore.addAll(getCurrentLevelLore(itemLevel));
			lore.add("");
			lore.add("§6Niveau actuel: §e" + itemLevel);
		}
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
}
