package net.neferett.linaris.db;

import net.neferett.linaris.BukkitAPI;
import redis.clients.jedis.Jedis;

public abstract class DatabaseConnector {

	protected String	password;
	protected BukkitAPI	plugin;

	public abstract void disable();

	public abstract Jedis getBungeeResource();

	public abstract Jedis getRank();

	public abstract Jedis getResource();

	public abstract Jedis getTokenResource();

	public abstract void initiateConnections() throws InterruptedException;

	public abstract void killConnections();

}
