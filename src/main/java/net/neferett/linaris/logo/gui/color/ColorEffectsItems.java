package net.neferett.linaris.logo.gui.color;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.inventory.InstantShopItem;

public class ColorEffectsItems extends InstantShopItem {

	private final String		color;

	private final String		colorname;
	private final String		logo;
	private final String		name;
	private final PlayerData	pd;

	public ColorEffectsItems(final String colorname, final String color, final int price, final ItemStack item,
			final PlayerData pd, final String name) {
		super("color-effects-" + colorname, "§7Effet §e" + color + colorname, PriceType.TOKEN, price, null, item);
		if (!pd.contains("color-effects-Défaut"))
			pd.set("color-effects-Défaut", colorname);
		this.color = color;
		this.colorname = colorname;
		this.pd = pd;
		this.name = name;
		this.logo = !this.pd.contains("logo") || this.pd.get("logo").equals("d") ? "✪" : this.pd.get("logo");
		this.setDescription(this.loadPreview());
	}

	@Override
	public boolean alreadyHave(final PlayerData pd) {
		return pd.contains("color-effects-" + this.colorname);
	}

	public List<String> loadPreview() {
		if (this.pd.getRank().getVipLevel() == 3)
			return Arrays
					.asList("",
							"§7Aperçu§f:", this.pd.getRank().getRealPrefix().contains("%c")
									? this.pd.getRank().getRealPrefix()
											.replace(
													"%c", !this.pd
															.contains(
																	"logocolor")
																			? "§b" + this.color
																			: (this.pd.get("logocolor").length() > 2
																					? this.pd.get("logocolor")
																							.substring(0,
																									this.pd.get(
																											"logocolor")
																											.length()
																											- 2)
																					: this.pd.get("logocolor"))
																					+ this.color)
											+ this.name
									: "§cAucun aperçu disponible");
		else
			return Arrays
					.asList("", "§7Aperçu§f:",
							this.pd.getRank().getRealPrefix().contains("%c")
									&& this.pd.getRank().getRealPrefix()
											.contains("%s")
													? this.pd.getRank().getRealPrefix()
															.replace("%c",
																	!this.pd.contains("logocolor") ? "§e" + this.color
																			: (this.pd.get("logocolor").length() > 2
																					? this.pd.get("logocolor")
																							.substring(0,
																									this.pd.get(
																											"logocolor")
																											.length()
																											- 2)
																					: this.pd.get("logocolor"))
																					+ this.color)
															.replace("%s",
																	this.colorname.contains("Magic") ? "d" : this.logo)
															+ this.name
													: "§cAucun aperçu disponible");
	}

	@Override
	public void onBuy(final PlayerData pd) {
		pd.set("color-effects-" + this.colorname, this.color);
		if (this.colorname.contains("Magic"))
			pd.set("logo", "d");
		else if (this.pd.getRank().getVipLevel() > 3)
			pd.set("logo", this.logo);
		pd.set("logocolor",
				!pd.contains("logocolor") ? "§b"
						: (pd.get("logocolor").length() > 2
								? pd.get("logocolor").substring(0, pd.get("logocolor").length() - 2)
								: pd.get("logocolor")) + this.color);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

	@Override
	public void onUse(final PlayerData pd) {
		Bukkit.getPlayer(this.name).sendMessage("§7Effet changé en " + this.color + this.colorname);
		if (this.colorname.contains("Magic"))
			pd.set("logo", "d");
		else if (this.pd.getRank().getVipLevel() > 3)
			pd.set("logo", this.logo);
		pd.set("logocolor",
				!pd.contains("logocolor") ? "§b"
						: (pd.get("logocolor").length() > 2
								? pd.get("logocolor").substring(0, pd.get("logocolor").length() - 2)
								: pd.get("logocolor")) + this.color);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

}
