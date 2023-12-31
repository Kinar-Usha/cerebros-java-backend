package com.cerebros.integration.doa.impl;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.PortfolioDao;
import com.cerebros.integration.mapper.PortfolioMapper;
import com.cerebros.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("portfolioDaoImpl")
public class PortfolioDaoImpl implements PortfolioDao {

    @Autowired
    private PortfolioMapper mapper;



    @Override
    public List<Portfolio> getPortfolio(String clientId) throws ClientNotFoundException {
        List<Portfolio> portfolioList= mapper.getPortfolio(clientId);
        if(portfolioList.isEmpty()){
            throw new ClientNotFoundException("Client Invalid");
        }
        return portfolioList;

    }

    @Override
    public int addToPortfolio(Portfolio portfolio, String clientId) {

        int rowsInserted=0;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("clientId", clientId);
            paramMap.put("portfolio", portfolio);
          rowsInserted=  mapper.addToPortfolio(paramMap);
        } catch (Exception e) {
           throw new DatabaseException("insert failed",e);
        }
        return rowsInserted;


    }
    @Override
    public int deletePortfolio(String clientId, String instrumentId){
        int rowsUpdated=0;
        try{
            rowsUpdated= mapper.deletePortfolio(clientId,instrumentId);
        }catch (Exception e){
            throw  new DatabaseException("delete failed");
        }
        return rowsUpdated;
    }

    @Override
    public int updatePortfolio(Portfolio portfolio, String clientId) {

        int rowsUpdated=0;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("clientId", clientId);
            paramMap.put("portfolio", portfolio);
           rowsUpdated= mapper.updatePortfolio(paramMap);

        } catch (Exception e) {
            throw new DatabaseException("update failed",e);
        }
        return rowsUpdated;

    }
    @Override
    public int updateCash(String clientId, BigDecimal cash){
        int rowsUpdated=0;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("clientId", clientId);
            paramMap.put("cashRemaining", cash);
            rowsUpdated= mapper.updateCash(paramMap);

        } catch (Exception e) {
            throw new DatabaseException("update failed",e);
        }
        return rowsUpdated;
    }
}
