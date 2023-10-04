package com.cerebros.services;

import com.cerebros.models.ClientRequest;
import com.cerebros.models.Order;
import com.cerebros.models.Price;
import com.cerebros.models.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FMTSService {
    @Value("${fmts.api.url}")
    private String fmtsApiUrl="http://localhost:3000";

    private final RestTemplate restTemplate;
    @Autowired
    public FMTSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<Price> getTradesPrices() {
        String apiUrl = fmtsApiUrl + "fmts/trades/prices";
        Price[] pricesArray = restTemplate.getForObject(apiUrl, Price[].class);
        System.out.print(pricesArray);
        return Arrays.asList(pricesArray);
    }
    public ResponseEntity<ClientRequest> getClientToken(ClientRequest request) {
        String apiUrl = fmtsApiUrl + "fmts/client";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ClientRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ClientRequest clientRequest = objectMapper.readValue(responseBody, ClientRequest.class);
                System.out.println(clientRequest);
                return new ResponseEntity<>(clientRequest, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to get client token. HTTP status code: " + responseEntity.getStatusCode());
        }
    }
    public ResponseEntity<Trade> executeTrade(Order order){
        String apiUrl = fmtsApiUrl + "fmts/trades/trade";
        HttpHeaders headers = new HttpHeaders();
        System.out.println(order);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println(responseBody);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Trade trade = objectMapper.readValue(responseBody, Trade.class);
                System.out.println(trade);
                return new ResponseEntity<>(trade, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to get client token. HTTP status code: " + responseEntity.getStatusCode());
        }


    }
}