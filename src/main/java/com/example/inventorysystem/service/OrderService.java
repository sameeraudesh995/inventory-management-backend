package com.example.inventorysystem.service;

import com.example.inventorysystem.model.Customer;
import com.example.inventorysystem.model.Item;
import com.example.inventorysystem.model.Order;
import com.example.inventorysystem.model.OrderItemRequest;
import com.example.inventorysystem.repository.CustomerRepository;
import com.example.inventorysystem.repository.ItemRepository;
import com.example.inventorysystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Order createOrder(Long customerId, List<OrderItemRequest> orderItems) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Item> items = new ArrayList<>();
        double total = 0.0;

        for (OrderItemRequest request : orderItems) {
            Item item = itemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (item.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Not enough stock for item: " + item.getName());
            }

            item.setQuantity(item.getQuantity() - request.getQuantity());
            itemRepository.save(item);

            // Add item multiple times based on quantity
            for (int i = 0; i < request.getQuantity(); i++) {
                items.add(item);
                total += item.getPrice();
            }
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(items);
        order.setOrderDate(new Date());
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
