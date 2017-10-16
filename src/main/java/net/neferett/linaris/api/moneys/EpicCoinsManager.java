package net.neferett.linaris.api.moneys;

import java.util.Date;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.Rank;
import redis.clients.jedis.Jedis;

public class EpicCoinsManager {

	protected BukkitAPI	api;
	protected Promo		currentPromo;
	protected Date		promoNextCheck	= null;

	public EpicCoinsManager(final BukkitAPI api) {
		this.api = api;
	}

	public TextComponent getCreditMessage(final double amount) {
		return new TextComponent("§7Gain de §eCoins §7+§6" + String.format("%.2f", amount) + " ");
	}

	public TextComponent getCreditMessage(final double amount, final String reason) {
		final TextComponent gain = new TextComponent("§7Gain de §eCoins §7+§6" + String.format("%.2f", amount) + " ");
		gain.setColor(ChatColor.GOLD);
		gain.addExtra(new ComponentBuilder("(" + reason + ")").color(ChatColor.GOLD).create()[0]);

		return gain;
	}

	public TextComponent getCreditMessage(final double amount, final String reason, final Multiplier multiplier) {
		final TextComponent gain = new TextComponent("§7Gain de §eCoins §7+§6" + String.format("%.2f", amount) + " ");
		gain.setColor(ChatColor.GOLD);
		gain.addExtra(new ComponentBuilder("(" + reason + ") ").color(ChatColor.GOLD).create()[0]);

		if (multiplier != null)
			for (final String multCause : multiplier.infos.keySet()) {
				final TextComponent line = new TextComponent("§6(" + multCause + "§6) ");
				line.setColor(ChatColor.GOLD);
				gain.addExtra(line);
			}

		return gain;
	}

	public Multiplier getCurrentMultiplier(final String joueur) {
		final Date current = new Date();
		final Multiplier ret = new Multiplier();

		if (this.promoNextCheck == null || current.after(this.promoNextCheck)) {
			final Jedis jedis = this.api.getConnector().getResource();
			final String prom = jedis.get("epiccoins:currentpromo"); // On get
																		// la
																		// promo
			jedis.close();

			if (prom == null)
				this.currentPromo = null;
			else
				this.currentPromo = new Promo(prom);

			this.promoNextCheck = new Date();
			this.promoNextCheck.setTime(this.promoNextCheck.getTime() + 60 * 1000);
		}

		if (this.currentPromo != null && current.before(this.currentPromo.end)) {
			ret.globalAmount = this.currentPromo.multiply;
			ret.infos.put(this.currentPromo.message, this.currentPromo.multiply);
		}

		final PlayerData data = this.api.getPlayerDataManager().getPlayerData(joueur);
		final Rank rank = data.getPRank();

		double multiply = rank.getECMultiplier() + data.getBooster();

		multiply = multiply < 1 ? 1 : multiply;

		ret.globalAmount += multiply;
		return ret;
	}
}
