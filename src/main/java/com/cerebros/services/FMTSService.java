package com.cerebros.services;

import com.cerebros.exceptions.OrderInvalidException;
import com.cerebros.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        String apiUrl = fmtsApiUrl + "/fmts/trades/prices";



        // Make the GET request and retrieve the response
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        System.out.println(jsonResponse);

        // Deserialize the JSON response into a list of Price objects
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Price>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
        }
    }

    public List<Instrument> getInstruments(){
        String apiUrl= fmtsApiUrl+"/fmts/trades/instruments";
        String jsonResponse= restTemplate.getForObject(apiUrl,String.class);
        ObjectMapper objectMapper= new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Instrument>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
        }
    }
    public ResponseEntity<ClientRequest> getClientToken(ClientRequest request) {
        String apiUrl = fmtsApiUrl + "/fmts/client";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ClientRequest clientRequest = objectMapper.readValue(responseBody, ClientRequest.class);
                System.out.println(responseBody);
                return new ResponseEntity<>(clientRequest, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to get client token. HTTP status code: " + responseEntity.getStatusCode());
        }
    }
    public ResponseEntity<Trade> executeTrade(Order order) throws OrderInvalidException {
        String apiUrl = fmtsApiUrl + "fmts/trades/trade";
        HttpHeaders headers = new HttpHeaders();
        System.out.println(order);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            String responseBody = responseEntity.getBody();
            if(Objects.equals(responseBody, "null")){
                throw new RuntimeException("execution price not proper");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Trade trade = objectMapper.readValue(responseBody, Trade.class);
                System.out.println(trade);
                return new ResponseEntity<>(trade, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
            }
        }else if (responseEntity.getStatusCode().value()==HttpStatus.CONFLICT.value()){
            throw new OrderInvalidException("49 error");
        } else if (responseEntity.getStatusCode()==HttpStatus.NOT_ACCEPTABLE) {
            throw new RuntimeException("execution price not proper");
        } else {
            throw new RuntimeException("Failed to get client token. HTTP status code: " + responseEntity.getStatusCode());
        }


    }
}
