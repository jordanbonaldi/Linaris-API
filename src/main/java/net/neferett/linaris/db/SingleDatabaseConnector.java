package net.neferett.linaris.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import net.neferett.linaris.BukkitAPI;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SingleDatabaseConnector extends DatabaseConnector {

	protected JedisPool		cachePool;
	protected JedisPool		cachePoolToken;
	protected JedisPool		mainPool;
	private final String	masterIp;

	public SingleDatabaseConnector(final BukkitAPI plugin, final String masterIp, final String password) {
		this.plugin = plugin;
		this.masterIp = masterIp;
		this.password = password;

		plugin.getLogger().info("[Database] Initializing connection.");
		try {
			this.initiateConnections();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disable() {
		this.plugin.getLogger().info("[Disabling Connector] Removing pools...");
		this.killConnections();
	}

	@Override
	public Jedis getBungeeResource() {
		return this.cachePool.getResource();
	}

	@Override
	public Jedis getResource() {
		return this.mainPool.getResource();
	}

	@Override
	public Jedis getTokenResource() {
		return this.cachePoolToken.getResource();
	}

	@Override
	public void initiateConnections() throws InterruptedException {
		final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1024);
		config.setMaxWaitMillis(5000);

		final String[] mainParts = StringUtils.split(this.masterIp, ":");
		final int mainPort = mainParts.length > 1 ? Integer.decode(mainParts[1]) : 6379;

		this.mainPool = new JedisPool(config, mainParts[0], mainPort, 5000, this.password);
		this.cachePool = new JedisPool(config, mainParts[0], mainPort, 5000, this.password, 1);
		this.cachePoolToken = new JedisPool(config, mainParts[0], mainPort, 5000, this.password, 2);

		this.plugin.getLogger().info("[Database] Connection initialized.");

	}

	@Override
	public void killConnections() {
		this.mainPool.destroy();
		this.cachePool.destroy();
		this.cachePoolToken.destroy();
	}
}
