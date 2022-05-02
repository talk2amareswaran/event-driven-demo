package com.educative.packageservice.gateway;

import org.springframework.integration.annotation.MessagingGateway;

import com.educative.packageservice.model.OrderPackage;

@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
public interface OutboundMessagingGateway {
    void sendToPubsub(OrderPackage  orderPackage);
}