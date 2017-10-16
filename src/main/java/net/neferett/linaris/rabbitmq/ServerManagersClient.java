package net.neferett.linaris.rabbitmq;

import org.bukkit.Bukkit;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.ServerInfo;
import net.neferett.linaris.rabbitmq.utils.rpc.RabbitMQRCPClient;
import net.neferett.linaris.utils.ServerUtils;
import net.neferett.linaris.utils.json.JSONObject;

public class ServerManagersClient extends RabbitMQRCPClient {

	public ServerManagersClient() throws Exception {
		
		BukkitAPI api = BukkitAPI.get();
		
		
		JSONObject json = new JSONObject();
		json.put("gameName", api.getServerInfos().getGameName());
		json.put("ip", BukkitAPI.ip);
		json.put("port", Bukkit.getServer().getPort());
		
		setRequestQueueName("serverm");
		setMessage(json);
		
		send();
		
		ServerInfo infos = api.getServerInfos();
		infos.setServerName(getCallback().getString("serverName"));
		ServerUtils.changeServerName(infos.getServerName());
	}

}
