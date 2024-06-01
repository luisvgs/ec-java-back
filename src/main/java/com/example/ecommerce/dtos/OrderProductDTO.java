package com.example.ecommerce.dtos;

public class OrderProductDTO {
    private Long product;
    private int quantity;
    private Long order;

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getOrder() {
        return order;
    }

    public Long getProductId() {
        return product;
    }

    public void setProductId(Long product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
