package net.neferett.linaris.commands;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;

public class LagCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.addAttachment(BukkitAPI.get(), "bukkit.command.tps", true, 1);
			player.sendMessage("§e-----------------------------------------------------");
			player.sendMessage("§bServeur: §a" + BukkitAPI.get().getServerInfos().getServerName() + " §7 §e"
					+ new Date().toString());
			player.sendMessage("§7Le lag indique le temps de réponse avec le serveur");
			final int ping = ((CraftPlayer) player).getHandle().ping;
			String prefix = "§c";
			if (ping <= 100)
				prefix = "§a";
			else if (ping > 100 && ping < 500)
				prefix = "§e";
			player.sendMessage("§6Latence avec le serveur " + prefix + ping + " §6ms");
			player.sendMessage("§7Les TPS indiquent si le serveur est surcharg§, §a20 = §bparfait");
			player.getServer().dispatchCommand(player, "tps");
			player.sendMessage("§e-----------------------------------------------------");

		}
		return false;
	}

}
