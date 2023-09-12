package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Portfolio {
    private String instrumentId;
    private String description;
    private String categoryId;
    private BigDecimal holdings;
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(instrumentId, portfolio.instrumentId) && Objects.equals(description, portfolio.description) && Objects.equals(categoryId, portfolio.categoryId) && Objects.equals(holdings, portfolio.holdings) && Objects.equals(price, portfolio.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId, description, categoryId, holdings, price);
    }

    public Portfolio(String instrumentId, String description, String categoryId, BigDecimal holdings, BigDecimal price) {
        this.instrumentId = instrumentId;
        this.description = description;
        this.categoryId = categoryId;
        this.holdings = holdings;
        this.price = price;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getHoldings() {
        return holdings;
    }

    public void setHoldings(BigDecimal holdings) {
        this.holdings = holdings;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
