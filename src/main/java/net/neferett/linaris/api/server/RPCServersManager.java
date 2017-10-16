package net.neferett.linaris.api.server;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.gson.Gson;

import net.neferett.linaris.events.MultiConnectionEvent;
import net.neferett.linaris.events.SoloConnectionEvent;
import net.neferett.linaris.rabbitmq.utils.rpc.RabbitMQRCPServer;
import net.neferett.linaris.utils.json.JSONObject;

public class RPCServersManager extends RabbitMQRCPServer{

	public RPCServersManager(String name) throws Exception {
		super("gcrequest-" + name);
	}

	@Override
	public JSONObject onMessage(JSONObject message) throws Exception {
		
		String type = message.getString("type");
		
		JSONObject callback = new JSONObject();
	
		if (type.equals("soloconnect")) {
			SoloConnection connection = new Gson().fromJson(message.getJSONObject("request").toString(), SoloConnection.class);
			SoloConnectionEvent event = new SoloConnectionEvent(connection.getPlayer(), connection.getRank(), connection.getArgs());
			Bukkit.getPluginManager().callEvent(event);
			ConnectionServerRespond respond = new ConnectionServerRespond(event.getStatus(), event.getMessage(), event.getErrorID());
			callback = new JSONObject(new Gson().toJson(respond));
		} else {
			MultiConnection connection = new Gson().fromJson(message.getJSONObject("request").toString(), MultiConnection.class);
			MultiConnectionEvent event = new MultiConnectionEvent(connection.getParty(), connection.getPlayers(), connection.getRanks(), connection.getArgs());
			Bukkit.getPluginManager().callEvent(event);
			ConnectionServerRespond respond = new ConnectionServerRespond(event.getStatus(), event.getMessage(), event.getErrorID());
			callback = new JSONObject(new Gson().toJson(respond));
		}
		
		System.out.println("JSON: " + callback.toString());
		return callback;
	}
	
	class MultiConnection {

		String party;
		String[] players;
		int[] ranks;
		String[] args;
		public MultiConnection(UUID uuid, String[] players, int[] ranks, String[] args) {
			this.party = uuid.toString();
			this.players = players;
			this.ranks = ranks;
			this.args = args;
		}

		public UUID getParty() {
			return UUID.fromString(party);
		}
		
		public String[] getPlayers() {
			return players;
		}

		public int[] getRanks() {
			return ranks;
		}
		
		public String[] getArgs() {
			return args;
		}

		public String getJSON() {
			return new Gson().toJson(this);
		}
	}
	
	class SoloConnection {

		String player;
		int rank;
		String[] args;

		public SoloConnection(String player, int rank, String[] args) {
			this.player = player;
			this.rank = rank;
			this.args = args;
		}

		public String getPlayer() {
			return player;
		}

		public int getRank() {
			return rank;
		}
		
		public String[] getArgs() {
			return args;
		}

		public String getJSON() {
			return new Gson().toJson(this);
		}
	}

}
