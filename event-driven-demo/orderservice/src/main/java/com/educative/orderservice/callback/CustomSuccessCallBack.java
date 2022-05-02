package com.educative.orderservice.callback;

import org.springframework.messaging.Message;

import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler.SuccessCallback;

public class CustomSuccessCallBack implements SuccessCallback {

    @Override
    public void onSuccess(String ackId, Message<?> message) {
        System.out.println("Message published successfully and the ACK ID is: " + ackId);
    }
}
