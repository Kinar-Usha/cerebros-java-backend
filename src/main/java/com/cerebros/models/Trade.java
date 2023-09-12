package com.cerebros.models;

import java.math.BigDecimal;

public class Trade {
    private String tradeId;
    private BigDecimal quantity;
    private BigDecimal executionPrice;
    private String direction;
    private BigDecimal cashValue;
    private String clientid;
    private String instrumentId;
    private Order order;

    public String getTradeId() {
        return tradeId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getExecutionPrice() {
        return executionPrice;
    }

    public String getDirection() {
        return direction;
    }

    public BigDecimal getCashValue() {
        return cashValue;
    }

    public String getClientid() {
        return clientid;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Order getOrder() {
        return order;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue, String clientid, String instrumentId, Order order) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientid = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
    }
}
