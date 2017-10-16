package net.neferett.linaris.logo.gui.logo;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.inventory.InstantShopItem;
import net.neferett.linaris.utils.ItemBuilder;

public class LogoItems extends InstantShopItem {

	private final String		color;

	private final String		logo;
	private final String		logoname;
	private final String		name;
	private final PlayerData	pd;

	public LogoItems(final String logoname, final String logo, final int price, final PlayerData pd,
			final ItemBuilder item, final String name) {
		super("logo-" + logoname, "§7Logo " + (pd.contains("logocolor") ? pd.get("logocolor") : "§e") + logo,
				PriceType.TOKEN, price, null, item.build());
		this.pd = pd;
		this.color = pd.contains("logocolor") ? pd.get("logocolor") : "§e";
		this.logoname = logoname;
		this.logo = logo;
		this.name = name;
		this.setDescription(this.loadPreview());
	}

	@Override
	public boolean alreadyHave(final PlayerData pd) {
		return pd.contains("logo-" + this.logoname);
	}

	public List<String> loadPreview() {
		return Arrays.asList("", "§7Aperçu§f:",
				this.pd.getRank().getRealPrefix().contains("%c") && this.pd.getRank().getRealPrefix().contains("%s")
						? this.pd.getRank().getRealPrefix().replace("%c", this.color).replace("%s",
								this.color.contains("k") ? "d" : this.logo) + this.name
						: "§cAucun aperçu disponible");
	}

	@Override
	public void onBuy(final PlayerData pd) {
		pd.set("logo-" + this.logoname, this.logoname);
		pd.set("logo", this.logo);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

	@Override
	public void onUse(final PlayerData pd) {
		Bukkit.getPlayer(this.name).sendMessage("§7Logo changé en " + this.color + this.logo);
		pd.set("logo", this.logo);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

}
