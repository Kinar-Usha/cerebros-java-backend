package com.cerebros.controller;

import com.cerebros.services.TradeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;



import java.util.ArrayList;

import static org.mockito.Mockito.when;

@AutoConfigureMybatis
@WebMvcTest
public class CerebrosWebMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService mockTradeService;

    @Test
    public void testTraes() throws Exception {
        when(mockTradeService.getClientTradeHistory("YOUR_CLIENTID"))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get("/tradehistory/YOUR_CLIENTID"))
                .andExpect(status().isNoContent());
    }
}
