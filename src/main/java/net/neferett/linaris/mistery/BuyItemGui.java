package net.neferett.linaris.mistery;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.ItemStackUtils;
import net.neferett.linaris.utils.ShopMessage;
import net.neferett.linaris.utils.gui.GuiScreen;

public class BuyItemGui extends GuiScreen {

	MysteryItem item;
	GuiScreen screen;
	
	public BuyItemGui(Player p, MysteryItem item,GuiScreen screen) {
		super("Confirmer l'achat", 4, p, false);
		this.item = item;
		this.screen = screen;
		build();
	}

	@Override
	public void drawScreen() {
		
		BukkitAPI.get().getTasksManager().addTask(() -> {
			setItemLine(item.getItemUIBuy(getPlayer()), 2	, 5);
			
			
			setItemLine(ItemStackUtils.createRenamedItemStack(Material.SLIME_BALL, 1, (short)0, "§a✔ Confirmer ✔", null), 3, 4);
			setItemLine(ItemStackUtils.createRenamedItemStack(Material.BARRIER, 1, (short)0, "§c✖ Annuler ✖", null), 3, 6);

			
			
			setItemLine(new ProfileItem(getPlayer()).getStaticItem(), 4	, 1);
		
			
			setItemLine(ItemStackUtils.createRenamedItemStack(Material.ARROW, 1, (short)0, "§fRevenir en arrière", null), 4, 9);
		});
		
	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(ItemStack item, InventoryClickEvent event) {
		
		if (!ItemStackUtils.isValid(item))
			return;
		event.setCancelled(true);
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		
		if (item.getType() == Material.ARROW) {
			
			screen.open();
			
		}
		
		if (item.getType() == Material.SLIME_BALL) {
			
			getPlayer().closeInventory();
			
			BukkitAPI.get().getTasksManager().addTask(() -> {
				this.item.testBuy(getPlayer());
			});
			
		}
		if (item.getType() == Material.BARRIER) {
		
			getPlayer().closeInventory();
			ShopMessage.itemBoughtCancel(getPlayer());
			
		}
		
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}

}
