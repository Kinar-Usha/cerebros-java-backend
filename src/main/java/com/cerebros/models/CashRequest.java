package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Objects;

public class CashRequest {
    private String clientId;
    private BigDecimal cashRemaining;

    public CashRequest() {
    }

    public CashRequest(String clientId, BigDecimal cashRemaining) {
        this.clientId = clientId;
        this.cashRemaining = cashRemaining;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getCashRemaining() {
        return cashRemaining;
    }

    public void setCashRemaining(BigDecimal cashRemaining) {
        this.cashRemaining = cashRemaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashRequest that = (CashRequest) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(cashRemaining, that.cashRemaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, cashRemaining);
    }

    @Override
    public String toString() {
        return "CashRequest{" +
                "clientId='" + clientId + '\'' +
                ", cashRemaining=" + cashRemaining +
                '}';
    }
}
