package net.neferett.linaris.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * Crée un vue de deux inventaires (top et bot) pour le joueur
 * @author Likaos
 */
public class PlayerInventoryView extends InventoryView {

	Inventory		bot;
	Inventory		top;
	Player			player;
	InventoryType	type;

	public PlayerInventoryView(Player player, Inventory targetInventory) {
		this.player = player;
		this.top = targetInventory;
		this.bot = player.getInventory();
		type = targetInventory.getType();
	}

	@Override
	public Inventory getBottomInventory() {
		return bot;
	}

	@Override
	public HumanEntity getPlayer() {
		return player;
	}

	@Override
	public Inventory getTopInventory() {
		return top;
	}

	@Override
	public InventoryType getType() {
		return type;
	}

}
