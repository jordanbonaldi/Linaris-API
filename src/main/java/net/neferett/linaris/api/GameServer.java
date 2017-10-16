package net.neferett.linaris.api;

import org.bukkit.entity.Player;

import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public class GameServer implements Comparable<GameServer> {

	private String servName;
	private String gameName;
	private String mapName;
	private int players;
	private int maxPlayers;
	
	private boolean canJoin;
	private boolean canSee;
	
	public GameServer(String servName,String gameName, String mapName,int players,int maxPlayers,boolean canJoin,boolean canSee) {
		this.servName = servName;
		this.gameName = gameName;
		this.mapName = mapName;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.canJoin = canJoin;
		this.canSee = canSee;
	}
	
	public String getServName() {
		return servName;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getPlayers() {
		return players;
	}
	
	public boolean canJoin() { return canJoin; }
	public boolean canSee() { return canSee; }
	
	public void setCanJoin(boolean canJoin) {
		this.canJoin = canJoin;
	}

	public void setCanSee(boolean canSee) {
		this.canSee = canSee;
	}
	
	public void setPlayers(int players) {
		this.players = players;
	}
	
	public void connectPlayer(Player p ) {
		
	}
	
	@Override
	public String toString() {
		try {
			JSONObject object = new JSONObject();
			object.put("servName", this.servName);
			object.put("gameName", this.gameName);
			object.put("mapName", this.mapName);
			object.put("players", this.players);
			object.put("maxPlayers", this.maxPlayers);
			object.put("canJoin", this.canJoin);
			object.put("canSee", this.canSee);
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject toJSON(String type) {
		if (this.servName == null) return null;
		try {
			JSONObject object = new JSONObject();
			object.put("type", type);
			object.put("servName", this.servName);
			object.put("gameName", this.gameName);
			object.put("mapName", this.mapName);
			object.put("players", this.players);
			object.put("maxPlayers", this.maxPlayers);
			object.put("canJoin", this.canJoin);
			object.put("canSee", this.canSee);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(GameServer o) {
		return getPlayers() - o.getPlayers();
	}
}
