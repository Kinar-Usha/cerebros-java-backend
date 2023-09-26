package com.cerebros.integration.doa;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Trade;

import java.sql.SQLException;
import java.util.List;

public interface TradesDao {
    List<Trade>  getTrades(String clientId) throws SQLException, ClientNotFoundException;
    void addTrade(Trade trade, String clientId);
}
