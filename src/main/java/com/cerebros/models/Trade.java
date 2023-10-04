package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Trade {
    private String tradeId;
    private BigDecimal quantity;
    private BigDecimal executionPrice;
    private String direction;
    private BigDecimal cashValue;
    private String clientId;
    private String instrumentId;
    private Order order;
    private Date executedTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(tradeId, trade.tradeId) && Objects.equals(quantity, trade.quantity) && Objects.equals(executionPrice, trade.executionPrice) && Objects.equals(direction, trade.direction) && Objects.equals(cashValue, trade.cashValue) && Objects.equals(clientId, trade.clientId) && Objects.equals(instrumentId, trade.instrumentId) && Objects.equals(order, trade.order) && Objects.equals(executedTime, trade.executedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, quantity, executionPrice, direction, cashValue, clientId, instrumentId, order, executedTime);
    }

    public Trade() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue, String clientid, String instrumentId, Order order, Date executedTime) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
        this.executedTime = executedTime;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue, String clientid, String instrumentId, Order order) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
    }

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
        return clientId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Order getOrder() {
        return order;
    }
    public Date getExecutedTime() {
        return executedTime;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setExecutionPrice(BigDecimal executionPrice) {
        this.executionPrice = executionPrice;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setCashValue(BigDecimal cashValue) {
        this.cashValue = cashValue;
    }

    public void setClientid(String clientid) {
        this.clientId = clientid;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setExecutedTime(Date executedTime) {
        if(executedTime==null){
            executedTime=new Date("20-1-2022");
        }


        this.executedTime = executedTime;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", quantity=" + quantity +
                ", executionPrice=" + executionPrice +
                ", direction='" + direction + '\'' +
                ", cashValue=" + cashValue +
                ", clientId='" + clientId + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", order=" + order +
                ", executedTime=" + executedTime +
                '}';
    }
}
