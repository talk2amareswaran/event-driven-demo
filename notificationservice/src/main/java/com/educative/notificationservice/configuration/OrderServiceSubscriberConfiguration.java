package com.educative.notificationservice.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.educative.notificationservice.model.Order;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Configuration
public class OrderServiceSubscriberConfiguration {

	public static final String SUBSCRIPTION_NAME = "order-notification";

	@Bean
	public PubSubInboundChannelAdapter orderNotificationChannelAdapter(
			@Qualifier("orderNotificationInputChannel") MessageChannel messageChannel, PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
		adapter.setOutputChannel(messageChannel);
		adapter.setPayloadType(Order.class);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}

	@Bean
	public MessageChannel orderNotificationInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "orderNotificationInputChannel")
	public MessageHandler orderNotificationMessageReceiver() {
		return message -> {
			Order order = (Order) message.getPayload();
			System.out.println();
			System.out.println("Notification received for the Order ID [" + order.getOrder_id()+"]");
			BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();

		};
	}
}
