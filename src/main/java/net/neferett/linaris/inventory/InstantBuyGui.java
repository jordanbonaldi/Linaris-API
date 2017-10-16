package net.neferett.linaris.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.ItemStackUtils;
import net.neferett.linaris.utils.ShopMessage;
import net.neferett.linaris.utils.gui.GuiScreen;

public class InstantBuyGui extends GuiScreen {

	InstantShopItem	item;
	GuiScreen		screen;

	public InstantBuyGui(final Player p, final InstantShopItem item, final GuiScreen screen) {
		super("Confirmer l'achat", 4, p, false);
		this.item = item;
		this.screen = screen;
		this.build();
	}

	@Override
	public void drawScreen() {

		BukkitAPI.get().getTasksManager().addTask(() -> {
			this.setItemLine(this.item.getItemUIBuy(this.getPlayer()), 2, 5);

			this.setItemLine(
					ItemStackUtils.createRenamedItemStack(Material.SLIME_BALL, 1, (short) 0, "§a✔ Confirmer ✔", null),
					3, 4);
			this.setItemLine(
					ItemStackUtils.createRenamedItemStack(Material.BARRIER, 1, (short) 0, "§c✖ Annuler ✖", null), 3, 6);

			this.setItemLine(
					ItemStackUtils.createRenamedItemStack(Material.ARROW, 1, (short) 0, "§fRevenir en arrière", null),
					4, 9);
		});

	}

	@Override
	public void onClick(final ItemStack item, final InventoryClickEvent event) {

		if (!ItemStackUtils.isValid(item)) return;
		event.setCancelled(true);
		if (!item.hasItemMeta()) return;
		if (!item.getItemMeta().hasDisplayName()) return;

		if (item.getType() == Material.ARROW) this.screen.open();

		if (item.getType() == Material.SLIME_BALL) {

			this.getPlayer().closeInventory();

			BukkitAPI.get().getTasksManager().addTask(() -> {
				this.item.testBuy(BukkitAPI.get().getPlayerDataManager().getPlayerData(this.getPlayer().getName()));
			});

		}
		if (item.getType() == Material.BARRIER) {

			this.getPlayer().closeInventory();
			ShopMessage.itemBoughtCancel(this.getPlayer());

		}

	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub

	}

}
