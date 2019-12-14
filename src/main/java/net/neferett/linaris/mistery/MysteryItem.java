package net.neferett.linaris.mistery;

import java.util.ArrayList;
import java.util.List;

import net.neferett.linaris.api.*;
import net.neferett.linaris.api.ranks.RankManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Setter;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.ranks.RankAPI;
import net.neferett.linaris.utils.ShopMessage;
import net.neferett.linaris.utils.StringUtils;
import net.neferett.linaris.utils.gui.GuiManager;
import net.neferett.linaris.utils.gui.GuiScreen;

public abstract class MysteryItem {

	public enum PriceType {
		EC, LC, TOKEN
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

	@Setter
	int			vipLevel;

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
		else if (this.priceType == PriceType.LC)
			return "§b" + this.price + "Crédits";
		else
			return "§e" + this.price + "Coins";
	}

	public String getColoredRank() {
		if (this.vipLevel == 1)
			return "§f";
		else if (this.vipLevel == 2)
			return "§eSaiyan";
		else if (this.vipLevel == 3)
			return "§9Kaïo Shin";
		else if (this.vipLevel == 4)
			return "§5Hakaï Shin";
		else if (this.vipLevel == 5)
			return "§dDaï Shinkan";
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

		RankAPI rank = RankManager.getInstance().getRanks().stream()
				.filter(e -> e.getVipLevel() == this.vipLevel).findFirst().orElse(null);

		if (rank != null && this.vipLevel != 0)
			strings.add("§cRéservé aux §" + rank.getColor() + rank.getName());
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

	public abstract void onRemove(Player p);

	public abstract void onUse(Player p, boolean save);

	public void testBuy(final Player p) {

		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName().toLowerCase());
		final String name = p.getName().toLowerCase();

		if (this.priceType == PriceType.TOKEN) {

			if (pd.getTokens() >= this.price) {
				ShopMessage.itemBoughtTokens(p, ChatColor.stripColor(this.getName()), this.price);
				pd.setInt("tokens", (int) (pd.getTokens() - this.price));
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

		if (this.vipLevel != 0) {

			final RankAPI rank = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName()).getRank();
			RankAPI r = RankManager.getInstance().getRanks().stream()
					.filter(e -> e.getVipLevel() == this.vipLevel).findFirst().orElse(null);

			if (rank.getVipLevel() >= this.vipLevel)
				this.onUse(p, true);
			else
				p.sendMessage("§cRéservé aux §" + r.getColor() + r.getName());

		} else if (ShopItemsManager.haveItem(p.getName().toLowerCase(), String.valueOf(Games.LOBBY.getID()),
				this.getID()))
			this.onUse(p, true);
		else
			GuiManager.openGui(new BuyItemGui(p, this, last));
	}

}
