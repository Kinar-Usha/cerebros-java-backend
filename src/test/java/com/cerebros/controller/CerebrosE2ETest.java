package com.cerebros.controller;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Order;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;
import com.cerebros.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import com.cerebros.models.Order;

import com.cerebros.models.Preferences;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CerebrosE2ETest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
	

    private Person person;
    private ClientIdentification clientIdentification;
    private Set<ClientIdentification> clientIdentifications;
    private Client client;
    private Preferences preferences;

    @Rollback
    @Test

    @BeforeEach
    void setUp() throws Exception {
        // Sample client data
        person = new Person("Bhavesh", "bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

        clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
                "333-22-4444");
        clientIdentifications = new HashSet<ClientIdentification>();
        clientIdentifications.add(clientIdentification);

        preferences = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");

        client = new Client("123", person, clientIdentifications);

    }

    public void testTradeExecution() {
        Order order = new Order("PQR1045", new BigDecimal("30.0"), new BigDecimal("104.25"), "B", "YOUR_CLIENTID",
                "N123456");
        ResponseEntity<DatabaseRequestResult> responseEntity = restTemplate.postForEntity("/trade", order,
                DatabaseRequestResult.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetTradeHistory() {
        ResponseEntity<List> response = restTemplate.getForEntity("/tradehistory/YOUR_CLIENTID", List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPrices() {
        ResponseEntity<List> response = restTemplate.getForEntity("/prices", List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPortfolio() {
        ResponseEntity<List> response = restTemplate.getForEntity("/portfolio/YOUR_CLIENTID", List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testLogin_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@gmail.com");
        loginRequest.setPassword("1234567890");

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity("/client/login", loginRequest,
                HashMap.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testLogin_unauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@gmail.com");
        loginRequest.setPassword("INVALID_PASSWORD");

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity("/client/login", loginRequest,
                HashMap.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // @Test
    // void registerValidClient() throws Exception {
    //     ClientRegisterRequest clientRequest = new ClientRegisterRequest(person, clientIdentifications, "zxcvbnm");
    //     ResponseEntity<Void> response = restTemplate.exchange("/client/register", HttpMethod.PUT, new HttpEntity<>(clientRequest), Void.class);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }
    
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
