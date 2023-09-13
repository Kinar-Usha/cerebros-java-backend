package com.cerebros.services;

import com.cerebros.exceptions.PortfolioNotFoundException;
import com.cerebros.models.Portfolio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioService {
    private final ClientService clientService= new ClientService();
    private Map<String, List<Portfolio>> clientPortfolios;
    public void setupdDummyPortfolio(){
        Portfolio portfolio1= new Portfolio("12345","testDesc", "GOVT", new BigDecimal("100"), new BigDecimal("20.00"));
        Portfolio portfolio2= new Portfolio("2345","testDesc", "CORP", new BigDecimal("300"), new BigDecimal("20.00"));
        Portfolio portfolio3= new Portfolio("345","testDesc", "CD", new BigDecimal("200"), new BigDecimal("20.00"));
        List<Portfolio> portfolioList= new ArrayList<>();
        portfolioList.add(portfolio1);
        portfolioList.add(portfolio2);
        portfolioList.add(portfolio3);
        clientPortfolios.put("a@bc.com", portfolioList);
        clientPortfolios.put("b@bc.com", portfolioList);
        clientPortfolios.put("c@bc.com", portfolioList);

    }

    public PortfolioService() {
    this.clientPortfolios=new HashMap<>();
    }

    public PortfolioService(Map<String, List<Portfolio>> clientPortfolios) {
        this.clientPortfolios = clientPortfolios;
    }

    public List<Portfolio> getPortfolio(String clientEmail){
        if(clientService.verifyEmailAddress(clientEmail)){
            List<Portfolio> clientPortfoliosList = clientPortfolios.get(clientEmail);
            if (clientPortfoliosList == null || clientPortfoliosList.isEmpty()) {
                throw new PortfolioNotFoundException(clientEmail);
            }

            return clientPortfoliosList;
        }
        else {
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
}
