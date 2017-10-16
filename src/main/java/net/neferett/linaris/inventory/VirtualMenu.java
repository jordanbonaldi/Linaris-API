package net.neferett.linaris.inventory;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.specialitems.InfoItem;
import net.neferett.linaris.specialitems.SpecialItem;

public abstract class VirtualMenu extends VirtualInventory {

	/**
	 * Si l'inventaire est dynamique le contenu est rechargé à chaque ouverture, possible d'éditer la position des
	 * objets Rajouter/Enlever/Modifier
	 */

	public VirtualMenu(String title, int row) {
		super(title, row);
	}

	@Override
	public void open(Player player) {
		fill(player, "");
	}

	@Override
	public void openWithExtraText(Player player, String extraText) {
		fill(player, extraText);
	}

	/**
	 * Rempli l'inventaire d'objets
	 * @param player
	 * @param extraText
	 */
	public void fill(Player player, String extraText) {
		BukkitAPI.get().getTasksManager().addTask(() -> {
			this.inventory = Bukkit.createInventory(this, row * 9, getTitleWithExtraText(extraText));

			for (Entry<Integer, SpecialItem> entry : myItems.entrySet()) {
				SpecialItem specialItem = entry.getValue();
				if (specialItem.getOpLevel() > BukkitAPI.get().getPlayerDataManager().getPlayerData(player.getName()).getRank().getModerationLevel()) continue;
				if (specialItem.getVipLevel() > BukkitAPI.get().getPlayerDataManager().getPlayerData(player.getName()).getRank().getVipLevel()) continue;
				if (specialItem instanceof InfoItem) inventory.setItem(entry.getKey(), ((InfoItem) specialItem).getInfoItem(player));
				else this.inventory.setItem(entry.getKey(), specialItem.getStaticItem());
			}
			player.openInventory(inventory);
		});
	}
}
