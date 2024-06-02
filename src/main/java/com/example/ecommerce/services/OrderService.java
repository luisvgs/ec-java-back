package com.example.ecommerce.services;

import com.example.ecommerce.dtos.OrderProductDTO;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.OrderProduct;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.Query;

@Service
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    @Autowired
    private OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void createOrder() {

    }

    public List<Map<String, Object>>  listAllOrders() {
        String sql = "SELECT \n" +
                "    o.id AS order_id,\n" +
                "    p.name AS name,\n" +
                "    p.category AS category,\n" +
                "    p.price AS price,\n" +
                "    op.quantity AS quantity\n" +
                "FROM orders o\n" +
                "JOIN order_product op ON o.id = op.order_id\n" +
                "JOIN products p ON op.product_id = p.id\n" +
                "ORDER BY order_id";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        return transformResultList(resultList);
        // return query.getResultList();
    }


    public  List<Map<String, Object>> getOrderById(int orderId) throws JsonProcessingException {
        String sql = "SELECT \n" +
                "    o.id AS order_id,\n" +
                "    p.name AS name,\n" +
                "    p.category AS category,\n" +
                "    p.price AS price,\n" +
                "    op.quantity AS quantity\n" +
                "FROM orders o\n" +
                "JOIN order_product op ON o.id = op.order_id\n" +
                "JOIN products p ON op.product_id = p.id\n" +
                "WHERE op.order_id = :orderId\n" + // Add condition to filter orders by ID
                "ORDER BY order_id";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("orderId", orderId); // Set the parameter

        List<Object[]> resultList = query.getResultList();

        return transformResultList(resultList);
    }

    public List<Map<String, Object>> transformResultList(List<Object[]> resultList) {
        List<Map<String, Object>> transformedList = new ArrayList<>();

        for (Object[] row : resultList) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("order_id", row[0]);
            orderMap.put("name", row[1]);
            orderMap.put("category", row[2]);
            orderMap.put("price", row[3]);
            orderMap.put("quantity", row[4]);

            transformedList.add(orderMap);
        }

        return transformedList;
    }

    public ResponseEntity<String> deleteOrder(int orderId) throws BadRequestException {
        Long id_ = (long) orderId;
        Order order = orderRepository.findById(id_)
                .orElseThrow(() -> new RuntimeException("Order with that id does not exist"));

        try {
            orderRepository.delete(order);
            return ResponseEntity.ok("Order deleted successfully.");
        } catch(Exception error) {
            throw new BadRequestException("Something wrong happened when deleting the order." + error);
        }
    }

    public ResponseEntity<String> createOrder(List<OrderProductDTO> orderProductDTO) {
        Order order = new Order();
        List<OrderProduct> orderProducts = orderProductDTO.stream().map(dto -> {
            OrderProduct orderProduct = new OrderProduct();
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));


            orderProduct.setProduct(product);
            orderProduct.setQuantity(dto.getQuantity());
            orderProduct.setOrder(order);
            return orderProduct;
        }).collect(Collectors.toList());

        order.setProducts(orderProducts);

        orderRepository.save(order);
        return ResponseEntity.created(null).body("Order created successfully!");
    }
}
