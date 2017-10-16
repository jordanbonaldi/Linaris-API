package net.neferett.linaris.api;

import java.util.ArrayList;

import net.neferett.linaris.BukkitAPI;
import redis.clients.jedis.Jedis;

public class FriendsManagement {

	protected BukkitAPI plugin;

	public FriendsManagement(BukkitAPI plugin) {
		this.plugin = plugin;
	}


	public boolean isFriend(String from, String isFriend) {
		return StringFriendList(from).contains(isFriend);
	}

	public ArrayList<String> friendList(String asking) {
		ArrayList<String> playerNames = new ArrayList<>();

		for (String id : StringFriendList(asking)) {
			playerNames.add(id);
		}
		return playerNames;
	}

	public ArrayList<String> StringFriendList(String asking) {
		ArrayList<String> playerIDs = new ArrayList<>();

		Jedis jedis = plugin.getConnector().getResource();
		for (String data : jedis.lrange("friends:" + asking, 0, -1)) {
			if (data == null || data.equals("")) {
				jedis.lrem("friends:" + asking, 0, data);
				continue;
			}

			String id = data;
			playerIDs.add(id);

		}
		jedis.close();
		return playerIDs;
	}

	public ArrayList<String> requestsList(String asking) {
		String dbKey = "friendrequest:*:" + asking;
		ArrayList<String> playerNames = new ArrayList<>();

		Jedis jedis = plugin.getConnector().getResource();
		for (String data : jedis.keys(dbKey)) {
			String[] parts = data.split(":");

			String id = parts[1];
			playerNames.add(id);

		}
		jedis.close();
		return playerNames;
	}

	public ArrayList<String> sentRequestsList(String asking) {
		String dbKey = "friendrequest:" + asking + ":";
		ArrayList<String> playerNames = new ArrayList<>();

		Jedis jedis = plugin.getConnector().getResource();
		for (String data : jedis.keys(dbKey)) {
			String[] parts = data.split(":");
			String id = parts[1];
			playerNames.add(id);

		}
		jedis.close();
		return playerNames;
	}

}
