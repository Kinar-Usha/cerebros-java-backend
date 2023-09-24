package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Order;
import com.cerebros.models.Portfolio;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("portfolioDaoImpl")
public class PortfolioDaoImpl implements PortfolioDao{

    @Autowired
    private PortfolioMapper mapper;



    @Override
    public List<Portfolio> getPortfolio(String clientId) throws SQLException, ClientNotFoundException {
        String sql= "select i.instrumentid, i.description,i.categoryid, p.holdings, pr.bidprice from SCOTT.cerebros_portfolio p inner join SCOTT.cerebros_instruments i on p.instrumentid= i.instrumentid inner join SCOTT.cerebros_prices pr on p.instrumentId =pr.instrumentId WHERE p.clientId=? ORDER BY p.holdings DESC";
        List<Portfolio> portfolioList= mapper.getPortfolio(clientId);
        if(portfolioList.isEmpty()){
            throw new ClientNotFoundException("Client Invalid");
        }
        return portfolioList;

    }

    @Override
    public void addToPortfolio(Portfolio portfolio, String clientId) {

            String orderSql = "insert into cerebros_portfolio(portfolioid, clientid, instrumentid, holdings) values(?,?,?,?)";


    }

    @Override
    public void updateportfolio(Portfolio portfolio, String clientId) {

            String orderSql = "UPDATE Cerebros_Portfolio\n" +
                    "SET holdings = ?\n" +
                    "WHERE clientId = ?\n" +
                    "  AND instrumentId = ?";


    }
}
