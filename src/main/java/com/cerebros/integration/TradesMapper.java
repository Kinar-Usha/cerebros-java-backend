package com.cerebros.integration;

import com.cerebros.models.Trade;

import java.util.List;

public interface TradesMapper {
    List<Trade> getTrades(String clientId);
}
