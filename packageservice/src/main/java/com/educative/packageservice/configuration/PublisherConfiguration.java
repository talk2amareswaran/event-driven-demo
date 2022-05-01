package com.educative.packageservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

import com.educative.packageservice.callback.CustomFailureCallBack;
import com.educative.packageservice.callback.CustomSuccessCallBack;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;

@Configuration
public class PublisherConfiguration {

	private static final String TOPIC_NAME = "package";
	
	@Bean
	@ServiceActivator(inputChannel = "pubsubOutputChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, TOPIC_NAME);
		adapter.setSuccessCallback(new CustomSuccessCallBack());
		adapter.setFailureCallback(new CustomFailureCallBack());
		return adapter;
	}
}
