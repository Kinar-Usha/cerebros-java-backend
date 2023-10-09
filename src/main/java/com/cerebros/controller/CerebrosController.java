package com.cerebros.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.cerebros.exceptions.OrderInvalidException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.models.ActivityReport;
import com.cerebros.models.Client;
import com.cerebros.models.ClientRequest;
import com.cerebros.models.Order;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Preferences;
import com.cerebros.models.Price;
import com.cerebros.models.Trade;
import com.cerebros.services.ClientService;
import com.cerebros.services.FMTSService;
import com.cerebros.services.PortfolioService;
import com.cerebros.services.ReportService;
import com.cerebros.services.TradeService;

@RestController
@RequestMapping("")
public class CerebrosController {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private FMTSService fmtsService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReportService reportService;
    
    @Autowired
    private Logger logger;


    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(value = "/ping", produces = MediaType.ALL_VALUE)
    public String ping() {
        return "Cerebros web service is alive at " + LocalDateTime.now();
    }

    // ------------------ Test for ClientService ------------------

    /*
     * This method should return false for emails that already exist in the clients.
     * It should return true for emails that do not exist in the clients and are
     * valid. This can be used during the registration process to verify that the
     * email address is not already in use.
     */
    @PostMapping(value = "/client/verifyEmail")
    public ResponseEntity<?> verifyEmailAddress(@RequestBody String email) {
        try {
            if (email.isEmpty()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            boolean result = clientService.verifyEmailAddress(email);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @GetMapping(value = "/client/{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Client client = clientService.getClient(clientId);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(client);
            }
        }catch (ClientNotFoundException e){
            logger.error("Client Not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (RuntimeException e) {
            logger.error("client run time error",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @PutMapping(value = "/client/register")
    public ResponseEntity<DatabaseRequestResult> registerClient(@RequestBody ClientRegisterRequest client) {
        ResponseEntity<DatabaseRequestResult> response;
        int count = 0;
        try {
            if (client == null) {
                return ResponseEntity.badRequest().build();
            }
            count = clientService.registerClient(client.getPerson(), client.getClientIdentifications(),
                    client.getPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ClientAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error("internal error",e);
            return ResponseEntity.internalServerError().build();
        }
        if (count != 0) {
            response = ResponseEntity.ok(new DatabaseRequestResult(count));
        } else {
            return ResponseEntity.internalServerError().build();
        }
        return response;
    }

    @GetMapping(value = "/prices")
    public ResponseEntity<List<Price>> queryPrices(){
        try{
            List<Price> prices= fmtsService.getTradesPrices();
            if(prices!=null){
                return ResponseEntity.status(HttpStatus.OK).body(prices);
            }
            else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }catch (RuntimeException e){
            logger.error("runTime",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        }
    }

    // ------------------ Test for TradeService -------------------

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
    public ResponseEntity<DatabaseRequestResult> addTrade(@RequestBody Order order){
        ResponseEntity<DatabaseRequestResult> response;
        int tradeCount=0;
        int portfolioCount=0;
        System.out.println(order);
        try{
            if(order==null || order.getOrderId() == null ||
                    order.getQuantity() == null ||
                    order.getTargetPrice() == null ||
                    order.getDirection() == null ||
                    order.getClientId() == null ||
                    order.getInstrumentId() == null ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String clientId= order.getClientId();
            Client client= clientService.getClient(clientId);
            ClientRequest clientRequest= new ClientRequest(client.getPerson().getEmail(),"","");
            System.out.println(clientRequest);
            ClientRequest clientResponse=fmtsService.getClientToken(clientRequest).getBody();

            order.setToken(clientResponse.getToken());
            Trade trade= fmtsService.executeTrade(order).getBody();
            System.out.println(trade);
            trade.setOrder(order);
            tradeCount= tradeService.updateClientTradeHistory(trade);
            if(tradeCount!=0){
               portfolioCount= portfolioService.updatePortfolio(trade);
            }
        }catch (DatabaseException e){
            logger.error("unique key constraint exception");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        }
        catch (RuntimeException e){
            logger.error("runtime exception", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } catch (OrderInvalidException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if(tradeCount!=0 && portfolioCount!=0) {
            response=ResponseEntity.status(HttpStatus.OK).body(new DatabaseRequestResult(tradeCount));
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GetMapping(value = "/portfolio/{clientId}")
    public ResponseEntity<List<Portfolio>> getPortfolio(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            List<Portfolio> portfolioList = portfolioService.getPortfolio(clientId);

            return ResponseEntity.ok(portfolioList);
        } catch (ClientNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping(value = "/client/activity/{clientId}")
    public ResponseEntity<List<String>> getClientActivity(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<String> report = reportService.generateClientActivityReport(clientId);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(report);
            }
        } catch (ClientNotFoundException e) {
            logger.error("Client Report Not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.error("Client runtime error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping(value = "/roboadvisor/{clientId}")
    public ResponseEntity<List<Trade>> getRoboAdvisorStocks(@RequestBody Preferences preferences,@PathVariable String clientId) {
        try {
            List<Trade> topBuyAndSellTrades = tradeService.getTopBuyAndSellTrades(preferences, clientId);

            return ResponseEntity.ok(topBuyAndSellTrades.subList(0, Math.min(topBuyAndSellTrades.size(), 5))); // Return the top 5 trades
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    
    
    @GetMapping(value = "/client/preferences/{clientId}")
    public ResponseEntity<Preferences> getPreferences(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Preferences preferenceList = clientService.getPreferences(clientId);

            return ResponseEntity.ok(preferenceList);
        } catch (ClientNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    @PostMapping(value = "/client/add/preferences/{clientId}")
    public ResponseEntity<?> addPreferences(@PathVariable String clientId, @RequestBody Preferences preferences) {
        try {
            if (preferences==null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            int result = clientService.addPreferences(clientId,preferences);
            if (result==1) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    
    @PutMapping(value = "/client/update/preferences/{clientId}")
    public ResponseEntity<?> updatePreferences(@PathVariable String clientId, @RequestBody Preferences preferences) {
        try {
            if (preferences==null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            int result = clientService.updatePreferences(clientId,preferences);
            if (result==1) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }





    @GetMapping("/token")
    public ResponseEntity<ClientRequest> getToken(){
        try {
            ClientRequest tclinet =new ClientRequest();
            tclinet.setEmail("kinar@gmail.com");
            tclinet.setClientId("");
            tclinet.setToken("");

            System.out.println("hello");

            ClientRequest response= fmtsService.getClientToken(tclinet).getBody();
                return  ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (DatabaseException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (RuntimeException e){
            logger.error("error",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }


}
