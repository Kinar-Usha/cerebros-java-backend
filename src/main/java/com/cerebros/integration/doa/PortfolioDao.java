package com.cerebros.integration.doa;

import com.cerebros.models.Portfolio;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioDao {
    List<Portfolio> getPortfolio(String clientId) ;

    int addToPortfolio(Portfolio dummyPortfolio, String clientId);
    int updatePortfolio(Portfolio portfolio, String clientId);
    int updateCash(String clientId, BigDecimal cash);
    int deletePortfolio(String clientId, String instrumentId);
}
