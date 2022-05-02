package com.educative.orderservice.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.educative.orderservice.gateway.OutboundMessagingGateway;
import com.educative.orderservice.model.Order;

@RestController
public class OrderServiceRestController {

	@Autowired
	OutboundMessagingGateway outboundMessagingGateway;

	@PostMapping("/publish")
	public String publishMessage(@RequestBody Order order) {
		order.setOrder_id("ORDERSERVICE-"+ (new Random().nextInt(1000) + 1) );
		order.setOrder_date(new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").format(new Date()));
		order.setOrder_by("Educative Order System");
		outboundMessagingGateway.sendToPubsub(order);
		System.out.println("Order ID ["+order.getOrder_id()+"] published successfully");
		System.out.println();
		return "Message published into the Google Cloud Pub/Sub topic";
	}
}
