package net.neferett.linaris.listeners;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.API;
import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.commands.VanishCommand;
import net.neferett.linaris.events.SoloConnectionEvent;
import net.neferett.linaris.mistery.MysteryItem;
import net.neferett.linaris.mistery.MysteryItemsManager;
import net.neferett.linaris.utils.ScoreboardSign;
import net.neferett.linaris.utils.TabListUtils;
import net.neferett.linaris.utils.file;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class PlayerConnectionListener implements Listener {
	static String	footer	= "§6Notre site§f: §bhttp://linaris.fr/ §f- §6Notre Twitter§f: §c@LinarisMC \n §6Boutique sur§f: §b§nhttp://linaris.fr/store/";
	static String	header	= "§eVous êtes connecté sur §aplay.linaris.fr \n §6Rejoignez nous sur TeamSpeak§f: §bts.linaris.fr";

	@EventHandler
	public void ConnectionSpec(final SoloConnectionEvent e) {
		if (e.getArgs().length == 2 && e.getArgs()[0].equals("invisible") && e.getArgs()[1] != null
				&& Bukkit.getPlayer(e.getArgs()[1]) != null)
			TaskManager.runTaskLater(() -> {
				Bukkit.getPlayer(e.getPlayer()).teleport(Bukkit.getPlayer(e.getArgs()[1]));
			}, 20 * 3);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {

		final Player player = event.getPlayer();
		file.getFile();
		TabListUtils.sendTablist(player, header, footer);

		event.setJoinMessage(null);

		if (!player.getAddress().getAddress().getHostAddress().equals("149.202.65.5")) {
			event.getPlayer().kickPlayer("§cMerci de vous connecter avec play.linaris.fr !");
			return;
		}

		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(player.getName());

		if (pd.contains("invisible") && pd.getBoolean("invisible")) {
			VanishCommand.setInvisible(player);
			return;
		}

		if (BukkitAPI.get().isApi()) {
			if ((pd.getRank().getVipLevel() >= 4 || pd.getRank().getModerationLevel() >= 1)
					&& API.getInstance().isAnnonce())
				Bukkit.broadcastMessage(pd.getRank().getPrefix(pd) + player.getName() + " §6§oa rejoint !");
			BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(player.getName())
					.setPrefix(pd.getRank().getTablist(pd));
		}

		final Predicate<Player> plpred = p -> BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName())
				.contains("invisible")
				&& BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName()).getBoolean("invisible");

		Bukkit.getOnlinePlayers().stream().filter(plpred).collect(Collectors.toList()).forEach(p -> {
			VanishCommand.setInvisible(p);
		});

		if (BukkitAPI.get().getServerInfos().getServerName().startsWith("fb"))
			player.setGameMode(GameMode.CREATIVE);
		else
			player.setGameMode(GameMode.SURVIVAL);

		BukkitAPI.get().getTasksManager().addTask(() -> {

			final String petID = SettingsManager.getSetting(player.getName(), Games.LOBBY, "pet");
			if (petID != null) {
				final MysteryItem item = MysteryItemsManager.getInstance().getMysteryItem(petID);
				if (item != null)
					item.onUse(player, false);
			}

		});

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(final PlayerKickEvent e) {
		e.setLeaveMessage("");
		final ScoreboardSign bar = ScoreboardSign.get(e.getPlayer());
		if (bar != null)
			bar.destroy();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(final PlayerQuitEvent e) {
		e.setQuitMessage("");
		final ScoreboardSign bar = ScoreboardSign.get(e.getPlayer());
		if (bar != null)
			bar.destroy();
	}
}
