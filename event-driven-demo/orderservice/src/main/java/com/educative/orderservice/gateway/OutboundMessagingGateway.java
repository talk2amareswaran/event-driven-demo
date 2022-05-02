package com.educative.orderservice.gateway;

import org.springframework.integration.annotation.MessagingGateway;

import com.educative.orderservice.model.Order;

@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
public interface OutboundMessagingGateway {
    void sendToPubsub(Order order);
}