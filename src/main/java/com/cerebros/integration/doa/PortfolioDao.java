package com.cerebros.integration.doa;

import com.cerebros.models.Portfolio;

import java.util.List;

public interface PortfolioDao {
    List<Portfolio> getPortfolio(String clientId) ;

    int addToPortfolio(Portfolio dummyPortfolio, String clientId);
    int updatePortfolio(Portfolio portfolio, String clientId);
}
