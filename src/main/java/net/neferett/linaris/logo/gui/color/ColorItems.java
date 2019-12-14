package net.neferett.linaris.logo.gui.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.inventory.InstantShopItem;
import net.neferett.linaris.utils.GetHead;

public class ColorItems extends InstantShopItem {

	private final String		color;

	private final String		colorname;
	private final String		logo;
	private final String		name;
	private final PlayerData	pd;

	public ColorItems(final String colorname, final String color, final int price, final GetHead head,
			final PlayerData pd, final String name) {
		super("color-" + colorname, "§7Couleur " + color + colorname, PriceType.TOKEN, price, null, head.getItem());
		this.color = pd.contains("logocolor") && pd.get("logocolor").length() > 2
				? color + pd.get("logocolor").substring(2) : color;
		this.colorname = colorname;
		this.pd = pd;
		this.name = name;
		this.logo = !this.pd.contains("logo") || this.pd.get("logo").equals("d") ? "✪" : this.pd.get("logo");
		this.setDescription(this.loadPreview());
	}

	@Override
	public boolean alreadyHave(final PlayerData pd) {
		return pd.contains("color-" + this.colorname);
	}

	private List<String> loadPreview() {
		String prefix = this.pd.getRank().getPrefix().replace("%c", this.color).replace("%s",
				this.color.contains("k") ? "d" : this.logo);

		if (prefix.charAt(prefix.length() - 2) == 'k')
			prefix = prefix.substring(0, prefix.length() - 3) + " ";

		return Arrays.asList("", "§7Aperçu§f:", prefix + this.name);
	}

	@Override
	public void onBuy(final PlayerData pd) {
		pd.set("color-" + this.colorname, this.color);
		pd.set("logocolor", this.color);
		if (this.pd.getRank().getVipLevel() > 3)
			pd.set("logo", this.logo);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

	@Override
	public void onUse(final PlayerData pd) {
		Bukkit.getPlayer(this.name).sendMessage("§7Couleur changé en " + this.color + this.colorname);
		pd.set("logocolor", this.color);
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(this.name).setPrefix(pd.getRank().getTablist(this.pd));
	}

}
