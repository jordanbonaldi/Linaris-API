package net.neferett.linaris.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.ItemStackUtils;

public abstract class GuiScreen implements Listener {

	Inventory	inv;
	String		name;
	Player		p;

	int			size;
	boolean		update;

	public GuiScreen(final String name, final int size, final Player p, final boolean update) {
		this.name = name;
		this.size = size;
		this.p = p;
		this.update = update;
	}

	public void addItem(final ItemStack item) {
		this.inv.addItem(item);
	}

	public void build() {
		this.inv = BukkitAPI.get().getServer().createInventory(this.p, this.size * 9, this.name);
	}

	public void clearInventory() {
		this.inv.clear();
	}

	public void close() {
		this.p.closeInventory();
	}

	abstract public void drawScreen();

	public Inventory getInventory() {
		return this.inv;
	}

	public Player getPlayer() {
		return this.p;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public abstract void onClick(ItemStack item, InventoryClickEvent event);

	public abstract void onClose();

	public abstract void onOpen();

	@EventHandler
	public void onPlayerInventory(final InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;
		if (!e.getClickedInventory().equals(this.inv))
			return;
		if (!ItemStackUtils.isValid(e.getCurrentItem()))
			return;
		e.setCancelled(true);
		if (!e.getCurrentItem().hasItemMeta())
			return;
		if (!e.getCurrentItem().getItemMeta().hasDisplayName())
			return;
		this.onClick(e.getCurrentItem(), e);
	}

	@EventHandler
	public void onPlayerInventory(final InventoryCloseEvent e) {
		this.onClose();
		if (!GuiManager.isOpened(this.getClass()))
			HandlerList.unregisterAll(this);
	}

	public void open() {
		this.p.openInventory(this.inv);
		this.drawScreen();
		this.p.updateInventory();
		BukkitAPI.get().getServer().getPluginManager().registerEvents(this, BukkitAPI.get());
		this.onOpen();
	}

	public void setFont(final ItemStack item) {
		for (int i = 0; i < this.inv.getSize(); i++)
			this.setItem(item, i);
	}

	public void setItem(final ItemStack item, final int slot) {
		this.inv.setItem(slot, item);
	}

	public void setItemLine(final ItemStack item, final int line, final int slot) {
		this.setItem(item, line * 9 - 9 + slot - 1);
	}

	public void setName(final String name) {
		this.name = name;
	}

}
