package com.example.inventorysystem.controller;

import com.example.inventorysystem.model.Order;
import com.example.inventorysystem.model.OrderItemRequest;
import com.example.inventorysystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestParam Long customerId, @RequestBody List<OrderItemRequest> orderItems) {
        return orderService.createOrder(customerId, orderItems);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
