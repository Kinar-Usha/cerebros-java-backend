package com.cerebros.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.cerebros.models.Order;

import com.cerebros.models.Preferences;

@Transactional
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CerebrosE2ETest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
	

    @Rollback
    @Test

    public void testTradeExecution(){
        Order order= new Order("PQR1045",new BigDecimal("30.0"),new BigDecimal("104.25"),"B","YOUR_CLIENTID","N123456");
        ResponseEntity<DatabaseRequestResult> responseEntity=restTemplate.postForEntity("/trade",order, DatabaseRequestResult.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    @Test
    public void testGetTradeHistory(){
        ResponseEntity<List> response= restTemplate.getForEntity("/tradehistory/YOUR_CLIENTID",List.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetPrices(){
        ResponseEntity<List> response=restTemplate.getForEntity("/prices",List.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetPortfolio(){
        ResponseEntity<List> response=restTemplate.getForEntity("/portfolio/YOUR_CLIENTID",List.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
	public void testGetForClientPreferences() {
		ResponseEntity<Preferences> response = restTemplate.getForEntity( "/client/preferences/YOUR_CLIENTID",Preferences.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}
    
    @Test
    public void testAddClientPreferences(){
        Preferences preference = new Preferences("Investment","High","Long-term","High");
        ResponseEntity<DatabaseRequestResult> responseEntity=restTemplate.postForEntity( "/client/add/preferences/YOUR_CLIENTID1",preference, DatabaseRequestResult.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    
    @Test
    public void testUpdateClientPreferences(){
        Preferences preference = new Preferences("Investment","High","Long-term","High");
    	String resourceUrl =  "/client/add/preferences/YOUR_CLIENTID1";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Preferences> requestUpdate = new HttpEntity<>(preference, headers);
    	restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Preferences.class);
    }
}
