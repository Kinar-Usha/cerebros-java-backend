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
    private String token;

    public String getToken() {
        return token;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public void setPlacedTime(Date placedTime) {
        this.placedTime = placedTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Order() {
    }

    public Order(String orderId, BigDecimal quantity, BigDecimal targetPrice, String direction, String clientId, String instrumentId, String token, Date placedTime) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.targetPrice = targetPrice;
        this.direction = direction;
        this.clientId = clientId;
        this.instrumentId = instrumentId;
        this.token = token;
        this.placedTime = placedTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", quantity=" + quantity +
                ", targetPrice=" + targetPrice +
                ", direction='" + direction + '\'' +
                ", clientId='" + clientId + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", token='" + token + '\'' +
                ", placedTime=" + placedTime +
                '}';
    }
}
