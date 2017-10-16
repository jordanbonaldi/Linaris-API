package net.neferett.linaris.logo.gui.logo;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.inventory.InstantShopItem;
import net.neferett.linaris.utils.ItemBuilder;
import net.neferett.linaris.utils.NBTItem;
import net.neferett.linaris.utils.gui.GuiScreen;

public class LogoShop extends GuiScreen {

	private final HashMap<ItemStack, InstantShopItem>	itemstacks	= new HashMap<>();
	private final String								name;
	private final PlayerData							pd;

	public LogoShop(final PlayerData pd, final String name) {
		super("Logo Shop", 5, Bukkit.getPlayer(pd.getPlayername()), false);
		this.build();
		this.pd = pd;
		this.name = name;
	}

	@Override
	public void drawScreen() {

		this.setItem(new LogoItems("l0", "✈", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 1);
		this.setItem(new LogoItems("l1", "✁", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 2);
		this.setItem(new LogoItems("l2", "✂", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 3);
		this.setItem(new LogoItems("l3", "✆", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 4);
		this.setItem(new LogoItems("l4", "✇", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 5);
		this.setItem(new LogoItems("l5", "✎", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 6);
		this.setItem(new LogoItems("l6", "✔", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 7);
		this.setItem(new LogoItems("l7", "✖", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 8);
		this.setItem(new LogoItems("l8", "✉", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 1, 9);
		this.setItem(new LogoItems("l9", "✤", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 1);
		this.setItem(new LogoItems("l10", "✠", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 2);
		this.setItem(new LogoItems("l11", "☶", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 3);
		this.setItem(new LogoItems("l12", "☵", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 4);
		this.setItem(new LogoItems("l13", "☯", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 5);
		this.setItem(new LogoItems("l14", "☬", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 6);
		this.setItem(new LogoItems("l15", "☢", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 7);
		this.setItem(new LogoItems("l16", "☠", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 8);
		this.setItem(new LogoItems("l17", "±", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 2, 9);
		this.setItem(new LogoItems("l18", "Ω", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 1);
		this.setItem(new LogoItems("l18", "Φ", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 2);
		this.setItem(new LogoItems("l18", "Θ", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 3);
		this.setItem(new LogoItems("l18", "♚", 80, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 4);
		this.setItem(new LogoItems("l18", "➔", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 5);
		this.setItem(new LogoItems("l18", "❤", 120, this.pd, new ItemBuilder(Material.ANVIL), this.name), 3, 6);

		this.setItemLine(new ItemBuilder(Material.ARROW).setTitle("§fRevenir en arrière").build(), 5, 9);
	}

	@Override
	public void onClick(final ItemStack item, final InventoryClickEvent event) {
		event.setCancelled(true);

		if (item.getType() == Material.ARROW) {
			this.getPlayer().closeInventory();
			return;
		}

		if ((event.isRightClick() || event.isLeftClick()) && this.itemstacks.containsKey(item)) {
			final InstantShopItem myitem = this.itemstacks.get(item);
			if (myitem == null) return;
			this.close();
			myitem.useOrBuy(this.pd, this);
		}
	}

	@Override
	public void onClose() {}

	@Override
	public void onOpen() {}

	public void setItem(final InstantShopItem item, final int line, final int slot) {
		final ItemStack i = item.getItemUI(this.getPlayer());
		this.itemstacks.put(i, item);
		this.setItemLine(i, line, slot);
	}

	public void setItemAnvil(final InstantShopItem item, final int line, final int slot) {
		final ItemStack i = item.getItemUI(this.getPlayer());
		this.itemstacks.put(i, item);
		this.setItemLine(i, line, slot);
	}

	public void setItemLine(final int id, final ItemStack item, final int line, final int slot) {
		super.setItemLine(new NBTItem(item).setInteger("itemID", id).getItem(), line, slot);
	}

}
