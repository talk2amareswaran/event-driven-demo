package com.educative.packageservice.configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.educative.packageservice.gateway.OutboundMessagingGateway;
import com.educative.packageservice.model.Order;
import com.educative.packageservice.model.OrderPackage;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Configuration
public class SubscriberConfiguration {

	public static final String SUBSCRIPTION_NAME = "order-packaging";

	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("myInputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
		adapter.setOutputChannel(messageChannel);
		adapter.setPayloadType(Order.class);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}

	@Bean
	public MessageChannel myInputChannel() {
		return new DirectChannel();
	}

	@Autowired
	OutboundMessagingGateway outboundMessagingGateway;

	@Bean
	@ServiceActivator(inputChannel = "myInputChannel")
	public MessageHandler messageReceiver() {
		return message -> {
			Order order = (Order) message.getPayload();
			
			BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();

			try {
				System.out.println("*******************************************************************************************");
				System.out.println("Order ID ["+order.getOrder_id()+"] is received and Packaging is [IN-PROGRESS]");
				
				System.out.println("Order Date: " + order.getOrder_date());
				System.out.println("Order by: " + order.getOrder_by());
				System.out.println("Total Products: " + order.getTotal_products());
				System.out.println("Grand Total: " + order.getGrand_total());
				
				Thread.sleep(5000);
				
				OrderPackage orderPackage = new OrderPackage();
				orderPackage.setOrder(order);
				orderPackage.setPackage_id("PACKAGESERVICE-"+ (new Random().nextInt(1000) + 1));
				orderPackage.setPackage_date(new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").format(new Date()));
				orderPackage.setPackage_by("Educative Package System");

				outboundMessagingGateway.sendToPubsub(orderPackage);
				System.out.println("Packaging is [COMPLETED] for the Order ID [" + order.getOrder_id() +"] and the Package ID is ["+orderPackage.getPackage_id()+"]");
				System.out.println("*******************************************************************************************");
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	}
}
