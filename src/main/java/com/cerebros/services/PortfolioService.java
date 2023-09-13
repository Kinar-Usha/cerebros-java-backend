package com.cerebros.services;

import com.cerebros.exceptions.PortfolioNotFoundException;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioService {
    List<Portfolio> portfolioList= new ArrayList<>();

    private final ClientService clientService= new ClientService();
    private final Map<String, List<Portfolio>> clientPortfolios;
    public void setupDummyPortfolio(){
        Portfolio portfolio1= new Portfolio("12345","testDesc", "GOVT",  BigDecimal.ONE,  BigDecimal.TEN);
        Portfolio portfolio2= new Portfolio("2345","testDesc", "CORP", new BigDecimal("300"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
        Portfolio portfolio3= new Portfolio("345","testDesc", "CD", new BigDecimal("200"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
        portfolioList.add(portfolio1);
        portfolioList.add(portfolio2);
        portfolioList.add(portfolio3);
        clientPortfolios.put("bhavesh@gmail.com", portfolioList);
//        clientPortfolios.put("john.doe@gmail.com", portfolioList);
        clientPortfolios.put("jane.doe@gmail.com", portfolioList);
        clientPortfolios.put("john.doe@gmail.com", new ArrayList<>());
        clientService.setupMockClients();

    }

    public PortfolioService() {
    this.clientPortfolios=new HashMap<>();
    }

    public PortfolioService(Map<String, List<Portfolio>> clientPortfolios) {
        this.clientPortfolios = clientPortfolios;
    }

    public List<Portfolio> getPortfolio(String clientEmail){
        if(!clientService.verifyEmailAddress(clientEmail)){
            List<Portfolio> clientPortfoliosList = clientPortfolios.get(clientEmail);
            if (clientPortfoliosList == null || clientPortfoliosList.isEmpty()) {
                throw new PortfolioNotFoundException(clientEmail);
            }

			return clientPortfoliosList;
		} else {
			throw new IllegalArgumentException("User not found");
		}
	}
//    public void addPortfolio(String clientId, Portfolio newPortfolio) {
//        if(clientService.verifyClientId(clientId)){
//            clientPortfolios.computeIfAbsent(clientId, k -> new ArrayList<>());
//            List<Portfolio> clientPortfoliosList = clientPortfolios.get(clientId);
//            boolean existingInstrumentId = false;
//
//            for (Portfolio portfolio : clientPortfoliosList) {
//                if (portfolio.getInstrumentId().equals(newPortfolio.getInstrumentId())) {
//                    portfolio.setHoldings(portfolio.getHoldings().add(newPortfolio.getHoldings()));
//                    existingInstrumentId = true;
//                    break;
//                }
//            }
//            if (!existingInstrumentId) {
//                clientPortfoliosList.add(newPortfolio);
//            }
//        }
//    }
public void updatePortfolio(Trade trade) {
    Portfolio portfolioItem = null;

    List<Portfolio> mockPortfolioData= clientPortfolios.get(clientService.getClient(trade.getClientid()).getPerson().getEmail());
    for (Portfolio item : mockPortfolioData) {
        if (item.getInstrumentId().equals(trade.getInstrumentId())) {
            portfolioItem = item;
            break;
        }
    }

    if (portfolioItem != null) {
        if (trade.getDirection().equals("B")) {
            BigDecimal totalValueBeforeTrade = portfolioItem.getHoldings().multiply( portfolioItem.getPrice());
            BigDecimal totalTradeValue = trade.getQuantity().multiply( trade.getExecutionPrice());
            portfolioItem.setHoldings( portfolioItem.getHoldings().add( trade.getQuantity()));
            portfolioItem.setPrice(totalValueBeforeTrade.add(totalTradeValue).divide(portfolioItem.getHoldings().add(trade.getQuantity()), 1, RoundingMode.HALF_UP));
        } else if (trade.getDirection().equals("S")) {
            BigDecimal totalValueBeforeTrade = portfolioItem.getHoldings().multiply( portfolioItem.getPrice());
            BigDecimal totalTradeValue = trade.getQuantity().multiply( trade.getExecutionPrice());
            portfolioItem.setHoldings( portfolioItem.getHoldings().subtract( trade.getQuantity()));
            portfolioItem.setPrice((totalValueBeforeTrade.subtract( totalTradeValue)).divide (portfolioItem.getHoldings().subtract( trade.getQuantity()),2, RoundingMode.HALF_UP));
            if (portfolioItem.getHoldings().equals(BigDecimal.ZERO) ) {
                mockPortfolioData.removeIf(item -> item.getInstrumentId().equals(trade.getInstrumentId()));
            }
        }
    } else {
        if (trade.getDirection().equals("B")) {
            Portfolio newItem = new Portfolio(trade.getInstrumentId(),"","",trade.getQuantity(),trade.getExecutionPrice());
            mockPortfolioData.add(newItem);

            // Call to get instruments and add to mockPortfolioData here
            // You would need to implement this logic
        } else {
            throw new RuntimeException("No Item in Portfolio to Sell");
        }
    }
    clientPortfolios.put(trade.getClientid(),mockPortfolioData);
    }
}
