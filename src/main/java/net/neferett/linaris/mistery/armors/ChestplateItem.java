package net.neferett.linaris.mistery.armors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.mistery.MysteryItem;
import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.specialitems.SpecialItem;

public class ChestplateItem extends MysteryItem {
	
	public ChestplateItem(String id, String name, PriceType priceType, double price, ItemStack itemUI) {
		super("armors-chestplate-" + id,"§f" + name, priceType, price, "", itemUI);

	}

	@Override
	public void onUse(Player p,boolean save) {
		
		if(save) {
			SettingsManager.setSetting(p.getName(), Games.LOBBY,"armors-chestplate", getID());		
		}
		
		MenuItem item = new MenuItem(getName(),getItem()) {

			@Override
			public void inventoryClickEvent(Player player) {
				
			}
			
		};
		
		SpecialItem.registerItem(item);
		p.getInventory().setChestplate(item.getStaticItem());
	}

	@Override
	public void onRemove(Player p) {
		p.getInventory().setChestplate(null);
		SettingsManager.removeSetting(p.getName(), Games.LOBBY, "armors-chestplate");
	}

}
