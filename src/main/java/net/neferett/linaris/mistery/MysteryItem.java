package net.neferett.linaris.mistery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.ItemInfo;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.Rank;
import net.neferett.linaris.api.ShopItemsManager;
import net.neferett.linaris.utils.ShopMessage;
import net.neferett.linaris.utils.StringUtils;
import net.neferett.linaris.utils.gui.GuiManager;
import net.neferett.linaris.utils.gui.GuiScreen;

public abstract class MysteryItem {

	public enum PriceType {
		EC, LC
	}

	public enum RankType {
		EPICVIP, MiniVIP, VIP, VIPPLUS
	}

	String		description;
	String		id;
	ItemStack	itemUI;
	String		name;

	double		price;

	PriceType	priceType;

	RankType	vipLevel;

	public MysteryItem(final String id, final String name, final PriceType priceType, final double price,
			final String description, final ItemStack itemUI) {
		this.id = id;
		this.name = name;
		this.priceType = priceType;
		this.price = price;
		this.description = description;
		this.itemUI = itemUI;

		MysteryItemsManager.getInstance().registerMysteryItem(this);
	}

	public String getColoredPrice() {
		if (this.priceType == PriceType.EC)
			return "§e" + this.price + "Coins";
		else
			return "§b" + this.price + "Crédits";
	}

	public String getColoredRank() {
		if (this.vipLevel == null)
			return "§7";
		if (this.vipLevel == RankType.MiniVIP)
			return "§fMiniVIP";
		else if (this.vipLevel == RankType.VIP)
			return "§fVIP";
		else if (this.vipLevel == RankType.VIPPLUS)
			return "§bVIP+";
		else if (this.vipLevel == RankType.EPICVIP)
			return "§aHéro";
		else
			return "§7";
	}

	public String getDescription() {
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
		final ItemStack item = this.getItem().clone();

		final ItemMeta meta = item.getItemMeta();

		final List<String> strings = new ArrayList<>();

		if (this.getDescription() != null && this.getDescription().length() > 1)
			strings.addAll(StringUtils.wrap(StringUtils.wrap(this.getDescription(), 25)));

		if (this.vipLevel != null)
			strings.add("§cRéservé aux " + this.getColoredRank());
		else {
			strings.add("");
			if (ShopItemsManager.haveItem(p.getName().toLowerCase(), String.valueOf(Games.LOBBY.getID()), this.getID()))
				strings.add("§aAcheté");
			else
				strings.add("§aClic gauche: débloquer " + this.getColoredPrice());
		}

		meta.setLore(strings);

		meta.setDisplayName(this.getName());

		item.setItemMeta(meta);

		return item;
	}

	public ItemStack getItemUIBuy(final Player p) {
		final ItemStack item = this.getItem().clone();

		final ItemMeta meta = item.getItemMeta();

		final List<String> strings = new ArrayList<>();

		if (this.getDescription() != null)
			strings.addAll(StringUtils.wrap(StringUtils.wrap(this.getDescription(), 25)));

		strings.add("");
		strings.add("§6Prix " + this.getColoredPrice());

		meta.setLore(strings);

		meta.setDisplayName(this.getName());

		item.setItemMeta(meta);

		return item;
	}

	public String getName() {
		return this.name;
	}

	public double getPrice() {
		return this.price;
	}

	public PriceType getPriceType() {
		return this.priceType;
	}

	public RankType getVipLevel() {
		return this.vipLevel;
	}

	public abstract void onRemove(Player p);

	public abstract void onUse(Player p, boolean save);

	public void setVipLevel(final RankType vipLevel) {
		this.vipLevel = vipLevel;
	}

	public void testBuy(final Player p) {

		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName().toLowerCase());
		final String name = p.getName().toLowerCase();

		if (this.priceType == PriceType.EC) {

			if (pd.getEC() >= this.price) {
				ShopMessage.itemBoughtEC(p, ChatColor.stripColor(this.getName()), this.price);
				pd.withdrawCoins(this.price, null);
				ShopItemsManager.setItem(name, String.valueOf(Games.LOBBY.getID()), new ItemInfo(this.getID(), 1));
			} else
				ShopMessage.itemNotEnoughGolds(p);

		} else if (pd.getLC() >= this.price) {
			ShopMessage.itemBoughtEC(p, ChatColor.stripColor(this.getName()), this.price);
			pd.withdrawLC(this.price, null);
			ShopItemsManager.setItem(name, String.valueOf(Games.LOBBY.getID()), new ItemInfo(this.getID(), 1));
		} else
			ShopMessage.itemNotEnoughLegendaryCoins(p);
	}

	public void useOrBuy(final Player p, final GuiScreen last) {

		if (this.vipLevel != null) {

			final Rank rank = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName()).getPRank();

			if (this.vipLevel == RankType.MiniVIP)
				if (rank.getVipLevel() >= 1) {
					this.onUse(p, true);
					return;
				} else
					p.sendMessage("§cRéservé aux " + this.getColoredRank());

			if (this.vipLevel == RankType.VIP)
				if (rank.getVipLevel() >= 2) {
					this.onUse(p, true);
					return;
				} else
					p.sendMessage("§cRéservé aux " + this.getColoredRank());

			if (this.vipLevel == RankType.VIPPLUS)
				if (rank.getVipLevel() >= 3) {
					this.onUse(p, true);
					return;
				} else
					p.sendMessage("§cRéservé aux " + this.getColoredRank());

			if (this.vipLevel == RankType.EPICVIP)
				if (rank.getVipLevel() >= 4) {
					this.onUse(p, true);
					return;
				} else
					p.sendMessage("§cRéservé aux " + this.getColoredRank());

		} else if (ShopItemsManager.haveItem(p.getName().toLowerCase(), String.valueOf(Games.LOBBY.getID()),
				this.getID()))
			this.onUse(p, true);
		else
			GuiManager.openGui(new BuyItemGui(p, this, last));
	}

}
