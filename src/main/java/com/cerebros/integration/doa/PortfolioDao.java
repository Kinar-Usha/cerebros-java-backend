package com.cerebros.integration.doa;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;

import java.sql.SQLException;
import java.util.List;

public interface PortfolioDao {
    List<Portfolio> getPortfolio(String clientId) throws SQLException, ClientNotFoundException;

    int addToPortfolio(Portfolio dummyPortfolio, String clientId);
    int updateportfolio(Portfolio portfolio, String clientId);
}
