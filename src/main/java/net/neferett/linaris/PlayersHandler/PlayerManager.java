package net.neferett.linaris.PlayersHandler;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

public class PlayerManager {

	static PlayerManager instance;

	public static PlayerManager get() {
		return instance;
	}

	HashMap<String, Players> players = new HashMap<>();

	public PlayerManager() {
		instance = this;
	}

	public List<Players> getModerator() {
		return this.players.values().stream().filter(p -> p.getPlayerData().getRank().getModerationLevel() > 1)
				.collect(Collectors.toList());
	}

	public Players getPlayer(final Player p) {
		if (this.players.containsKey(p.getName().toLowerCase()))
			return this.players.get(p.getName().toLowerCase());
		else {
			this.players.put(p.getName().toLowerCase(), new Players(p));
			return this.players.get(p.getName().toLowerCase());
		}
	}

	public HashMap<String, Players> getPlayers() {
		return this.players;
	}

	public void removePlayer(final Player p) {
		if (this.players.containsKey(p.getName().toLowerCase()))
			this.players.remove(p.getName().toLowerCase());
	}

}
