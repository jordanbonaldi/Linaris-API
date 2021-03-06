package net.neferett.linaris.rabbitmq.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {

	private static Connection	connection;
	private static String		rabbitHost		= "127.0.0.1";

	private static String		rabbitPassword	= "toto";

	private static String		rabbitUsername	= "admin";

	public static Connection getConnection() {
		return connection;
	}

	public static String getRabbitHost() {
		return rabbitHost;
	}

	public static String getRabbitPassword() {
		return rabbitPassword;
	}

	public static String getRabbitUsername() {
		return rabbitUsername;
	}

	public static void inits() {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(getRabbitHost());
		factory.setUsername(getRabbitUsername());
		factory.setPassword(getRabbitPassword());
		// factory.setVirtualHost("/");
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(10000);
		try {
			connection = factory.newConnection();
		} catch (final IOException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		} catch (final TimeoutException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}

}
