package com.cerebros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cerebros.integration.doa.TradesDao;
import com.cerebros.models.Trade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final TradesDao tradesDao;
    @Autowired
    public ReportService(TradesDao tradesDao) {
        this.tradesDao = tradesDao;
    }
	public List<Trade> generateClientActivityReport(String clientId) {
		  return tradesDao.getTrades(clientId).stream()
	                .sorted(Comparator.comparing(Trade::getTradeId))
	                .limit(100)
	                .collect(Collectors.toList());
    }
}
