package com.cerebros.integration.mapper;

import com.cerebros.models.Portfolio;

import java.util.List;
import java.util.Map;

public interface PortfolioMapper {
    public List<Portfolio> getPortfolio(String clientId);



    int addToPortfolio(Map<String, Object> paramMap);

    int updatePortfolio(Map<String, Object> paramMap);
}
