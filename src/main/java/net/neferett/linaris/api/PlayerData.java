package net.neferett.linaris.api;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.moneys.FinancialCallback;
import net.neferett.linaris.api.moneys.Multiplier;
import net.neferett.linaris.api.ranks.RankAPI;
import net.neferett.linaris.api.ranks.RankManager;
import redis.clients.jedis.Jedis;

public class PlayerData extends PlayerDataAbstract {

	private RankAPI					cacheRank	= null;
	private Date					lastRefresh	= null;
	private final PlayerDataManager	manager;
	public final String				playername;
	private final BukkitAPI			plugin;

	public PlayerData(final String player, final PlayerDataManager manager, final BukkitAPI bridge) {
		super(player);
		this.playername = player;
		this.manager = manager;
		this.plugin = bridge;
		this.updateData();
	}

	public void check() {
		if (this.isChecked())
			return;
		this.setBoolean("checked", true);
	}

	@Override
	public boolean contains(final String key) {
		this.refreshIfNeeded();
		return super.contains(key);
	}

	public void creditCoins(final double famount, final String reason, final boolean applyMultiplier,
			final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			double amount = famount;
			TextComponent message = null;
			if (applyMultiplier) {
				final Multiplier multiplier = this.manager.getCoinsManager().getCurrentMultiplier(this.playerID);
				amount += amount * multiplier.globalAmount;

				message = reason != null ? this.manager.getCoinsManager().getCreditMessage(amount, reason, multiplier)
						: this.manager.getCoinsManager().getCreditMessage(amount);
			} else
				message = reason != null ? this.manager.getCoinsManager().getCreditMessage(amount, reason)
						: this.manager.getCoinsManager().getCreditMessage(amount);

			this.displayMessage(message);

			final double result = this.increaseCoins(amount);

			if (financialCallback != null)
				financialCallback.done(result, amount, null);

		}, "CreditCoinsThread").start();
	}

	public void creditLegendaryCoins(final double famount, final String reason, final boolean applyMultiplier,
			final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			double amount = famount;
			TextComponent message = null;
			if (applyMultiplier) {
				final Multiplier multiplier = this.manager.getLegendaryCoinsManager()
						.getCurrentMultiplier(this.playerID);
				amount += amount * multiplier.globalAmount;

				message = reason != null
						? this.manager.getLegendaryCoinsManager().getCreditMessage(amount, reason, multiplier)
						: this.manager.getLegendaryCoinsManager().getCreditMessage(amount);
			} else
				message = reason != null ? this.manager.getLegendaryCoinsManager().getCreditMessage(amount, reason)
						: this.manager.getLegendaryCoinsManager().getCreditMessage(amount);

			this.displayMessage(message);

			final double result = this.increaseLC(amount);

			if (financialCallback != null)
				financialCallback.done(result, amount, null);

		}, "CreditStarsThread").start();
	}

	public double decreaseCoins(final double decrBy) {
		return this.increaseCoins(-decrBy);
	}

	public double decreaseLC(final double decrBy) {
		return this.increaseLC(-decrBy);
	}

	void displayMessage(final TextComponent message) {
		final Player player = Bukkit.getPlayer(this.playerID);
		if (player != null)
			player.sendMessage(message.toLegacyText());
	}

	@Override
	public String get(final String key) {
		this.refreshIfNeeded();
		return super.get(key);
	}

	public double getBooster() {
		if (!this.contains("booster"))
			return 0;
		return this.getDouble("booster");
	}

	public long getBoosterFinish() {
		if (this.contains("boosterFinish"))
			return this.getLong("boosterFinish");
		return 0;
	}

	public String getCurrentServer() {

		final String key = "currentserver:" + this.playerID + ":*";
		final Jedis jedis = this.plugin.getConnector().getResource();
		final Set<String> keys = jedis.keys(key);
		if (keys.isEmpty())
			return null;

		String server = null;

		final Iterator<String> servers = keys.iterator();

		while (servers.hasNext() && server != null)
			server = servers.next();

		if (server == null)
			return null;

		server = jedis.get(server);
		jedis.close();
		return server;
	}

	public double getEC() {
		if (!this.contains("coins"))
			this.setDouble("coins", 0);
		return this.getDouble("coins");
	}

	public RankAPI getGeneralRank() {
		if (!this.contains("rank"))
			this.set("rank", Integer.toString(0));
		return RankManager.getInstance().getRank(this.getInt("rank"));
	}

	@Override
	public Set<String> getKeys() {
		this.refreshIfNeeded();
		return super.getKeys();
	}

	public double getLC() {
		if (!this.contains("legendarycoins"))
			this.setDouble("legendarycoins", 0);
		return this.getDouble("legendarycoins");
	}

	public String getPlayername() {
		return this.playername;
	}

	public RankAPI getRank() {
		if (this.cacheRank == null)
			if (BukkitAPI.get().isApi() && API.getInstance() != null && API.getInstance().getHandleRank()
					&& this.getGeneralRank().getModerationLevel() < 1) {
				if (!this.contains(API.getInstance().getGame() + "-rank"))
					this.set(API.getInstance().getGame() + "-rank", "100");
				this.cacheRank = RankManager.getInstance().getRank(this.getInt(API.getInstance().getGame() + "-rank"));
			} else
				this.cacheRank = this.getGeneralRank();
		return this.cacheRank;
	}

	public long getRankFinish() {
		if (this.contains("rankFinish"))
			return this.getLong("rankFinish");
		return 0;
	}

	public String getReconnectServer() {

		final String key = "reconnectserver:" + this.playerID + ":*";
		final Jedis jedis = this.plugin.getConnector().getResource();
		final Set<String> keys = jedis.keys(key);
		if (keys.isEmpty())
			return null;

		String server = null;

		final Iterator<String> servers = keys.iterator();

		while (servers.hasNext() && server != null)
			server = servers.next();

		if (server == null)
			return null;

		server = jedis.get(server);
		jedis.close();
		return server;
	}

	public int getTokens() {
		if (!this.contains("tokens"))
			this.setInt("tokens", 0);
		return this.getInt("tokens");
	}

	@Override
	public Map<String, String> getValues() {
		this.refreshIfNeeded();
		return super.getValues();
	}

	public double increaseCoins(final double incrBy) {
		final Jedis jedis = this.plugin.getConnector().getResource();
		final double newValue = Double.parseDouble(jedis.hget("player:" + this.playerID, "coins")) + incrBy;
		jedis.hset("player:" + this.playerID, "coins", String.valueOf(newValue));
		jedis.close();

		this.playerData.put("coins", String.valueOf(newValue));
		final PlayerLocal pl = PlayerLocalManager.get().getPlayerLocal(this.playerID);
		pl.setGainedEC(pl.getGainedEC() + incrBy);
		return newValue;
	}

	public double increaseLC(final double incrBy) {
		final Jedis jedis = this.plugin.getConnector().getResource();
		final double newValue = Double.parseDouble(jedis.hget("player:" + this.playerID, "legendarycoins")) + incrBy;
		jedis.hset("player:" + this.playerID, "legendarycoins", String.valueOf(newValue));
		jedis.close();

		this.playerData.put("legendarycoins", String.valueOf(newValue));

		final PlayerLocal pl = PlayerLocalManager.get().getPlayerLocal(this.playerID);
		pl.setGainedLC(pl.getGainedLC() + incrBy);
		return newValue;
	}

	public boolean isChecked() {
		return this.contains("checked");
	}

	protected void refreshIfNeeded() {
		if (this.lastRefresh == null || this.lastRefresh.getTime() + 1000 * 60 * 5 < System.currentTimeMillis())
			this.updateData();
	}

	@Override
	public void remove(final String key) {
		this.playerData.remove(key);
		this.plugin.getTasksManager().addTask(() -> {
			final Jedis jedis = this.plugin.getConnector().getResource();
			jedis.hdel("player:" + this.playerID, key);
			jedis.close();
		});
	}

	@Override
	public void set(final String key, final String value) {
		this.playerData.put(key, value);

		final Jedis jedis = this.plugin.getConnector().getResource();
		jedis.hset("player:" + this.playerID, key, value);
		jedis.close();
	}

	@Override
	public void setBoolean(final String key, final boolean value) {
		this.set(key, String.valueOf(value));
	}

	public void setBoosterTime(final long days) {
		this.setDouble("booster", 0.25);

		if (this.contains("boosterFinish"))
			if (this.getLong("boosterFinish") == 0)
				this.setLong("boosterFinish", System.currentTimeMillis() + days * 86400000);
			else
				this.setLong("boosterFinish", this.getLong("boosterFinish") + days * 86400000);
		else
			this.setLong("boosterFinish", System.currentTimeMillis() + days * 86400000);

	}

	@Override
	public void setDouble(final String key, final double value) {
		this.set(key, String.valueOf(value));
	}

	@Override
	public void setInt(final String key, final int value) {
		this.set(key, String.valueOf(value));
	}

	@Override
	public void setLong(final String key, final long value) {
		this.set(key, String.valueOf(value));
	}

	public void setRank(final int rank) {
		if (BukkitAPI.get().isApi() && API.getInstance().getHandleRank())
			this.setInt(API.getInstance().getGame() + "-rank", rank);
		else
			this.set("rank", Integer.toString(rank));
	}

	public void setRank(final RankAPI rank) {
		this.set("rank", Integer.toString(rank.getId()));
		if (this.contains("rankFinish"))
			this.remove("rankFinish");
	}

	public void setRankTime(final RankAPI rank, final long days) {
		this.set("rank", Integer.toString(rank.getId()));
		if (this.contains("rankFinish"))
			if (this.getLong("rankFinish") == 0)
				this.set("rankFinish", Long.toString(System.currentTimeMillis() + days * 86400000));
			else
				this.set("rankFinish", Long.toString(this.getLong("rankFinish") + days * 86400000));
		else
			this.set("rankFinish", Long.toString(System.currentTimeMillis() + days * 86400000));
	}

	public void setReconnectServer(final String server) {
		final String key = "reconnectserver:" + this.playerID + ":" + server;
		final Jedis jedis = this.plugin.getConnector().getResource();

		final String keyBis = "reconnectserver:" + this.playerID + ":*";
		final Set<String> keys = jedis.keys(keyBis);
		for (final String k : keys)
			jedis.del(k);

		jedis.set(key, server);
		jedis.close();
	}

	protected void updateData() {
		final Jedis jedis = this.plugin.getConnector().getResource();
		final Map<String, String> data = jedis.hgetAll("player:" + this.playerID);
		jedis.close();
		this.playerData = data;
		this.lastRefresh = new Date();
	}

	public void withdrawCoins(final double famount, final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			final double result = this.decreaseCoins(famount);
			if (financialCallback != null)
				financialCallback.done(result, -famount, null);

		}, "WithdrawCoinsThread").start();
	}

	public void withdrawLC(final double amount, final FinancialCallback<Long> financialCallback) {
		new Thread(() -> {
			final double result = this.decreaseLC(amount);

			if (financialCallback != null)
				financialCallback.done(result, -amount, null);

		}, "WithdrawStarsThread").start();
	}

}
