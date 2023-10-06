package com.cerebros.services;

import com.cerebros.models.ClientRequest;
import com.cerebros.models.Instrument;
import com.cerebros.models.Price;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class FMTSServiceTest {
//    @Autowired
//    private FMTSService fmtsService;
//
//    @Autowired
//    private RestTemplate restTemplate;
    @Mock
    private RestTemplate restTemplate;


    private FMTSService fmtsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fmtsService= new FMTSService(restTemplate);
    }
    @Test
    void getTradesPrices_shouldReturnListOfPrices() throws JsonProcessingException {

        Instrument instrOne = new Instrument("N123456","CUSIP","46625H100", "STOCK", "JPMorgan Chase & Co. Capital Stock",new BigDecimal("1000"), BigDecimal.ONE);
        List<Price> samplePrices = getSamplePrices(instrOne);
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the list of Price objects to a JSON string
        String json = objectMapper.writeValueAsString(samplePrices);

        String apiUrl = "http://localhost:3000/fmts/trades/prices";

        when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(json);

        List<Price> result = fmtsService.getTradesPrices();
        System.out.println(result);



        assertEquals(samplePrices, result);
    }

    private static List<Price> getSamplePrices(Instrument instrOne) {
        Instrument instrTwo = new Instrument("T67890","CUSIP","9128285M8","GOVT","USA, Note 3.125 15nov2028 10Y",new BigDecimal("10000"),BigDecimal.ONE);
        List<Price> samplePrices = List.of(new Price(new BigDecimal("104.75"), new BigDecimal("104.25"),  "21-AUG-19 10.00.01.042000000 AM GMT", instrOne),
                new Price(new BigDecimal("1.03375"),new BigDecimal("1.03325") , "21-AUG-19 10.00.01.042000000 AM GMT", instrTwo));
        return samplePrices;
    }

    //    @Test
@Test
void getClientToken_shouldReturnClientRequest()  {
    // Create a mock response body
    String responseBody = "{\"clientId\": \"12345\", \"email\": \"Test Client\",\"token\": \"1234\"}";
    ClientRequest clientRequest= new ClientRequest("Test Client", "12345", "1234");

    // Mock the HTTP response
    ResponseEntity<ClientRequest> mockResponseEntity = new ResponseEntity<>(clientRequest, HttpStatus.OK);

    // Mock the restTemplate.exchange method
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ClientRequest.class)))
            .thenReturn(mockResponseEntity);

    // Create a ClientRequest object for the request
    ClientRequest request = new ClientRequest(/* Set request parameters */);

    // Call the getClientToken method
    ResponseEntity<ClientRequest> result = fmtsService.getClientToken(request);

    // Verify that restTemplate.exchange was called with the correct arguments
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ClientRequest.class));

    // Verify that the result contains the expected ClientRequest
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("12345", result.getBody().getClientId());
    }
}
