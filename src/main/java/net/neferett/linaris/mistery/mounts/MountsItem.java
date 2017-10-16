package net.neferett.linaris.mistery.mounts;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.mistery.MysteryItem;

public class MountsItem extends MysteryItem {

	Class<? extends Mount> mount;

	public MountsItem(String id, String name, PriceType priceType, double price, ItemStack itemUI,
			Class<? extends Mount> mount) {
		super("mount-" + id, "§b" + name, priceType, price, "", itemUI);
		this.mount = mount;

	}

	@Override
	public void onUse(Player p, boolean save) {

		if (save) {
			SettingsManager.setSetting(p.getName(), Games.LOBBY, "mount", this.getID());
		}
		this.onRemove(p);

		try {
			final Constructor<?> ctor = this.mount.getConstructor(Player.class);
			ctor.newInstance(p);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onRemove(Player p) {
		SettingsManager.removeSetting(p.getName(), Games.LOBBY, "mount");
		p.leaveVehicle();
	}

}
