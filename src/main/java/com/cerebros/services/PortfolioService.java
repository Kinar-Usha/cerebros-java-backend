package com.cerebros.services;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.integration.doa.PortfolioDao;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {
    private final PortfolioDao portfolioDao;
    List<Portfolio> portfolioList;
    private Map<String, List<Portfolio>> clientPortfolios;

    @Autowired
    public PortfolioService(PortfolioDao portfolioDao) {
        this.portfolioDao = portfolioDao;
    }


    public List<Portfolio> getPortfolio(String clientID) {
        try {
            List<Portfolio> portfolioList = portfolioDao.getPortfolio(clientID);
            if (portfolioList.isEmpty()) {
                throw new ClientNotFoundException("Client Invalid");
            }
            return portfolioList;
        } catch (ClientNotFoundException ex) {
            throw new ClientNotFoundException("Client Invalid");
        }

	}
public int updatePortfolio(Trade trade) {
    String clientId = trade.getClientid();
    String instrumentId = trade.getInstrumentId();
    int rows=0;
    List<Portfolio> portfolioList = portfolioDao.getPortfolio(clientId);

    Portfolio portfolioItem = findPortfolioItemByInstrumentId(portfolioList, instrumentId);

    if (portfolioItem != null) {
        updateExistingPortfolioItem(trade, portfolioItem);
         rows=portfolioDao.updatePortfolio(portfolioItem, clientId);
    } else {
        if ("B".equals(trade.getDirection())) {
            createNewPortfolioItem(trade, clientId);
        } else {
            throw new RuntimeException("No Item in Portfolio to Sell");
        }
    }
    return  rows;
}

    private Portfolio findPortfolioItemByInstrumentId(List<Portfolio> portfolioList, String instrumentId) {
        return portfolioList.stream()
                .filter(item -> item.getInstrumentId().equals(instrumentId))
                .findFirst()
                .orElse(null);
    }
/**
 * NOTE Portfolio needs Description and categoryID
 *
 * **/
    private void updateExistingPortfolioItem(Trade trade, Portfolio portfolioItem) {
        BigDecimal totalValueBeforeTrade = portfolioItem.getHoldings().multiply(portfolioItem.getPrice());
        BigDecimal totalTradeValue = trade.getQuantity().multiply(trade.getExecutionPrice());

        if ("B".equals(trade.getDirection())) {
            portfolioItem.setPrice((totalValueBeforeTrade.add(totalTradeValue)).divide((portfolioItem.getHoldings().add(trade.getQuantity())), 1, RoundingMode.HALF_UP));
            portfolioItem.setHoldings(portfolioItem.getHoldings().add(trade.getQuantity()));
        } else if ("S".equals(trade.getDirection())) {
            BigDecimal remainingHoldings = portfolioItem.getHoldings().subtract(trade.getQuantity());
            if (remainingHoldings.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Not enough holdings to sell.");
            }
            if (remainingHoldings.compareTo(BigDecimal.ZERO) == 0) {
                portfolioItem.setPrice(BigDecimal.ZERO);
                portfolioItem.setHoldings(BigDecimal.ZERO);
            } else {
                portfolioItem.setPrice((totalValueBeforeTrade.subtract(totalTradeValue)).divide(portfolioItem.getHoldings().subtract(trade.getQuantity()), 2, RoundingMode.HALF_UP));
                portfolioItem.setHoldings(remainingHoldings);
            }

        }
    }

    private void createNewPortfolioItem(Trade trade, String clientId) {
        Portfolio newItem = new Portfolio(trade.getInstrumentId(), "", "", trade.getQuantity(), trade.getExecutionPrice());
        portfolioDao.addToPortfolio(newItem, clientId);
    }

}
