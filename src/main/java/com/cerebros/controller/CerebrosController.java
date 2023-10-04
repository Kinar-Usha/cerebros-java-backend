package com.cerebros.controller;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.models.Trade;
import com.cerebros.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Trade> trades= tradeService.getClientTradeHistory(clientId);
            if(trades.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            else{
                return  ResponseEntity.status(HttpStatus.OK).body(trades);
            }
        }catch (DatabaseException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @PostMapping("/trade")
    public ResponseEntity<DatabaseRequestResult> addTrade(@RequestBody Trade trade){
        ResponseEntity<DatabaseRequestResult> response;
        int count=0;
        try{
            if(trade==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            count= tradeService.updateClientTradeHistory(trade);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
        if(count!=0) {
            response=ResponseEntity.status(HttpStatus.OK).body(new DatabaseRequestResult(count));
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

}