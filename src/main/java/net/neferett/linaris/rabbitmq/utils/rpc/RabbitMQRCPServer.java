package net.neferett.linaris.rabbitmq.utils.rpc;

import com.rabbitmq.client.AMQP.BasicProperties;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.rabbitmq.utils.RabbitMQUtils;
import net.neferett.linaris.utils.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public abstract class RabbitMQRCPServer implements Runnable {
	
	private Connection connection;
	private Channel channel;
	private String replyQueueName;
	private QueueingConsumer consumer;
	
	public RabbitMQRCPServer(String replyQueueName) throws Exception {

		System.out.println(replyQueueName);

		this.replyQueueName = replyQueueName;
		
	    connection = RabbitMQUtils.getConnection();
		channel = connection.createChannel();

		channel.queueDeclare(this.replyQueueName, false, false, false, null);

		channel.basicQos(1);

		consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(this.replyQueueName, false, consumer);
			


		BukkitAPI.get().getServer().getScheduler().runTaskAsynchronously(BukkitAPI.get(), this);
	}	

	@Override
	public void run() {
		
		try {
			
			while (true) {
				
			    QueueingConsumer.Delivery delivery = consumer.nextDelivery();

			    System.out.println("receive");

			    BasicProperties props = delivery.getProperties();
			    BasicProperties replyProps = new BasicProperties
			                                     .Builder()
			                                     .correlationId(props.getCorrelationId())
			                                     .build();
	
			    String message = new String(delivery.getBody());
			    JSONObject response = null;
			    
			    try {
					
			    	response = onMessage(new JSONObject(message));
			    	
				} catch (Exception e) {
					e.printStackTrace();
					
					channel.basicPublish("", props.getReplyTo(), replyProps, "".getBytes());
					
				    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				    
				    continue;
				}
	
			    channel.basicPublish("", props.getReplyTo(), replyProps, response.toString().getBytes());
	
			    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		
		} catch (Exception e) {}
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public QueueingConsumer getConsumer() {
		return consumer;
	}
	
	public abstract JSONObject onMessage(JSONObject message) throws Exception;
	
}
