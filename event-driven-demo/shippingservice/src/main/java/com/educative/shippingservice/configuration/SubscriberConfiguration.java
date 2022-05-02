package com.educative.shippingservice.configuration;

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

import com.educative.shippingservice.gateway.OutboundMessagingGateway;
import com.educative.shippingservice.model.OrderPackage;
import com.educative.shippingservice.model.Shipping;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Configuration
public class SubscriberConfiguration {

	public static final String SUBSCRIPTION_NAME = "package-shipping";

	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("myInputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
		adapter.setOutputChannel(messageChannel);
		adapter.setPayloadType(OrderPackage.class);
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
			
			OrderPackage orderPackage = (OrderPackage) message.getPayload();

			BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();

			try {
				
				System.out.println("*******************************************************************************************");
				System.out.println("Package ID ["+orderPackage.getPackage_id()+"] is received and Shipping is [IN-PROGRESS]");
				
				System.out.println("Package Date: " + orderPackage.getPackage_date());
				System.out.println("Package by: " + orderPackage.getPackage_by());
				System.out.println("Order Id of the package: " + orderPackage.getOrder().getOrder_id());
				
				Thread.sleep(5000);
				
				Shipping shipping = new Shipping();
				shipping.setOrderPackage(orderPackage);
				shipping.setShipping_id("SHIPPINGSERVICE-" + (new Random().nextInt(1000) + 1));
				shipping.setShipping_date(new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").format(new Date()));
				shipping.setShipping_by("Educative Shipping System");
				outboundMessagingGateway.sendToPubsub(shipping);
				System.out.println("Shipping is [COMPLETED] for the Package ID [" + orderPackage.getPackage_id()+"] and the Shipping ID is ["+shipping.getShipping_id()+"]");
				System.out.println("*******************************************************************************************");
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		};
	}
}
