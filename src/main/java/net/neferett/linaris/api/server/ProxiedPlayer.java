package net.neferett.linaris.api.server;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.PlayerUtils;


public class ProxiedPlayer {

	private final String playerId;

	public ProxiedPlayer(String playerId) {
		this.playerId = playerId;
	}

	
	public String getServer() {
		return BukkitAPI.get().getPlayerDataManager().getPlayerData(playerId).get("currentserver", "Inconnu");
	}
//	
//	public String getProxy() {
//		return BukkitAPI.get().getPlayerManager().getPlayerData(playerId).get("currentproxy", "Inconnu");
//	}
//
//	
//	public void disconnect(TextComponent reason) {
//		BukkitAPI.get().getPubSub().send("apiexec.kick", playerId + " " + new Gson().toJson(reason));
//	}

	
	public void connect(String server) {
		PlayerUtils.goToServer(playerId, server);
	}
	
//	public void sendMessage(TextComponent component) {
//		BukkitAPI.get().getPubSub().send("apiexec.message", playerId + " " + new Gson().toJson(component));
//	}
}
