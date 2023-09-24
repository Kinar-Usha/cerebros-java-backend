package com.cerebros.integration;

import com.cerebros.models.Portfolio;

import java.util.List;

public interface PortfolioMapper {
    public List<Portfolio> getPortfolio(String clientId);
}
