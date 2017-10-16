package net.neferett.linaris.mistery.armors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.mistery.MysteryItem;
import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.specialitems.SpecialItem;

public class HelmetItem extends MysteryItem {
	
	public HelmetItem(String id, String name, PriceType priceType, double price, ItemStack itemUI) {
		super("armors-helmet-" + id,"§f" + name, priceType, price, "", itemUI);

	}

	@Override
	public void onUse(Player p,boolean save) {
		
		if(save) {
			SettingsManager.setSetting(p.getName(), Games.LOBBY,"armors-helmet", getID());		
		}
		
		MenuItem item = new MenuItem(getName(),getItem()) {

			@Override
			public void inventoryClickEvent(Player player) {
				
			}
			
		};
		
		SpecialItem.registerItem(item);
		p.getInventory().setHelmet(item.getStaticItem());
	}

	@Override
	public void onRemove(Player p) {
		p.getInventory().setHelmet(null);
		SettingsManager.removeSetting(p.getName(), Games.LOBBY,"armors-helmet");
	}

}
