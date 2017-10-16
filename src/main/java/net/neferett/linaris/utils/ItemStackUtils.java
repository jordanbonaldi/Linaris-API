package net.neferett.linaris.utils;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_8_R3.Item;

public class ItemStackUtils {

	/**
	 * Crée un itemStack renommé
	 * 
	 * @param mat
	 * @param size
	 * @param dataValue
	 * @param name
	 * @param lore
	 * @return
	 */
	public static ItemStack createRenamedItemStack(Material mat, int size, short dataValue, String name,
			String[] lore) {
		ItemStack itemStack = new ItemStack(mat, size, dataValue);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (name != null)
			itemMeta.setDisplayName(name);
		if (lore != null)
			itemMeta.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	/**
	 * Renomme un itemStack, attention utilisé avec les specialItem les rend
	 * inutilisables
	 * 
	 * @param itemStack
	 * @param newName
	 * @return
	 */
	public static ItemStack renameItemStack(ItemStack itemStack, String newName) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(newName);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	/**
	 * Retoure un itemStack sans attributs (en bleu)
	 * 
	 * @param itemStack
	 * @return l'itemStack sans attributs
	 * @deprecated Ne fonctionne plus à partir de la 1.7.10
	 */
	public static ItemStack getItemStackWithNoAttributes(ItemStack itemStack) {
		return itemStack;
	}

	/**
	 * Vérifie si l'itemStack a un nom affiché
	 * 
	 * @param itemStack
	 * @return a un nom ou pas
	 */
	public static Boolean hasDisplayName(ItemStack itemStack) {
		if (!isValid(itemStack))
			return false;
		if (!itemStack.hasItemMeta())
			return false;
		if (!itemStack.getItemMeta().hasDisplayName())
			return false;
		return true;
	}

	/**
	 * Recupère le nom de l'item
	 * 
	 * @param itemStack
	 * @return le nom ou null
	 */
	public static String getDisplayName(ItemStack itemStack) {
		if (hasDisplayName(itemStack))
			return itemStack.getItemMeta().getDisplayName();
		return null;
	}

	/**
	 * Si l'objet est un ItemStack
	 * 
	 * @param itemStack
	 * @return
	 */
	public static boolean isValid(ItemStack itemStack) {
		return itemStack != null && itemStack.getType() != Material.AIR;
	}

	/**
	 * @param id
	 *            de l'item
	 * @param nombre
	 *            maximum d'item du stack
	 */
	public static void setMaxStackSize(int itemId, int max) {
		Item item = Item.getById(itemId);
		item.c(max);
	}

	/**
	 * Retourne la piece d'armure à la couleur demandée. La liste des
	 * materials acceptés sont : LEATHER_HELMET LEATHER_CHESTPLATE
	 * LEATHER_LEGGINGS LEATHER_BOOTS name et lore sont optionels.
	 * 
	 * @param material
	 * @param color
	 * @param name
	 * @return
	 */
	public static ItemStack getColoredArmor(Material material, Color color, String name, String[] lore) {
		ItemStack item = new ItemStack(material, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		if (name != null)
			meta.setDisplayName(name);
		if (lore != null)
			meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getPlayerHead(String player) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwner(player);
		head.setItemMeta(meta);

		return head;
	}

}
