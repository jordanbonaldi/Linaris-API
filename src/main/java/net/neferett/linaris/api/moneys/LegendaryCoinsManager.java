package net.neferett.linaris.api.moneys;

import java.util.Date;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.Rank;
import redis.clients.jedis.Jedis;

public class LegendaryCoinsManager {

	protected BukkitAPI	api;
	protected Promo		currentPromo;
	protected Date		promoNextCheck	= null;

	public LegendaryCoinsManager(final BukkitAPI api) {
		this.api = api;
	}

	public TextComponent getCreditMessage(final double amount) {
		return new TextComponent("§7Gain de §bCrédits §7+§6" + String.format("%.2f", amount) + " ");
	}

	public TextComponent getCreditMessage(final double amount, final String reason) {
		final TextComponent gain = this.getCreditMessage(amount);
		final TextComponent rComponent = new TextComponent(" (" + reason + ") ");
		rComponent.setColor(ChatColor.GOLD);
		gain.addExtra(rComponent);
		return gain;
	}

	public TextComponent getCreditMessage(final double amount, final String reason, final Multiplier multiplier) {
		final TextComponent gain = this.getCreditMessage(amount, reason);

		if (multiplier != null)
			for (final String multCause : multiplier.infos.keySet()) {
				final TextComponent line = new TextComponent(multCause);
				line.setColor(ChatColor.GOLD);
				final TextComponent details = new TextComponent(" *" + multiplier.infos.get(multCause));
				details.setColor(ChatColor.AQUA);
				line.addExtra(details);

				final TextComponent toAdd = new TextComponent(" [");
				toAdd.setColor(ChatColor.GOLD);
				toAdd.addExtra(line);
				toAdd.addExtra(ChatColor.GOLD + "]");

				gain.addExtra(line);
			}

		return gain;
	}

	public Multiplier getCurrentMultiplier(final String joueur) {
		final Date current = new Date();
		final Multiplier ret = new Multiplier();

		if (this.promoNextCheck == null || current.after(this.promoNextCheck)) {
			final Jedis jedis = this.api.getConnector().getResource();
			final String prom = jedis.get("legendarycoins:currentpromo"); // On
																			// get
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

		final Rank rank = this.api.getPlayerDataManager().getPlayerData(joueur).getPRank();

		double multiply = rank.getLCMultiplier();

		multiply = multiply < 1 ? 1 : multiply;

		ret.globalAmount += multiply;
		return ret;
	}
}
