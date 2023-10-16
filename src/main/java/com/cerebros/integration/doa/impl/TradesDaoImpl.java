package com.cerebros.integration.doa.impl;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.TradesDao;
import com.cerebros.integration.mapper.TradesMapper;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Repository("tradesDaoImpl")
public class TradesDaoImpl implements TradesDao {
    @Autowired
    private TradesMapper mapper;
    public TradesDaoImpl() {

    }

    @Override
    public List<Trade> getTrades(String clientId) {

        List<Trade> Trades= mapper.getTrades(clientId);
        
        if(Trades.isEmpty()){
            throw new DatabaseException("Client Invalid or no Trade history");
        }
        return  Trades;

    }
    @Override
    public int addTrade(Trade trade, String clientId) {
        int rows;
        try {
            if(trade.getOrder()!=null){
                mapper.insertOrder(trade.getOrder());
            }

            rows = mapper.insertTrade(trade);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to add trade", e);
        }
        return rows;
    }

}
