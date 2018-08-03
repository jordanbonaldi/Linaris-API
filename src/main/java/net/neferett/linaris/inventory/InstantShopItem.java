package net.neferett.linaris.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.utils.ShopMessage;
import net.neferett.linaris.utils.gui.GuiManager;
import net.neferett.linaris.utils.gui.GuiScreen;

public abstract class InstantShopItem {

	public enum PriceType {
		EC, LC, TOKEN
	}

	List<String>	description;
	String			id;
	ItemStack		itemUI;
	String			name;
	double			price;

	PriceType		priceType;

	public InstantShopItem(final String id, final String name, final PriceType priceType, final double price,
			final List<String> description, final ItemStack itemUI) {
		this.id = id;
		this.name = name;
		this.priceType = priceType;
		this.price = price;
		this.description = description;
		this.itemUI = itemUI;
	}

	public abstract boolean alreadyHave(PlayerData pd);

	public double getBasePrice() {
		return this.price;
	}

	public String getColoredPrice(final Player p) {
		if (this.priceType == PriceType.EC)
			return "§e" + this.price + "Coins";
		else if (this.priceType == PriceType.LC)
			return "§b" + this.price + "Crédits";
		else
			return "§c" + this.price + "Senzus";
	}

	public List<String> getDescription() {
		return this.description;
	}

	public String getID() {
		return this.id;
	}

	public ItemStack getItem() {
		final ItemStack item = this.itemUI.clone();
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getName());
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getItemUI(final Player p) {
		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName().toLowerCase());
		final ItemStack item = this.getItem().clone();

		final ItemMeta meta = item.getItemMeta();

		final List<String> strings = new ArrayList<>();

		if (this.getDescription() != null && !this.getDescription().isEmpty())
			strings.addAll(this.getDescription());
		strings.add("");
		if (this.alreadyHave(pd))
			strings.add("§aAcheté");
		else
			strings.add("§aClic gauche: débloquer " + this.getColoredPrice(p));

		meta.setLore(strings);

		meta.setDisplayName(this.getName());

		item.setItemMeta(meta);

		return item;
	}

	public ItemStack getItemUIBuy(final Player p) {
		final ItemStack item = this.getItem().clone();

		final ItemMeta meta = item.getItemMeta();

		final List<String> strings = new ArrayList<>();

		if (this.getDescription() != null && !this.getDescription().isEmpty())
			strings.addAll(this.getDescription());

		strings.add("");
		strings.add("§6Prix " + this.getColoredPrice(p));

		meta.setLore(strings);

		meta.setDisplayName(this.getName());

		item.setItemMeta(meta);

		return item;
	}

	public String getName() {
		return this.name;
	}

	public PriceType getPriceType() {
		return this.priceType;
	}

	public abstract void onBuy(PlayerData pd);

	public abstract void onUse(PlayerData pd);

	public void setDescription(final List<String> desc) {
		this.description = desc;
	}

	public void testBuy(final PlayerData pd) {

		final Player p = Bukkit.getPlayer(pd.getPlayername());

		if (this.priceType == PriceType.EC) {

			if (pd.getEC() >= this.price) {
				ShopMessage.itemBoughtEC(p, ChatColor.stripColor(this.getName()), this.price);
				pd.withdrawCoins(this.price, null);
				this.onBuy(pd);
			} else
				ShopMessage.itemNotEnoughGolds(Bukkit.getPlayer(pd.getPlayername()));

		} else if (this.priceType == PriceType.LC) {
			if (pd.getLC() >= this.price) {
				ShopMessage.itemBoughtLC(p, ChatColor.stripColor(this.getName()), this.price);
				pd.withdrawLC(this.price, null);
				this.onBuy(pd);
			} else
				ShopMessage.itemNotEnoughLegendaryCoins(p);
		} else if (pd.getTokens() >= this.price) {
			ShopMessage.itemBoughtTokens(p, ChatColor.stripColor(this.getName()), this.price);
			pd.setInt("tokens", (int) (pd.getTokens() - this.price));
			this.onBuy(pd);
		} else
			ShopMessage.itemNotEnoughTokens(p);
	}

	public void useOrBuy(final PlayerData pd, final GuiScreen last) {
		final Player p = Bukkit.getPlayer(pd.getPlayername());

		if (this.alreadyHave(pd)) {
			this.onUse(pd);
			return;
		} else
			GuiManager.openGui(new InstantBuyGui(p, this, last));

	}

}
