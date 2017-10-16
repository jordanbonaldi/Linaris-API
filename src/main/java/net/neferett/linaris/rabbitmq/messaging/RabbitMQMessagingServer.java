package net.neferett.linaris.rabbitmq.messaging;

import java.io.IOException;

import org.bukkit.Bukkit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.rabbitmq.utils.RabbitMQUtils;
import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public abstract class RabbitMQMessagingServer implements Runnable {

	String requestQueueName;
	String queueName;
	Connection connection;
	Channel channel;

	public RabbitMQMessagingServer(String requestQueueName) throws IOException {
		
		this.requestQueueName = requestQueueName;
		
		connection = RabbitMQUtils.getConnection();
		channel = connection.createChannel();

		channel.exchangeDeclare(requestQueueName, "fanout");
		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, requestQueueName, "");

		Bukkit.getServer().getScheduler().runTaskAsynchronously(BukkitAPI.get(), this);
	}

	@Override
	public void run() {	
		try {
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					try {
						JSONObject json = new JSONObject(message);
						onMessage(json);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void onMessage(JSONObject message) throws Exception;

}
