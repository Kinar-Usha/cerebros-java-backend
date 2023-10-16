package com.cerebros.integration.mapper;

import com.cerebros.models.Portfolio;
import org.apache.ibatis.annotations.Delete;

import java.util.List;
import java.util.Map;

public interface PortfolioMapper {
    public List<Portfolio> getPortfolio(String clientId);



    int addToPortfolio(Map<String, Object> paramMap);

    int updatePortfolio(Map<String, Object> paramMap);
    int updateCash(Map<String,Object> paramMap);

    @Delete("Delete from cerebros_portfolio where clientID=#{clientId} and instrumentid=#{instrumentId}")
    int deletePortfolio(String clientId, String instrumentId);
}
