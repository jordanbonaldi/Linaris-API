package net.neferett.linaris.api;

import java.util.concurrent.ConcurrentHashMap;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.moneys.EpicCoinsManager;
import net.neferett.linaris.api.moneys.LegendaryCoinsManager;

public class PlayerDataManager {

	protected ConcurrentHashMap<String, PlayerData> cachedData = new ConcurrentHashMap<>();
	private final BukkitAPI BukkitAPI;
	private EpicCoinsManager coinsManager;
	private LegendaryCoinsManager legendaryCoinsManager;
	
	public EpicCoinsManager getCoinsManager() {
		return coinsManager;
	}
	
	public LegendaryCoinsManager getLegendaryCoinsManager() {
		return legendaryCoinsManager;
	}

	public PlayerDataManager(BukkitAPI BukkitAPI) {
		this.BukkitAPI = BukkitAPI;
		this.coinsManager = new EpicCoinsManager(BukkitAPI);
		this.legendaryCoinsManager = new LegendaryCoinsManager(BukkitAPI);
	}

	public PlayerData getPlayerData(String player) {
		return getPlayerData(player, false);
	}
	
	public PlayerData getPlayerData(String player, boolean forceRefresh) {
		player = player.toLowerCase();
		if (!cachedData.containsKey(player)) {
			PlayerData data = new PlayerData(player, this, BukkitAPI);
			cachedData.put(player, data);
			return data;
		}

		PlayerData data = cachedData.get(player);

		if (forceRefresh) {
			data.updateData();
			return data;
		}

		data.refreshIfNeeded();
		return data;
	}

	public void update(String player) {
		player = player.toLowerCase();
		if (!cachedData.containsKey(player)) {
			PlayerData data = new PlayerData(player, this, BukkitAPI);
			cachedData.put(player, data);
			return;
		}

		PlayerData data = cachedData.get(player);
		data.updateData();
	}

	public void unload(String player) {
		player = player.toLowerCase();
		cachedData.remove(player);
	}
}
