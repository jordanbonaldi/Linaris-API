package net.neferett.linaris.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.neferett.linaris.rabbitmq.effects.PlayerEffect;
import net.neferett.linaris.rabbitmq.effects.SoundEffect;
import net.neferett.linaris.rabbitmq.messaging.RabbitMQMessagingServer;
import net.neferett.linaris.utils.json.JSONArray;
import net.neferett.linaris.utils.json.JSONObject;

public class PlayerEffectMessaging extends RabbitMQMessagingServer {

	List<PlayerEffect> effects;
	
	public PlayerEffectMessaging() throws Exception {
		super("playereffects");
		this.effects = new ArrayList<PlayerEffect>();
		this.effects.add(new SoundEffect());
	}

	@Override
	public void onMessage(JSONObject message) throws Exception {

		String type = message.getString("type");
		
		Player player = Bukkit.getPlayer(message.getString("player"));
		if (player == null) return;
		
		JSONArray arr = message.getJSONArray("args");
		List<Object> list = new ArrayList<Object>();
		for(int i = 0; i < arr.length(); i++){
		    list.add(arr.get(i));
		}
		
		for (PlayerEffect effect : effects) {
			if (effect.getName().equals(type)) {
				effect.use(player, list.stream().toArray(Object[]::new));
				return;
			}
		}
		
	}

}
