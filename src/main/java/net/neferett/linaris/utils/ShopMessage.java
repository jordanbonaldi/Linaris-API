package net.neferett.linaris.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ShopMessage {
	public static String	currency;
	public static String	lendaryCoinsCurrency;
	public static String	shopPrefix;
	public static String	storeUrl;

	static {
		ShopMessage.shopPrefix = "§7[§6Boutique§7] ";
		ShopMessage.storeUrl = "§bhttp://linaris.fr/store";
		ShopMessage.currency = "Coins";
		ShopMessage.lendaryCoinsCurrency = "Crédits";
	}

	public static void itemBought(final Player player, final String name, final int level) {
		sendToPlayer(player, "§6Achat r\u00e9ussi > " + name + " (" + level + ")");
		player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.5f, 1.0f);
	}

	public static void itemBoughtCancel(final Player player) {
		sendToPlayer(player, "§cAchat annul\u00e9");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	public static void itemBoughtEC(final Player player, final String name, final double price) {
		sendToPlayer(player, "§6Achat r\u00e9ussi §a" + name + " §e-" + price + "Coins");
		player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.5f, 1.0f);
	}

	public static void itemBoughtLC(final Player player, final String name, final double price) {
		sendToPlayer(player, "§6Achat r\u00e9ussi §a" + name + " §b-" + price + "LC");
		player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.5f, 1.0f);
	}

	public static void itemBoughtTokens(final Player player, final String name, final double price) {
		sendToPlayer(player, "§6Achat r\u00e9ussi §a" + name + " §c-" + price + "Tokens");
		player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.5f, 1.0f);
	}

	public static void itemFailedUnknow(final Player player) {
		sendToPlayer(player, "§cErreur lors de l'achat, signalez le sur le forum ! ");
		player.closeInventory();
	}

	public static void itemIsAlreadyBought(final Player player) {
		sendToPlayer(player, "§cVous poss\u00e9dez d\u00e9j\u00e0 cet objet !");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	public static void itemIsAtMaxLevel(final Player player) {
		sendToPlayer(player, "§cCet article est d\u00e9j\u00e0 au niveau maximum !");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	public static void itemNotBoughtGoWebShop(final Player player) {
		sendToPlayer(player, "§cD\u00e9bloquez cet objet sur " + ShopMessage.storeUrl);
		player.closeInventory();
	}

	public static void itemNotEnoughGolds(final Player player) {
		sendToPlayer(player, "§cVous ne poss\u00e9dez pas assez de: §eCoins§c !");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	public static void itemNotEnoughLegendaryCoins(final Player player) {
		sendToPlayer(player, "§cVous ne poss\u00e9dez pas assez de: §6Crédits§c");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	public static void itemNotEnoughTokens(final Player player) {
		sendToPlayer(player, "§cVous ne poss\u00e9dez pas assez de: §6Senzus§c");
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.0f);
	}

	private static void sendToPlayer(final Player player, final String msg) {
		player.sendMessage(ShopMessage.shopPrefix + msg);
	}
}
