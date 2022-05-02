package com.educative.notificationservice.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.educative.notificationservice.model.OrderPackage;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Configuration
public class PackageServiceSubscriberConfiguration {

	public static final String SUBSCRIPTION_NAME = "package-notification";

	@Bean
	public PubSubInboundChannelAdapter packageNotificationChannelAdapter(@Qualifier("packageNotificationInputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
		adapter.setOutputChannel(messageChannel);
		adapter.setPayloadType(OrderPackage.class);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}
	
	@Bean
	public MessageChannel packageNotificationInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "packageNotificationInputChannel")
	public MessageHandler packageNotificationMessageReceiver() {
		return message -> {
			OrderPackage orderPackage = (OrderPackage) message.getPayload();
			System.out.println();
			System.out.println("Notification received for the Package ID ["+orderPackage.getPackage_id()+"]");
			BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();

		};
	}
}
