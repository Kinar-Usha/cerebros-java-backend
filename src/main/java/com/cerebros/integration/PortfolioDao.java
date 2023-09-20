package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;

import java.sql.SQLException;
import java.util.List;

public interface PortfolioDao {
    List<Portfolio> getPortfolio(String clientId) throws SQLException, ClientNotFoundException;

    void addToPortfolio(Portfolio dummyPortfolio, String clientId);
    void upDateportfolio(Portfolio portfolio, String clientId);
}
