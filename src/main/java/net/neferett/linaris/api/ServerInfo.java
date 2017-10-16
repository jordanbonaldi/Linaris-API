package net.neferett.linaris.api;

import net.neferett.linaris.BukkitAPI;

public class ServerInfo {

	String serverName;
	String gameName;
	String mapName;
	boolean canJoin = false;
	boolean canSee = false;
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public boolean canJoin() {
		return this.canJoin;
	}
	
	public boolean canSee() {
		return this.canSee;
	}
	
	public void setCanSee(boolean canSee,boolean update) {
		this.canSee = canSee;
		update();
	}
	
	public void setCanJoin(boolean canJoin,boolean update) {
		this.canJoin = canJoin;
		update();
	}
	
	public void update() {
		BukkitAPI.get().heartbeat();
	}
	
}
