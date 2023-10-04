package com.cerebros.controller;

import com.cerebros.models.Trade;
import com.cerebros.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("")
public class CerebrosController {
    @Autowired
    private TradeService tradeService;
    @GetMapping(value="/ping",
            produces= MediaType.ALL_VALUE)
    public String ping() {
        return "Cerebros web service is alive at " + LocalDateTime.now();
    }

    @GetMapping(value = "/tradehistory/{clientId}")
    public ResponseEntity< List<Trade>> queryTradeHistory(@PathVariable String clientId){
        List<Trade> trades= tradeService.getClientTradeHistory(clientId);
        return  ResponseEntity.status(HttpStatus.OK).body(trades);
    }
}
