package com.Arjun.OrderService.controller;

import com.Arjun.OrderService.dto.OrderRequest;
import com.Arjun.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest.skuCode()+" "+orderRequest.toString());
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }
}
