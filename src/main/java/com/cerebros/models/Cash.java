package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Cash {
    private BigDecimal cashRemaining;

    public Cash() {
    }

    public Cash(BigDecimal cashRemaining) {
        this.cashRemaining = cashRemaining;
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
        Cash cash = (Cash) o;
        return Objects.equals(cashRemaining, cash.cashRemaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashRemaining);
    }

    @Override
    public String toString() {
        return "Cash{" +
                "cashRemaining=" + cashRemaining +
                '}';
    }
}
