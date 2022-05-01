package com.educative.notificationservice.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.educative.notificationservice.model.Shipping;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Configuration
public class ShippingServiceSubscriberConfiguration {

	public static final String SUBSCRIPTION_NAME = "shipping-notification";

	@Bean
	public PubSubInboundChannelAdapter shippingNotificationChannelAdapter(@Qualifier("shippingNotificationInputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
		adapter.setOutputChannel(messageChannel);
		adapter.setPayloadType(Shipping.class);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}
	
	@Bean
	public MessageChannel shippingNotificationInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "shippingNotificationInputChannel")
	public MessageHandler shippingNotificationMessageReceiver() {
		return message -> {
			Shipping shipping = (Shipping) message.getPayload();
			System.out.println();
			System.out.println("Notification received for the Shipping ID [" + shipping.getShipping_id() + "] Date ["
					+ shipping.getShipping_date() + "] Shipping by [" + shipping.getShipping_by()+"]");
			BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();

		};
	}
}
