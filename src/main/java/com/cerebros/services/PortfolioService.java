package com.cerebros.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cerebros.exceptions.PortfolioNotFoundException;
import com.cerebros.models.Portfolio;

public class PortfolioService {
	private final ClientService clientService = new ClientService();
	private Map<String, List<Portfolio>> clientPortfolios;

	public PortfolioService() {
		this.clientPortfolios = new HashMap<>();
	}

	public PortfolioService(Map<String, List<Portfolio>> clientPortfolios) {
		this.clientPortfolios = clientPortfolios;
	}

	public List<Portfolio> getPortfolio(String clientEmail) {
		if (clientService.verifyEmailAddress(clientEmail)) {
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
}
