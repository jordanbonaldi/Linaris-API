package net.neferett.linaris.spectator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.inventory.VirtualInventory;
import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.utils.PlayerUtils;

public class EpicubeMenuItemSpectatorCompass extends MenuItem implements Listener {
	public static int id;

	public EpicubeMenuItemSpectatorCompass() {
		super("§bMenu Spectateur", new ItemStack(Material.COMPASS), new String[0]);
	}

	@Override
	public void inventoryClickEvent(final Player player) {
		if (BukkitAPI.get().getServerInfos().getServerName().contains("practice"))
			return;
		VirtualInventory.get(EpicubeVirtualMenuPlayerTracker.id).open(player);
		player.updateInventory();
		PlayerUtils.givePlayerBackToHubItem(player);
		SpectatorManager.giveSpectatorItem(player);
	}
}
