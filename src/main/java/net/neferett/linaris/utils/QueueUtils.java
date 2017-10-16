package net.neferett.linaris.utils;

import org.bukkit.entity.Player;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.rabbitmq.messaging.RabbitMQMessagingClient;
import net.neferett.linaris.utils.json.JSONObject;

public class QueueUtils {

	public static void addInQueue(Player p, Games game) {
		addInQueue(p, game, null);
	}
	
	public static void addInQueue(Player p, Games game, String map) {
		try {
			JSONObject json = new JSONObject();
			json.put("player", p.getName().toLowerCase());
			json.put("type", "add");
			json.put("game", game.getID());
			if (map != null && !map.isEmpty())
				json.put("map", map);
			new RabbitMQMessagingClient("gamequeues", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeFromQueue(Player p) {
		try {
			JSONObject json = new JSONObject();
			json.put("player", p.getName().toLowerCase());
			json.put("type", "remove");
			new RabbitMQMessagingClient("gamequeues", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
