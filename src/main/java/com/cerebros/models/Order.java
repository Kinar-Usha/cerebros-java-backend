package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String orderId;
    private BigDecimal quantity;
    private BigDecimal targetPrice;
    private String direction;
    private String clientId;
    private String instrumentId;

    public Date getPlacedTime() {
        return placedTime;
    }

    private Date placedTime;

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
        return instrumentId;
    }

    public Order(String orderId, BigDecimal quantity, BigDecimal targetPrice, String direction, String clientId, String instrumentId, Date placedTime) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.targetPrice = targetPrice;
        this.direction = direction;
        this.clientId = clientId;
        this.instrumentId = instrumentId;
        this.placedTime = placedTime;
    }

    public Order(String orderId, BigDecimal quantity, BigDecimal targetPrice, String direction, String clientId, String instrumentId) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.targetPrice = targetPrice;
        this.direction = direction;
        this.clientId = clientId;
        this.instrumentId = instrumentId;
    }
}
