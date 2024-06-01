package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.OrderProductDTO;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.OrderProduct;
import com.example.ecommerce.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/healthcheck")
    public String check() {
        return "OK";
    }

    @GetMapping("/all")
    public List<Order> getAll() {
        return this.orderService.listAllOrders();
    }

    @GetMapping("/withId/{orderId}")
    public  List<Map<String, Object>> getOrderById(@PathVariable("orderId") int orderId) throws BadRequestException, JsonProcessingException {
        return this.orderService.getOrderById(orderId);
    }

    @PostMapping("/create")
    public ResponseEntity<String>createOrder(@RequestBody List<OrderProductDTO> orderProducts) {
        return orderService.createOrder(orderProducts);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> createOrder(@RequestParam(name="orderId") int orderId) throws  BadRequestException{
        return orderService.deleteOrder(orderId);
    }
}
