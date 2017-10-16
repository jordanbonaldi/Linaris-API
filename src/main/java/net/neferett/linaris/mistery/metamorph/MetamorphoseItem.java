package net.neferett.linaris.mistery.metamorph;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.disguises.DisguiseAPI;
import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.mistery.MysteryItem;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class MetamorphoseItem extends MysteryItem {
	
	Disguise disguise;
	
	public MetamorphoseItem(String id, String name, PriceType priceType, double price, ItemStack itemUI, Disguise disguise) {
		super("metamorphose-" + id,"§b" + name, priceType, price, "", itemUI);
		this.disguise = disguise;

	}

	@Override
	public void onUse(Player p,boolean save) {
		
		if(save) {
			SettingsManager.setSetting(p.getName(), Games.LOBBY,"metamorphose", getID());		
		}
		
		if (DisguiseAPI.isDisguised(p))
			DisguiseAPI.undisguiseToAll(p);
		
		Disguise dis = disguise.clone();
		dis.setViewSelfDisguise(SettingsManager.isEnabled(p.getName(), Games.LOBBY,"metamorphose-viewself",true));
		
		TaskManager.runTask(() -> {
			DisguiseAPI.disguiseEntity(p, dis);
		});
		
	}

	@Override
	public void onRemove(Player p) {
		DisguiseAPI.undisguiseToAll(p);
		SettingsManager.removeSetting(p.getName(), Games.LOBBY,"metamorphose");
	}

}
