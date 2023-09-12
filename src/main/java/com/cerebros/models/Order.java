package com.cerebros.models;

import java.math.BigDecimal;

public class Order {
    private String orderId;
    private BigDecimal quantity;
    private BigDecimal targetPrice;
    private String direction;
    private String clientId;
    private String insturmentId;

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public String getDirection() {
        return direction;
    }

    public String getClientId() {
        return clientId;
    }

    public String getInsturmentId() {
        return insturmentId;
    }

    public Order(String orderId, BigDecimal quantity, BigDecimal targetPrice, String direction, String clientId, String insturmentId) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.targetPrice = targetPrice;
        this.direction = direction;
        this.clientId = clientId;
        this.insturmentId = insturmentId;
    }
}
