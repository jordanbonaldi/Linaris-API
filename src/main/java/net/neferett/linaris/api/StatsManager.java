package net.neferett.linaris.api;

import java.util.HashMap;
import java.util.Set;

import net.neferett.linaris.BukkitAPI;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class StatsManager {

	BukkitAPI api;

	public StatsManager(final BukkitAPI api) {
		this.api = api;
	}

	public HashMap<String, Double> getStatsValue(final Games game, final String stat, final int topNumber) {
		final HashMap<String, Double> scores = new HashMap<>();

		final Jedis j = this.api.getConnector().getResource();
		final Set<Tuple> players = j.zrevrangeWithScores("gamestats:" + game.getID() + ":" + stat, 0, topNumber);
		players.forEach((p) -> {
			scores.put(p.getElement(), p.getScore());
		});
		j.close();

		return scores;
	}

	public HashMap<String, Double> getStatsValueDec(final Games game, final String stat, final int topNumber) {
		final HashMap<String, Double> scores = new HashMap<>();

		final Jedis j = this.api.getConnector().getResource();
		final Set<Tuple> players = j.zrangeWithScores("gamestats:" + game.getID() + ":" + stat, 0, topNumber);
		players.forEach((p) -> {
			scores.put(p.getElement(), p.getScore());
		});
		j.close();

		return scores;
	}

	public double getStatValue(final String player, final Games game, final String stat) {

		final Jedis j = this.api.getConnector().getResource();
		final double value = j.zscore("gamestats:" + game.getID() + ":" + stat, player);
		j.close();

		return value;
	}

	public void increase(final String player, final Games game, final String stat, final double amount) {
		this.api.getServer().getScheduler().runTaskAsynchronously(this.api, () -> {
			final Jedis j = this.api.getConnector().getResource();
			j.zincrby("gamestats:" + game.getID() + ":" + stat, amount, player);
			j.close();
		});
	}

	public void setValue(final String player, final Games game, final String stat, final double value) {
		this.api.getServer().getScheduler().runTaskAsynchronously(this.api, () -> {
			final Jedis j = this.api.getConnector().getResource();
			j.zadd("gamestats:" + game.getID() + ":" + stat, value, player);
			j.close();
		});
	}
}
