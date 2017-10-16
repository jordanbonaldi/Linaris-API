package net.neferett.linaris.mistery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.MobDisguise;
import net.neferett.linaris.disguises.disguisetypes.watchers.BatWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.CreeperWatcher;
import net.neferett.linaris.mistery.MysteryItem.PriceType;
import net.neferett.linaris.mistery.armors.BootsItem;
import net.neferett.linaris.mistery.armors.ChestplateItem;
import net.neferett.linaris.mistery.armors.HelmetItem;
import net.neferett.linaris.mistery.armors.LeggingsItem;
import net.neferett.linaris.mistery.metamorph.MetamorphoseItem;
import net.neferett.linaris.mistery.mounts.MountsItem;
import net.neferett.linaris.mistery.pets.PetItem;

public class MysteryItemsManager {

	List<MysteryItem> mysteryItems = new ArrayList<MysteryItem>();
	
	private static MysteryItemsManager instance;
	public static MysteryItemsManager getInstance() {
		return (instance == null) ? instance = new MysteryItemsManager() : instance;
	}
	
	public void inits() {
		for (ColorUtils color : ColorUtils.values()) {
			color.setHelmet(new HelmetItem(color.name().toLowerCase(), "Cuire color§", PriceType.EC, color.getPrices()[0], ColorUtils.createItem(Material.LEATHER_HELMET, "Cuir Coloré", color.getColor())));
			color.setChestplate(new ChestplateItem(color.name().toLowerCase(), "Cuire color§", PriceType.EC, color.getPrices()[1], ColorUtils.createItem(Material.LEATHER_CHESTPLATE, "Cuir Coloré", color.getColor())));
			color.setLeggings(new LeggingsItem(color.name().toLowerCase(), "Cuire color§", PriceType.EC, color.getPrices()[2], ColorUtils.createItem(Material.LEATHER_LEGGINGS, "Cuir Coloré", color.getColor())));
			color.setBoots(new BootsItem(color.name().toLowerCase(), "Cuire color§", PriceType.EC, color.getPrices()[3], ColorUtils.createItem(Material.LEATHER_BOOTS, "Cuir Coloré", color.getColor())));

		}
		
		for (OresUtils ore :OresUtils.values()) {
			ore.setHelmet(new HelmetItem(ore.getName().toLowerCase(), ore.getName(), PriceType.EC, ore.getPrices()[0],new ItemStack(ore.getMata())));
			ore.setChestplate(new ChestplateItem(ore.getName().toLowerCase(), ore.getName(), PriceType.EC, ore.getPrices()[1], new ItemStack(ore.getMatb())));
			ore.setLeggings(new LeggingsItem(ore.getName().toLowerCase(), ore.getName(), PriceType.EC, ore.getPrices()[2], new ItemStack(ore.getMatc())));
			ore.setBoots(new BootsItem(ore.name().toLowerCase(), ore.getName(), PriceType.EC, ore.getPrices()[3], new ItemStack(ore.getMatd())));
			
		}
		
		for (MetamorphosesUtils meta : MetamorphosesUtils.values()) {
			
			MobDisguise dis = new MobDisguise(meta.getType());
			
			if (meta == MetamorphosesUtils.POWERCREEPER) {
				CreeperWatcher cdis = new CreeperWatcher(dis);
				cdis.setPowered(true);
				dis.setWatcher(cdis);
			}
			
			if (meta == MetamorphosesUtils.BAT) {
				BatWatcher cdis = new BatWatcher(dis);
				cdis.setFlying(true);
				dis.setWatcher(cdis);
			}
			
			meta.setDisguise(dis);
			
			MetamorphoseItem item = new MetamorphoseItem(meta.name().toLowerCase(), meta.getName(), meta.getPriceType(), meta.getPrice(),meta.getItemStack(),meta.getDisguise());
			if (meta.getVipLevel() != null)
				item.setVipLevel(meta.getVipLevel());
			meta.setItem(item);			
		}
		
		for (PetsUtils utils : PetsUtils.values()) {
			utils.setItem(new PetItem(utils.getName().toLowerCase().replace(' ', '-'), utils.getName(), utils.getPriceType(), utils.getPrice(), utils.getItemStack(), utils.getType()));
		}
		
		for (MountsUtils utils : MountsUtils.values()) {
			utils.setItem(new MountsItem(utils.getName().toLowerCase().replace(' ', '-'), utils.getName(), utils.getPriceType(), utils.getPrice(), utils.getItemStack(), utils.getType()));
		}

	}
	
	public List<MysteryItem> getMysteryItems() {
		return mysteryItems;
	}
	
	public MysteryItem getMysteryItem(String id) {
		if (mysteryItems.isEmpty()) return null;
		for (MysteryItem game : mysteryItems) 
			if (game.getID().equals(id)) return game;
		return null;
	}

	public List<MysteryItem> getMysteryItem(Class<? extends MysteryItem> type) {
		List<MysteryItem> items = new ArrayList<MysteryItem>();
		if (mysteryItems.isEmpty()) return items;
		for (MysteryItem game : mysteryItems) 
			if (type.isInstance(game)) items.add(game);
		return items;
	}
	
	
	public void registerMysteryItem(MysteryItem game) {
		this.mysteryItems.add(game);
	}
	
	
	
	
}
