package com.Arjun.OrderService.service;

import com.Arjun.OrderService.client.InventoryClient;
import com.Arjun.OrderService.dto.OrderRequest;
import com.Arjun.OrderService.event.OrderPlacedEvent;
import com.Arjun.OrderService.model.Order;
import com.Arjun.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private  final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        System.out.println(orderRequest.quantity() +" "+ orderRequest.skuCode()+ " "+orderRequest.price());
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            OrderPlacedEvent orderPlacedEvent=new OrderPlacedEvent(order.getOrderNumber(),orderRequest.userDetails().email());
            log.info("Start - sending OrderPlacedEvent {} to kafka topic",orderPlacedEvent);
            kafkaTemplate.send("order_placed",orderPlacedEvent);
            log.info("End - sending OrderPlacedEvent {} to kafka topic",orderPlacedEvent);
        }
        else throw new RuntimeException("product with skuCode "+ orderRequest.skuCode()+" is not in stock");
    }
}
