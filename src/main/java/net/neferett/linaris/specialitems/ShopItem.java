package net.neferett.linaris.specialitems;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.ShopItemsManager;

/**
 * Le shop item n'est utilisable quand dans inventaire
 * @author Likaos
 */
public abstract class ShopItem extends SpecialItem {

	public HashMap<Integer, LevelInfo>	levelInfos	= new HashMap<Integer, LevelInfo>();
	public String							shopId;
	public String							shopItemId;

	public ShopItem(String name, String[] lore, ItemStack itemStack, String shopName, String shopItemId) {
		super(name, lore, itemStack);
		this.setInventoryClickable(true);
		this.shopId = BukkitAPI.get().getServerInfos().getGameName();
		this.shopItemId = shopItemId;
		this.levelInfos = buildLevelInfos();
	}
	
	public ShopItem(String name, ItemStack itemStack, String shopName, String shopItemId, String... lore) {
		super(name, lore, itemStack);
		this.setInventoryClickable(true);
		this.shopId = BukkitAPI.get().getServerInfos().getGameName();
		this.shopItemId = shopItemId;
		this.levelInfos = buildLevelInfos();
	}

	@Override
	public void rightClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void leftClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void middleClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	/**
	 * La méthode qui sera appelée quand une boutique veut recupérer l'objet
	 * @param player
	 * @return
	 */
	public abstract ItemStack getShopItem(Player player);

	/**
	 * L'id du shop, correspond à la gameId chez nous
	 * @return
	 */
	public String getShopId() {
		return shopId;
	}

	/**
	 * La Lore du niveau actuel
	 * @return
	 */
	public List<String> getCurrentLevelLore(int itemLevel) {
		return this.levelInfos.get(itemLevel).getLore();
	}

	/**
	 * L'id de l'item dans le shop
	 * @return
	 */
	public String getShopItemId() {
		return shopItemId;
	}

	/**
	 * Recupère le niveau d'un objet
	 * @param player
	 * @return niveau de l'objet
	 */
	public int getItemLevel(Player player) {
		return ShopItemsManager.getItem(player.getName(), shopId, shopItemId).getLevel();
	}

	/**
	 * Les level infos, 0 = texte à afficher si non acheté
	 * @return
	 */
	protected abstract HashMap<Integer, LevelInfo> buildLevelInfos();

}
