package com.cerebros.controller;

import com.cerebros.services.PortfolioService;
import com.cerebros.services.TradeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.cerebros.models.Trade;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;



import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMybatis
@WebMvcTest
public class CerebrosWebMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService mockTradeService;
    
    @MockBean
    private PortfolioService mockPortfolioService;

    @Test
    public void testTraes() throws Exception {
        when(mockTradeService.getClientTradeHistory("YOUR_CLIENTID"))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get("/tradehistory/YOUR_CLIENTID"))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testQueryTradeHistory() throws Exception {

        List<Trade> tradeList = new ArrayList<>();
        when(mockTradeService.getClientTradeHistory("YOUR_CLIENTID")).thenReturn(tradeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tradehistory/YOUR_CLIENTID"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty()); 
    }

    @Test
    public void testGetPortfolio() throws Exception {

        List<Portfolio> portfolioList = new ArrayList<>();
        when(mockPortfolioService.getPortfolio("YOUR_CLIENTID")).thenReturn(portfolioList);

        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/YOUR_CLIENTID"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty()); 
    }

    @Test
    public void testGetPortfolio_ClientNotFound() throws Exception {
        when(mockPortfolioService.getPortfolio("INVALID_CLIENT")).thenThrow(new ClientNotFoundException("Client Invalid"));
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/INVALID_CLIENT"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
