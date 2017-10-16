package net.neferett.linaris.customevents.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilRenameItemEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private ItemStack item;

	private String DisplayName = "";

	private ItemMeta meta;

	private List<String> lore;

	private Player player;

	protected InventoryClickEvent event;

	private boolean isCancelled = false;

	public AnvilRenameItemEvent(InventoryClickEvent event) {
		this.player = (Player) event.getWhoClicked();
		this.item = event.getCurrentItem();
		this.meta = item.getItemMeta();
		this.DisplayName = meta.getDisplayName();
		this.lore = meta.getLore();
		this.event = event;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Obtient la META de l'item renomer
	 * 
	 * @return meta
	 */
	public ItemMeta getItemMeta() {
		return meta;
	}

	/**
	 * Obtient le nom de l'item renomer
	 * 
	 * @return DisplayName
	 */
	public String getItemDisplayName() {
		return DisplayName;
	}

	/**
	 * Obtient la lore de l'item renomer
	 * 
	 * @return lore
	 */
	public List<String> getItemLore() {
		return lore;
	}

	/**
	 * Obtient l'item qui a été renomer
	 * 
	 * @return item
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Obtient le joueur qui a renomer l'item
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
		this.event.setCancelled(cancel);
	}
}
