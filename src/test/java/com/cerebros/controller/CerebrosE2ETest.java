package com.cerebros.controller;

import com.cerebros.models.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CerebrosE2ETest {
    @Autowired
    private TestRestTemplate restTemplate;

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
}
