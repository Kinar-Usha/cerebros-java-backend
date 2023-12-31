package com.cerebros.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import com.cerebros.exceptions.*;
import com.cerebros.models.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.cerebros.services.ClientService;
import com.cerebros.services.FMTSService;
import com.cerebros.services.PortfolioService;
import com.cerebros.services.ReportService;
import com.cerebros.services.TradeService;

import oracle.jdbc.proxy.annotation.Post;



@CrossOrigin( origins = "http://localhost:4200" )
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

    // ------------------ ClientService Endpoints ------------------

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
            logger.error("Illegal arg", e);
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
        } catch (ClientNotFoundException e) {
            logger.error("Client Not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.error("client run time error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @GetMapping(value = "/client/email/{email}")
    public ResponseEntity<Client> getClientFromEmail(@PathVariable String email) {
        try {
            if (email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Client client = clientService.getClientFromEmail(email);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(client);
            }
        } catch (ClientNotFoundException e) {
            logger.error("Client Not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.error("client run time error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @PutMapping(value = "/client/register")
    public ResponseEntity<Void> registerClient(@RequestBody ClientRegisterRequest client) {
        ResponseEntity<DatabaseRequestResult> response;
        int count = 0;
        try {
            if (client == null) {
                return ResponseEntity.badRequest().build();
            }
            count = clientService.registerClient(
                    client.getPerson(),
                    client.getClientIdentifications(),
                    client.getPassword());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        } catch (ClientAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } catch (Exception e) {
            logger.error("internal error", e);
            return ResponseEntity.internalServerError().build();
        }

        if (count == 1) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping(value = "/client/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest == null) {
                return ResponseEntity.badRequest().build();
            }
            boolean result = clientService.login(loginRequest.getEmail(), loginRequest.getPassword());

            String clientId = clientService.getClientFromEmail(loginRequest.getEmail()).getClientId();

            HashMap<String, String> response = new HashMap<>();
            response.put("clientId", clientId);

            Client client = clientService.getClient(clientId);

            return ResponseEntity.ok(client);

        } catch (InvalidCredentialsException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    // ------------------ TradeService Endpoints -------------------

    @GetMapping(value = "/prices")
    public ResponseEntity<List<Price>> queryPrices() {
        try {
            List<Price> prices = fmtsService.getTradesPrices();
            if (prices != null) {
                return ResponseEntity.status(HttpStatus.OK).body(prices);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (RuntimeException e) {
            logger.error("runTime", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @CrossOrigin("")
    @GetMapping(value = "/tradehistory/{clientId}")
    public ResponseEntity<List<Trade>> queryTradeHistory(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Trade> trades = tradeService.getClientTradeHistory(clientId);
            if (trades.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(trades);
            }
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }
    @GetMapping("/cash/{clientId}")
    public ResponseEntity<?> addTrade(@PathVariable String clientId){
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Cash cash=clientService.getCash(clientId);
            if(cash==null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            else{
                return  ResponseEntity.status(HttpStatus.OK).body(cash);
            }
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }
    @PostMapping("/updateCash")
    public ResponseEntity<?> addCash(@RequestBody CashRequest cash){
       int  rows=0;
        try{
            rows=portfolioService.updateCash(cash.getClientId(), cash.getCashRemaining());
        }catch (Exception e){
            return  ResponseEntity.internalServerError().body("cash Update failed");
        }
        return ResponseEntity.ok(rows);
    }


    @Transactional
    @PostMapping("/trade")
    public ResponseEntity<?> addTrade(@RequestBody Order order) {
        ResponseEntity<DatabaseRequestResult> response;
        int tradeCount = 0;
        int portfolioCount = 0;
        int cashCount=0;
        System.out.println(order);
        try {
            if (order == null || order.getOrderId() == null ||
                    order.getQuantity() == null ||
                    order.getTargetPrice() == null ||
                    order.getDirection() == null ||
                    order.getClientId() == null ||
                    order.getInstrumentId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Portfolio> portfolios;
            BigDecimal cashcheck= clientService.getCash(order.getClientId()).getCashRemaining();
            if((order.getQuantity().multiply(order.getTargetPrice())).compareTo(cashcheck)>0){
                if(order.getDirection().equals("B")) {
                    throw new RuntimeException("not enough cash");
                }
            }
            try {
               portfolios = portfolioService.getPortfolio(order.getClientId());

            }catch (ClientNotFoundException e){
                portfolios=new ArrayList<>();
                logger.error(e.getMessage());
            }
            Portfolio portfolioItem= portfolioService.findPortfolioItemByInstrumentId(portfolios, order.getInstrumentId());
            BigDecimal holding;
            if(portfolioItem==null){
                holding=BigDecimal.ZERO;
            }
            else {
                holding=portfolioItem.getHoldings();
            }
            if(order.getQuantity().compareTo(holding)>0){
                System.out.println(order.getDirection());
                if(Objects.equals(order.getDirection(), "S")){
                    throw new RuntimeException("No Item in Portfolio to Sell");
                }
            }

            String orderId= UUID.randomUUID().toString();
            order.setOrderId(orderId);
            String clientId = order.getClientId();
            Client client = clientService.getClient(clientId);
            ClientRequest clientRequest = new ClientRequest(client.getPerson().getEmail(), "", "");
            System.out.println(clientRequest);
            ClientRequest clientResponse = fmtsService.getClientToken(clientRequest).getBody();

            order.setToken(clientResponse.getToken());
            Trade trade = fmtsService.executeTrade(order).getBody();
            System.out.println(trade);
            trade.setOrder(order);
            Instrument instrument= fmtsService.getInstruments().stream() .filter(item -> item.getInstrumentId().equals(trade.getInstrumentId()))
                    .findFirst()
                    .orElse(null);;
            if (instrument != null) {
                trade.setDescription(instrument.getInstrumentDescription());
            }
            portfolioCount = portfolioService.updatePortfolio(trade);
            if (portfolioCount != 0) {
                tradeCount = tradeService.updateClientTradeHistory(trade);
                if(tradeCount!=0){
                    BigDecimal cash= clientService.getCash(order.getClientId()).getCashRemaining();
                    System.out.println(trade.getCashValue());
                    System.out.println(cash);

                    if(cash!=null){
                        cashCount= portfolioService.updateCash(order.getClientId(), cash, trade.getCashValue(), trade.getDirection() );
                        System.out.println(cashCount);

                    }
                }

            }
        }
        catch (OrderInvalidException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        catch (DatabaseException e) {
            logger.error("unique key constraint exception");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
        catch (RuntimeException e){
            if(Objects.equals(e.getMessage(), "No Item in Portfolio to Sell")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough holdings");
            } else if (Objects.equals(e.getMessage(), "not enough cash")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Not Enough Cash"); // Change the response body here
            } else if (Objects.equals(e.getMessage(), "execution price not proper")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid price/ Invalid quantity");
            }
        }
        if (tradeCount != 0 && cashCount != 0) {
            response = ResponseEntity.status(HttpStatus.OK).body(new DatabaseRequestResult(tradeCount));
        } else {
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
    public ResponseEntity<List<Trade>> getClientActivity(@PathVariable String clientId) {
        try {
            if (clientId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Trade> report = reportService.generateClientActivityReport(clientId);
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

    @GetMapping(value = "/roboadvisor/preferences/{clientId}")
    public ResponseEntity<List<Price>> getRoboAdvisorStocks(@RequestParam String risk,
            @RequestParam String time,
            @RequestParam String income,@PathVariable String clientId) {
        try {
                Preferences preferences = new Preferences();
                preferences.setRisk(risk);
                preferences.setTime(time);
                preferences.setIncome(income);
                logger.debug("{}",preferences.getRisk());
            List<Price> topBuyAndSellTrades = tradeService.getTopBuyAndSellTrades(preferences,clientId);
            return ResponseEntity.ok(topBuyAndSellTrades);                                                                            // the
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
            if (preferences == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            int result = clientService.addPreferences(clientId, preferences);
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @PutMapping(value = "/client/update/preferences/{clientId}")
    public ResponseEntity<?> updatePreferences(@PathVariable String clientId, @RequestBody Preferences preferences) {
        try {
            if (preferences == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            int result = clientService.updatePreferences(clientId, preferences);
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arg", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @GetMapping("/token")
    public ResponseEntity<ClientRequest> getToken() {
        try {
            ClientRequest tclinet = new ClientRequest();
            tclinet.setEmail("kinar@gmail.com");
            tclinet.setClientId("");
            tclinet.setToken("");

            System.out.println("hello");

            ClientRequest response = fmtsService.getClientToken(tclinet).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.error("error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

}
