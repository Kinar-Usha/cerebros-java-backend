package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.cerebros.services.TradeService;

public class Trade {
    private String tradeId;
    private BigDecimal quantity;
    private BigDecimal executionPrice;
    private String direction;
    private BigDecimal cashValue;
    private String clientid;
    private String instrumentId;
    private Order order;

    public Date getExecutedTime() {
        return executedTime;
    }

    private Date executedTime;

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

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue, String clientid, String instrumentId, Order order, Date executedTime) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientid = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
        this.executedTime = executedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(tradeId, trade.tradeId) &&
                Objects.equals(quantity, trade.quantity) &&
                Objects.equals(executionPrice, trade.executionPrice) &&
                Objects.equals(direction, trade.direction) &&
                Objects.equals(cashValue, trade.cashValue) &&
                Objects.equals(clientid, trade.clientid) &&
                Objects.equals(instrumentId, trade.instrumentId) &&
                Objects.equals(order, trade.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, quantity, executionPrice, direction, cashValue, clientid, instrumentId, order);
    }

}
