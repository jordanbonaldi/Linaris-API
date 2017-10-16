package net.neferett.linaris.db.redis;

import java.util.HashMap;
import java.util.Map;

import net.neferett.linaris.db.Credentials;
import net.neferett.linaris.db.Database;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public final class RedisDatabase implements Database {

	public static RedisDatabase create(final Credentials credentials) {
		return new RedisDatabase(credentials);
	}

	private final Credentials						credentials;
	private JedisPool								pool;

	private final Map<Class<?>, RedisRepository<?>>	repositories	= new HashMap<>();

	private RedisDatabase(final Credentials credentials) {
		this.credentials = credentials;
	}

	public Jedis connected() {
		try (Jedis jedis = this.pool.getResource()) {
			return jedis;
		} catch (final JedisConnectionException e) {
			System.out.println("Redis connection had died, reconnecting.");
			this.setup();
		}
		return this.connected();
	}

	public Credentials credentials() {
		return this.credentials;
	}

	public Jedis getBungeeResource() {
		return this.connected();
	}

	public Jedis getResource() {
		return this.connected();
	}

	public <T> void loadRepository(final RedisRepository<? extends RedisEntity> repository) {
		this.repositories.put(repository.getClassOfEntity(), repository);
	}

	public RedisRepository<?> repositoryBy(final Class<? extends RedisEntity> model) {
		return this.repositories.get(model);
	}

	@Override
	public void setup() {
		final JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxTotal(20);
		config.setMinIdle(5);
		config.setMaxIdle(10);
		config.setMaxWaitMillis(200L);
		config.setBlockWhenExhausted(false);

		final String host = this.credentials.getHost();
		int port = 6379;

		if (host.split(":").length == 2)
			try {
				port = Integer.parseInt(host.split(":")[1]);
			} catch (final NumberFormatException ignored) {
				System.out.println("Host " + host + " has an invalid port!");
			}

		this.pool = this.credentials.getPassword() != null && this.credentials.getPassword().length() > 0
				? new JedisPool(config, host, port, 1000, this.credentials.getPassword(), this.credentials.database())
				: new JedisPool(config, host, port, 1000);
	}
}
