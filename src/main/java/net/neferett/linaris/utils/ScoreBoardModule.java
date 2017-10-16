package net.neferett.linaris.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;

public abstract class ScoreBoardModule implements Runnable {

	BukkitAPI game;

	public ScoreBoardModule(final BukkitAPI game) {
		this.game = game;
		game.getServer().getScheduler().scheduleSyncRepeatingTask(game, this, 0, 20);
	}

	public BukkitAPI getGame() {
		return this.game;
	}

	public abstract void onUpdate();

	public abstract void onUpdate(Player p);

	@Override
	public void run() {
		this.update();
	}

	public void update() {
		this.onUpdate();
		for (final Player p : Bukkit.getOnlinePlayers())
			this.onUpdate(p);
	}

}
