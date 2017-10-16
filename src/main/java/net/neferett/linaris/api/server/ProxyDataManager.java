package net.neferett.linaris.api.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.GameServer;
import net.neferett.linaris.api.Games;
import redis.clients.jedis.Jedis;

public class ProxyDataManager {

	private final BukkitAPI api;

	public ProxyDataManager(BukkitAPI api) {
		this.api = api;
	}
	
	public Set<String> getPlayersOnServer(String server) {
		Jedis jedis = api.getConnector().getBungeeResource();
		Set<String> data = jedis.smembers("connectedonserv:" + server);
		jedis.close();

		HashSet<String> ret = new HashSet<>();
		data.stream().forEach(str -> ret.add(str));

		return ret;
	}
	
	public ProxiedPlayer getProxiedPlayer(String uuid) {
		return new ProxiedPlayer(uuid);
	}
	
	public ConcurrentHashMap<String, GameServer> getServers() {
		Jedis jedis = api.getConnector().getBungeeResource();
		Map<String, String> servers = jedis.hgetAll("servers");
		jedis.close();

		ConcurrentHashMap<String, GameServer> gameServers = new ConcurrentHashMap<>();
		for (String server : servers.keySet()) {
			String[] ip = servers.get(server).split(":");
			if (ip.length == 8)
				gameServers.put(server, new GameServer(server, ip[2], ip[3] , Integer.valueOf(ip[5]), Integer.valueOf(ip[4]), Boolean.valueOf(ip[6]) , Boolean.valueOf(ip[7])));
		}
		return gameServers;
	}
	

	public LinkedList<GameServer> getServersByGameName(String string) {
		LinkedList<GameServer> servers = new LinkedList<GameServer>();
		getServers().values().stream().filter((g) -> g.getGameName().equals(string)).forEach((g) -> {
			servers.add(g);
		});
		return servers;
	}
	
	public LinkedList<GameServer> getServersByGameNameAndMap(String string,String map) {
		LinkedList<GameServer> servers = getServersByGameName(string);
		LinkedList<GameServer> mpservers = new LinkedList<GameServer>();
		servers.stream().filter((g) -> g.getMapName().equals(map)).forEach((g) -> {
			mpservers.add(g);
		});
		return mpservers;
	}
	
	public int getPlayerInGame(Games game) {
		List<GameServer> servers = getServersByGameName(game.getDisplayName());
		int nb = 0;
		if (!servers.isEmpty())
			for (GameServer s : servers)
				nb += s.getPlayers();
		return nb;
	}
	
	public int getPlayerInGameWithMap(Games game,String map) {
		List<GameServer> servers = getServersByGameNameAndMap(game.getDisplayName(),map);
		int nb = 0;
		if (!servers.isEmpty())
			for (GameServer s : servers)
				nb += s.getPlayers();
		return nb;
	}
}
