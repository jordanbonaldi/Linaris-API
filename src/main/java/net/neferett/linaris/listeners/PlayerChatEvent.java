package net.neferett.linaris.listeners;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.commands.CommandHandler;

public class PlayerChatEvent implements Listener {

	@EventHandler
	public void onPlayerChatEvent(final AsyncPlayerChatEvent e) {
		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(e.getPlayer().getName());
		if (pd.contains("invisible") && pd.getBoolean("invisible")) {
			e.getPlayer().sendMessage("§cVous ne pouvez pas parler en étant invisible !");
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerCommandEvent(final PlayerCommandPreprocessEvent e) {

		final Players p = PlayerManager.get().getPlayer(e.getPlayer());
		final String cmd = e.getMessage().toLowerCase().substring(1);
		String cmdname = cmd;

		if (cmd.split(" ").length > 1)
			cmdname = cmd.split(" ")[0];
		final CommandHandler cmdhandler = CommandHandler.getCmdByName(cmdname);

		if (cmdhandler == null)
			return;

		if (BukkitAPI.get().getCommandProcess().stream().filter(pred -> pred.test(e) == false)
				.collect(Collectors.toList()).size() != 0) {
			e.setCancelled(true);
			return;
		}
		p.setCommandHandler(cmdhandler);
		if (!cmdhandler.getPredicateOnPLayerData()
				.test(BukkitAPI.get().getPlayerDataManager().getPlayerData(e.getPlayer().getName()))) {
			p.setCommandHandler(cmdhandler);
			cmdhandler.onError(p);
			e.setCancelled(true);
			return;
		}
		cmdhandler.cmd(p, cmdname, Arrays.asList(cmd.split(" ")));
		e.setCancelled(true);
	}

}
