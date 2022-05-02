package com.educative.shippingservice.gateway;

import org.springframework.integration.annotation.MessagingGateway;

import com.educative.shippingservice.model.Shipping;

@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
public interface OutboundMessagingGateway {
    void sendToPubsub(Shipping  shipping);
}