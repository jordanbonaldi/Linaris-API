package net.neferett.linaris.api;

import java.util.Map;
import java.util.stream.Collectors;

import net.neferett.linaris.BukkitAPI;
import redis.clients.jedis.Jedis;

public class PlayerBuy {

	public enum BuyItem {
		OTHER, TOKEN;
	}

	private int					amount;
	private String				date;
	private final BuyItem		i;
	private String				ip;
	private String				item;

	private Map<String, String>	map;

	private String				name;

	public PlayerBuy(final BuyItem i) {
		this.i = i;
		BukkitAPI.get().getTasksManager().addTask(() -> {
			final Jedis j = BukkitAPI.get().getConnector().getTokenResource();
			this.map = j.hgetAll(this.i == BuyItem.TOKEN ? "tokens" : "buys");
			j.close();
		});
	}

	public PlayerBuy(final BuyItem i, final String name, final int amount, final String ip, final String date) {
		this.i = i;
		this.name = name;
		this.ip = ip;
		this.date = date;
		this.amount = amount;
	}

	public PlayerBuy(final BuyItem i, final String name, final int amount, final String ip, final String date,
			final String item) {
		this(i, name, amount, ip, date);
		this.item = item;
	}

	public void addToRedis() {
		BukkitAPI.get().getTasksManager().addTask(() -> {
			final Jedis j = BukkitAPI.get().getConnector().getTokenResource();
			j.hset(this.i == BuyItem.TOKEN ? "tokens" : "buys", this.name + "@" + this.date,
					this.amount + "@" + this.ip + "@" + this.date + (this.item == null ? "" : "@" + this.item));
			j.close();
		});
	}

	public Map<String, String> getByAmount(final int amount) {
		return this.map.entrySet().stream()
				.filter((k) -> k.getValue().split("@")[0].equalsIgnoreCase(String.valueOf(amount)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, String> getByDate(final String date) {
		if (!(this.i == BuyItem.OTHER))
			return null;
		return this.map.entrySet().stream().filter((k) -> k.getValue().split("@")[2].equalsIgnoreCase(date))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, String> getByIP(final String ip) {
		return this.map.entrySet().stream()
				.filter((k) -> k.getValue().split("@")[1].equalsIgnoreCase(String.valueOf(this.amount)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, String> getByItem(final String item) {
		if (!(this.i == BuyItem.OTHER))
			return null;
		return this.map.entrySet().stream().filter((k) -> k.getValue().split("@")[3].equalsIgnoreCase(item))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, String> getByPlayer(final String name) {
		return this.map.entrySet().stream().filter((k) -> k.getKey().split("@")[0].equalsIgnoreCase(name))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
