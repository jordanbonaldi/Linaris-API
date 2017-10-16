package net.neferett.linaris.customevents.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.customevents.events.AnvilRenameItemEvent;
import net.neferett.linaris.utils.ItemStackUtils;

public class AnvilItemRenameListener implements Listener
{
	@EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
	public void onInventoryClickForRenaming(InventoryClickEvent event) {
			HumanEntity ent = event.getWhoClicked();
			if(!(ent instanceof Player))return;
			// Verif : inventaire = anvil
			Inventory inv = event.getInventory();				
			if(!(inv instanceof AnvilInventory))return;
			// Verif : récupération de l'item fusionné 
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if(rawSlot!=2 || rawSlot != view.convertSlot(rawSlot))return;
			// Verif : item fusionné possède un nom 
			ItemStack current = event.getCurrentItem();
			if(!ItemStackUtils.hasDisplayName(event.getCurrentItem()))return;
			// Si le premier item porte le meme nom que le resultat (= pas de changement de nom)
			ItemStack first = inv.getItem(0);
			if(ItemStackUtils.hasDisplayName(first) && ItemStackUtils.getDisplayName(first).equals(ItemStackUtils.getDisplayName(current)))return;
			// Appel de l'event
			Bukkit.getPluginManager().callEvent(new AnvilRenameItemEvent(event));
			/*
			 * Old
			if(ent instanceof Player)
			{
				Inventory inv = event.getInventory();				
				if(inv instanceof AnvilInventory)
				{
					InventoryView view = event.getView();
					int rawSlot = event.getRawSlot();
					if(rawSlot == view.convertSlot(rawSlot))
					{
						if(rawSlot == 2)
						{
							ItemStack item = event.getCurrentItem();
							if(item != null)
							{
								ItemMeta meta = item.getItemMeta();
								if(meta != null)
								{
									if(meta.hasDisplayName())
									{
										Bukkit.getPluginManager().callEvent(new AnvilRenameItemEvent(event));
										return;
									}
								}
							}
						}
					}
				}
		}
		*/
	}
}
