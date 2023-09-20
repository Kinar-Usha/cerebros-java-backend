package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Order;
import com.cerebros.models.Portfolio;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PortfolioDaoImpl implements PortfolioDao{

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private DataSource dataSource;

    public PortfolioDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Portfolio> getPortfolio(String clientId) throws SQLException, ClientNotFoundException {
        String sql= "select i.instrumentid, i.description,i.categoryid, p.holdings, pr.bidprice from SCOTT.cerebros_portfolio p inner join SCOTT.cerebros_instruments i on p.instrumentid= i.instrumentid inner join SCOTT.cerebros_prices pr on p.instrumentId =pr.instrumentId WHERE p.clientId=? ORDER BY p.holdings DESC";
        List<Portfolio> portfolioList= new ArrayList<>();
        PreparedStatement statement= null;
        Connection conn= dataSource.getConnection();
        try{
            statement = conn.prepareStatement(sql);
            statement.setString(1,clientId);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                // Log client not found and throw custom exception
                logger.error("Client with ID " + clientId + " not found in the database");
                throw new ClientNotFoundException("Client with ID " + clientId + " not found");
            }
            do {
                String instrumentId= rs.getString(1);
                String desc = rs.getString(2);
                String categoryId= rs.getString(3);
                BigDecimal holdings= rs.getBigDecimal(4).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal price= rs.getBigDecimal(5).setScale(2, RoundingMode.HALF_EVEN);
                Portfolio portfolio= new Portfolio(instrumentId,desc,categoryId,holdings,price);
                portfolioList.add(portfolio);
            }
            while (rs.next());
            logger.info("Client portfolio successfully retrieved for ID " + clientId);

        }catch (SQLException e){
            logger.error("get portfolio failed", e);
            throw new DatabaseException("get Portfolio failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("can't close connection", e);
                }
            }
        }
        return  portfolioList;
    }

    @Override
    public void addToPortfolio(Portfolio portfolio, String clientId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            String orderSql = "insert into cerebros_portfolio(portfolioid, clientid, instrumentid, holdings) values(?,?,?,?)";

            stmt = conn.prepareStatement(orderSql);
            stmt.setInt(1, new java.util.Random().nextInt());
            stmt.setString(2,clientId);
            stmt.setString(3, portfolio.getInstrumentId());
            stmt.setBigDecimal(4, portfolio.getHoldings());
            stmt.executeUpdate();
        } catch (SQLException e){
            logger.error("add portfolio failed", e);
            throw new DatabaseException("add portfolio failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("can't close connection", e);
                }
            }
        }
    }

    @Override
    public void upDateportfolio(Portfolio portfolio, String clientId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            String orderSql = "UPDATE Cerebros_Portfolio\n" +
                    "SET holdings = ?\n" +
                    "WHERE clientId = ?\n" +
                    "  AND instrumentId = ?";

            stmt = conn.prepareStatement(orderSql);
            stmt.setBigDecimal(1, portfolio.getHoldings());
            stmt.setString(2,clientId);
            stmt.setString(3, portfolio.getInstrumentId());
            int rowsUpdated=stmt.executeUpdate();
            if(rowsUpdated==0){
                throw new DatabaseException("No row with such client id or instrumentId");
            }
        } catch (SQLException e){
            logger.error("add portfolio failed", e);
            throw new DatabaseException("add portfolio failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("can't close connection", e);
                }
            }
        }

    }
}
