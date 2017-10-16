package net.neferett.linaris.mistery.pets;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.PlayerLocal;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.api.pets.Pet;
import net.neferett.linaris.mistery.MysteryItem;

public class PetItem extends MysteryItem {
	
	Class<? extends Pet> pet;
	
	public PetItem(String id, String name, PriceType priceType, double price, ItemStack itemUI, Class<? extends Pet> pet) {
		super("pet-" + id,"§b" + name, priceType, price, "", itemUI);
		this.pet = pet;
	}

	@Override
	public void onUse(Player p,boolean save) {
		
		PlayerLocal pl = PlayerLocal.getPlayer(p.getName());
		if (pl.getPet() != null) {
			pl.getPet().dispawn();
			pl.setPet(null);
		}
		
		SettingsManager.setSetting(p.getName(), Games.LOBBY,"pet", getID());			
		
		try {
			Constructor<?> ctor = pet.getConstructor(Player.class);
			Pet pet = (Pet) ctor.newInstance(p);
			PlayerLocal.getPlayer(p.getName()).setPet(pet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onRemove(Player p) {
		PlayerLocal pl = PlayerLocal.getPlayer(p.getName());
		if (pl.getPet() != null) {
			pl.getPet().dispawn();
			pl.setPet(null);
		}
		SettingsManager.removeSetting(p.getName(), Games.LOBBY,"pet");
	}

}
