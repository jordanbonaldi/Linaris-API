package net.neferett.linaris.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.PlayerLocalManager;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.mistery.MysteryItem;
import net.neferett.linaris.mistery.MysteryItemsManager;

public class VanishCommand extends CommandHandler {

	public static void setInvisible(final Player p) {
		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName());
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(p.getName());

		BukkitAPI.get().getTasksManager().addTask(() -> {

			final String petID = SettingsManager.getSetting(p.getPlayer().getName(), Games.LOBBY, "pet");
			if (petID != null) {
				final MysteryItem item = MysteryItemsManager.getInstance().getMysteryItem(petID);
				if (item != null)
					item.onRemove(p.getPlayer());
			}

		});
		p.setGameMode(GameMode.SPECTATOR);
		BukkitAPI.get().getPlayerLocalManager().unload(p.getPlayer().getName());
		pd.setBoolean("invisible", true);
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (!player.equals(p.getPlayer()))
				player.hidePlayer(p.getPlayer());
		});
	}

	public VanishCommand() {
		super("vanish", p -> p.getRank().getModerationLevel() > 1, "v");
		this.setErrorMsg("§cCette commande est réservé au §eStaff §c!");
	}

	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {
		if (!player.getPlayerData().contains("invisible"))
			setInvisible(player.getPlayer());
		else if (player.getPlayerData().getBoolean("invisible"))
			this.setVisible(player);
		else if (!player.getPlayerData().getBoolean("invisible"))
			setInvisible(player.getPlayer());
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

	private void setVisible(final Players p) {
		setInvisible(p.getPlayer());
		p.setGameMode(GameMode.SURVIVAL);
		BukkitAPI.get().getTasksManager().addTask(() -> {

			final String petID = SettingsManager.getSetting(p.getPlayer().getName(), Games.LOBBY, "pet");
			if (petID != null) {
				final MysteryItem item = MysteryItemsManager.getInstance().getMysteryItem(petID);
				if (item != null)
					item.onUse(p.getPlayer(), false);
			}

		});
		PlayerLocalManager.get().getPlayerLocal(p.getName()).inits();
		BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(p.getPlayer().getName())
				.setPrefix(p.getPlayerData().getRank().getPrefix(p.getPlayerData()));
		p.getPlayerData().setBoolean("invisible", false);
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (!player.equals(p.getPlayer()))
				player.showPlayer(p.getPlayer());
		});
	}

}
