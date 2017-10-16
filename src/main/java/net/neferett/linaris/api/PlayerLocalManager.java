package net.neferett.linaris.api;

import java.util.concurrent.ConcurrentHashMap;

import net.neferett.linaris.BukkitAPI;

public class PlayerLocalManager {

	public static PlayerLocalManager get() {
		return BukkitAPI.get().getPlayerLocalManager();
	}

	protected ConcurrentHashMap<String, PlayerLocal> cachedData = new ConcurrentHashMap<>();

	public PlayerLocal getPlayerLocal(String player) {
		player = player.toLowerCase();
		if (!this.cachedData.containsKey(player)) {
			final PlayerLocal data = new PlayerLocal(player);
			this.cachedData.put(player, data);
			return data;
		}

		final PlayerLocal data = this.cachedData.get(player);

		return data;
	}

	public void unload(String player) {
		player = player.toLowerCase();
		final PlayerLocal pl = this.getPlayerLocal(player);
		pl.unload();
		this.cachedData.remove(player);
	}
}
