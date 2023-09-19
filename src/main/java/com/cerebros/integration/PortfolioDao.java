package com.cerebros.integration;

import com.cerebros.models.Portfolio;

import java.sql.SQLException;
import java.util.List;

public interface PortfolioDao {
    List<Portfolio> getPortfolio() throws SQLException;

}
