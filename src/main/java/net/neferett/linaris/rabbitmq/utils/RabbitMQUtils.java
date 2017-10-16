package net.neferett.linaris.rabbitmq.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {

	private static Connection connection;
	public static Connection getConnection() {
		return connection;
	}
	
	public static void inits() {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(getRabbitHost());
		factory.setUsername(getRabbitUsername());
		factory.setPassword(getRabbitPassword());
		//factory.setVirtualHost("/");
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(10000);
	    try {
			connection = factory.newConnection();
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		} catch (TimeoutException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}
	
	private static String rabbitHost = "149.202.65.5";
	public static String getRabbitHost() {
		return rabbitHost;
	}
	private static String rabbitUsername = "linaris";
	public static String getRabbitUsername() {
		return rabbitUsername;
	}
	private static String rabbitPassword = "d8F3uN5r";
	public static String getRabbitPassword() {
		return rabbitPassword;
	}
	
}
