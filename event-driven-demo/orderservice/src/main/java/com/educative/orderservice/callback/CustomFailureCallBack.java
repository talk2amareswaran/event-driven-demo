package com.educative.orderservice.callback;

import org.springframework.messaging.Message;

import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler.FailureCallback;

public class CustomFailureCallBack implements FailureCallback {

    @Override
    public void onFailure(Throwable cause, Message<?> message) {
        System.out.println("Unable to publish message and error is: " + cause.getMessage());
    }
}