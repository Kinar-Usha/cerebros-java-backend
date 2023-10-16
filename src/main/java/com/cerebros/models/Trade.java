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
    private String description;
    private Date executedTime;
    private BigDecimal suitabilityScore;

    public BigDecimal getSuitabilityScore() {
        return suitabilityScore;
    }

    public void setSuitabilityScore(BigDecimal suitabilityScore) {
        this.suitabilityScore = suitabilityScore;
    }

    public Trade() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue,
            String clientid, String instrumentId, Order order, Date executedTime) {
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

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue,
            String clientid, String instrumentId, Order order) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue,
            String clientid, String instrumentId, Order order, Date executedTime, BigDecimal suitabilityScore) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.suitabilityScore = suitabilityScore;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
        this.executedTime = executedTime;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue,
            String clientid, String instrumentId, Order order, BigDecimal suitabilityScore) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.suitabilityScore = suitabilityScore;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientid;
        this.instrumentId = instrumentId;
        this.order = order;
    }

    public Trade(String tradeId, BigDecimal quantity, BigDecimal executionPrice, String direction, BigDecimal cashValue,
            String clientId, String instrumentId, Order order, String description, Date executedTime) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.direction = direction;
        this.cashValue = cashValue;
        this.clientId = clientId;
        this.instrumentId = instrumentId;
        this.order = order;
        this.description = description;
        this.executedTime = executedTime;
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
        if (executedTime == null) {
            executedTime = new Date("20-1-2022");
        }

        this.executedTime = executedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trade other = (Trade) obj;
        return Objects.equals(cashValue, other.cashValue) && Objects.equals(clientId, other.clientId)
                && Objects.equals(description, other.description) && Objects.equals(direction, other.direction)
                && Objects.equals(executedTime, other.executedTime)
                && Objects.equals(executionPrice, other.executionPrice)
                && Objects.equals(instrumentId, other.instrumentId) && Objects.equals(order, other.order)
                && Objects.equals(quantity, other.quantity) && Objects.equals(suitabilityScore, other.suitabilityScore)
                && Objects.equals(tradeId, other.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashValue, clientId, description, direction, executedTime, executionPrice, instrumentId,
                order, quantity, suitabilityScore, tradeId);
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
                ", description='" + description + '\'' +
                ", executedTime=" + executedTime +
                '}';
    }
}
