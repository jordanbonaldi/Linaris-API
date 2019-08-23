package net.neferett.linaris.rabbitmq.utils.rpc;

import com.rabbitmq.client.AMQP.BasicProperties;

import net.neferett.linaris.rabbitmq.utils.RabbitMQUtils;
import net.neferett.linaris.utils.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMQRCPClient {

	private Connection connection;
	private Channel channel;
	private String requestQueueName;
	private String replyQueueName;
	private QueueingConsumer consumer;
	private JSONObject message;

	private JSONObject callback;

	public RabbitMQRCPClient(String requestQueueName, JSONObject message) throws Exception {

		this.requestQueueName = requestQueueName;
		this.message = message;

		connection = RabbitMQUtils.getConnection();
		channel = connection.createChannel();

		replyQueueName = channel.queueDeclare().getQueue();
		consumer = new QueueingConsumer(channel);
		channel.basicConsume(replyQueueName, true, consumer);

		this.callback = call(message);

		close();
	}

	public void setMessage(JSONObject message) {
		this.message = message;
	}

	public void setRequestQueueName(String requestQueueName) {
		this.requestQueueName = requestQueueName;
	}

	public RabbitMQRCPClient() throws Exception {
	}

	public void send() throws Exception {

		connection = RabbitMQUtils.getConnection();
		channel = connection.createChannel();
		replyQueueName = channel.queueDeclare().getQueue();
		consumer = new QueueingConsumer(channel);
		channel.basicConsume(replyQueueName, true, consumer);

		this.callback = call(message);
		close();
	}

	public JSONObject getCallback() {
		return callback;
	}

	private JSONObject call(JSONObject message) throws Exception {
		String response = null;
		String corrId = java.util.UUID.randomUUID().toString();

		BasicProperties props = new BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();

		channel.basicPublish("", requestQueueName, props, message.toString().getBytes());

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				System.out.println(new String(delivery.getBody()));
				response = new String(delivery.getBody());
				break;
			}
		}

		System.out.println(response);

		return new JSONObject(response);
	}

	private void close() throws Exception {
		channel.close();
	}

}
