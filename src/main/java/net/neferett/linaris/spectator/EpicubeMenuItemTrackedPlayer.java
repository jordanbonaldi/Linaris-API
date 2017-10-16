package net.neferett.linaris.spectator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.specialitems.SpecialItem;

public class EpicubeMenuItemTrackedPlayer extends MenuItem {
	String playerName;

	public EpicubeMenuItemTrackedPlayer(final Player player) {
		super(getDisplayName(player), buildHead(player), new String[0]);
		this.playerName = player.getName();
		SpecialItem.registerItem(this);
	}

	private static String getDisplayName(final Player player) {
		String displayName = player.getName();
		if (SpectatorManager.displayTeam) {
			final Scoreboard board = player.getScoreboard();
			if (board != null) {
				@SuppressWarnings("deprecation")
				final Team team = board.getPlayerTeam(player);
				if (team != null) {
					final String prefix = (team.getPrefix() != null) ? team.getPrefix() : "";
					final String suffix = (team.getSuffix() != null) ? team.getSuffix() : "";
					displayName = prefix + displayName + suffix;
				}
			}
		}
		return displayName;
	}

	private static ItemStack buildHead(final Player player) {
		final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		final SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwner(player.getName());
		head.setItemMeta(meta);
		return head;
	}

	@Override
	public void inventoryClickEvent(final Player player) {
		final Player teleport = Bukkit.getPlayer(this.playerName);
		if (teleport == null) {
			player.sendMessage("§7Ce joueur n'est plus en ligne !");
			player.closeInventory();
			return;
		}
		if (!SpectatorManager.isTrackable(teleport)) {
			player.sendMessage("§7Ce joueur n'est plus dans en vie !");
			player.closeInventory();
			return;
		}
		player.teleport(teleport);
	}
}
