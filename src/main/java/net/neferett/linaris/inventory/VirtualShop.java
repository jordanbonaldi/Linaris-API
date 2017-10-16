package net.neferett.linaris.inventory;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.specialitems.InfoItem;
import net.neferett.linaris.specialitems.ShopItem;
import net.neferett.linaris.specialitems.SpecialItem;

/**
 * Le Virtual Shop charge à la première ouverture l'état de sa boutique
 * 
 * @author Likaos
 */
public class VirtualShop extends VirtualInventory {

	/**
	 * Retourne l'id du virtualInventory shopItem
	 * 
	 * @param virtualInventoryId
	 * @return
	 */
	public static VirtualShop getVirtualShop(final int virtualInventoryId) {
		return (VirtualShop) VirtualInventory.get(virtualInventoryId);
	}

	public String	shopId;

	public String	shopName;

	public VirtualShop(final String title, final int row, final String shopName) {
		super(title, row);
		this.shopName = shopName;
		this.shopId = BukkitAPI.get().getServerInfos().getGameName();;
	}

	/**
	 * Recupère la shopId
	 * 
	 * @return
	 */
	public String getShopId() {
		return this.shopId;
	}

	/**
	 * Recupère le shopName
	 * 
	 * @return
	 */
	public String getShopName() {
		return this.shopName;
	}

	@Override
	public void open(final Player player) {
		this.openWithExtraText(player, "");
	}

	@Override
	public void openWithExtraText(final Player player, final String extraText) {

		BukkitAPI.get().getTasksManager().addTask(() -> {
			if (player.isOnline() && player.isValid()) {
				final Inventory shopInventory = Bukkit.createInventory(this, this.row * 9,
						this.getTitleWithExtraText(extraText));
				for (final Entry<Integer, SpecialItem> entry : this.myItems.entrySet()) {
					final SpecialItem item = entry.getValue();
					if (item.getOpLevel() > BukkitAPI.get().getPlayerDataManager().getPlayerData(player.getName())
							.getRank().getModerationLevel())
						continue;
					if (item instanceof ShopItem)
						shopInventory.setItem(entry.getKey(), ((ShopItem) item).getShopItem(player));
					else if (item instanceof InfoItem)
						shopInventory.setItem(entry.getKey(), ((InfoItem) item).getInfoItem(player));
					else shopInventory.setItem(entry.getKey(), item.getStaticItem());
				}
				player.openInventory(shopInventory);
			}
		});

	}
}
