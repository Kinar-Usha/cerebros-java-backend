package com.cerebros.controller;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;
import com.cerebros.services.ClientService;
import com.cerebros.services.PortfolioService;
import com.cerebros.services.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.cerebros.models.Trade;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMybatis
@WebMvcTest
public class CerebrosWebMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService mockTradeService;

    @MockBean
    private PortfolioService mockPortfolioService;

    @MockBean
    private ClientService mockClientService;

    private Person person;
    private ClientIdentification clientIdentification;
    private Set<ClientIdentification> clientIdentifications;
    private Client client;
    private Preferences preferences;

    private ObjectMapper jsonMapper;

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

        // Creating the ObjectMapper object
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    // ------------------ Test for ClientService ------------------

    @ParameterizedTest
    @ValueSource(strings = { "bhavesh@gmail.com", "john.doe@gmail.com", "jane.doe@gmail.com" })
    void verifyExistingEmailAddress(String email) throws Exception {
        when(mockClientService.verifyEmailAddress(email)).thenReturn(false);
            // Should return false for emails that already exist in the clients
            mockMvc.perform(post("/client/verifyEmail").content(email))
                    .andExpect(status().isNoContent());
    }

    @ParameterizedTest
	@ValueSource(strings = { "Kin.s@gmail.com", "k1@gmail.com", "A_kinar@yahoo.com" })
	void TestVerifyEmailAdressFormat_success(String email) throws Exception {
       when(mockClientService.verifyEmailAddress(email)).thenReturn(true);
            mockMvc.perform(post("/client/verifyEmail").content(email))
                    .andExpect(status().isOk());
	}

    @ParameterizedTest
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) throws Exception {
        when(mockClientService.verifyEmailAddress(email)).thenThrow(IllegalArgumentException.class);
            mockMvc.perform(post("/client/verifyEmail").content(email))
                    .andExpect(status().isBadRequest());
	}

    @ParameterizedTest
	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
	void verifyNonExistentEmailAddress(String email) throws Exception {
        when(mockClientService.verifyEmailAddress(email)).thenReturn(true);
            // Should return true for emails that are not registered yet
            mockMvc.perform(post("/client/verifyEmail").content(email))
                    .andExpect(status().isOk());
	}

    @Test
    void getExistingClient() throws Exception {
        when(mockClientService.getClient("123")).thenReturn(client);
        mockMvc.perform(get("/client/123"))
                .andExpect(status().isOk());
    }

    @Test
    void getNonExistentClient() throws Exception {
        when(mockClientService.getClient("123")).thenReturn(null);
        mockMvc.perform(get("/client/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void registerInvalidClientIdentification() throws Exception {
        clientIdentification.setValue("333-22-44544");
        clientIdentifications = new HashSet<ClientIdentification>();
        clientIdentifications.add(clientIdentification);

        when(mockClientService.registerClient(person, clientIdentifications, "1234"))
                .thenThrow(IllegalArgumentException.class);

        ClientRegisterRequest clientPayload = new ClientRegisterRequest(person, clientIdentifications, "1234");

        // Converting the Object to JSONString
        String jsonString = jsonMapper.writeValueAsString(clientPayload);
        System.out.println(jsonString);
        mockMvc.perform(put("/client/register").contentType(
                MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerValidClient() throws Exception {

        when(mockClientService.registerClient(person, clientIdentifications, "1234"))
                .thenReturn(1);

        ClientRegisterRequest clientPayload = new ClientRegisterRequest(person, clientIdentifications, "1234");

        // Converting the Object to JSONString
        String jsonString = jsonMapper.writeValueAsString(clientPayload);
        System.out.println(jsonString);
        mockMvc.perform(put("/client/register").contentType(
                MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk());

    }

    @Test
    void registrationFailsOnExistingClient() throws Exception {
            
        when(mockClientService.registerClient(person, clientIdentifications, "1234"))
                .thenThrow(ClientAlreadyExistsException.class);

        ClientRegisterRequest clientPayload = new ClientRegisterRequest(person, clientIdentifications, "1234");

        // Converting the Object to JSONString
        String jsonString = jsonMapper.writeValueAsString(clientPayload);
        System.out.println(jsonString);
        mockMvc.perform(put("/client/register").contentType(
                MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isConflict());
    }

    // ------------------ Test for TradeService -------------------

    @Test
    public void testTrades() throws Exception {
        when(mockTradeService.getClientTradeHistory("YOUR_CLIENTID"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/tradehistory/YOUR_CLIENTID"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testQueryTradeHistory() throws Exception {

        List<Trade> tradeList = new ArrayList<>();
        when(mockTradeService.getClientTradeHistory("YOUR_CLIENTID")).thenReturn(tradeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tradehistory/YOUR_CLIENTID"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetPortfolio() throws Exception {

        List<Portfolio> portfolioList = new ArrayList<>();
        when(mockPortfolioService.getPortfolio("YOUR_CLIENTID")).thenReturn(portfolioList);

        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/YOUR_CLIENTID"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetPortfolio_ClientNotFound() throws Exception {
        when(mockPortfolioService.getPortfolio("INVALID_CLIENT")).thenThrow(new ClientNotFoundException("Client Invalid"));
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/INVALID_CLIENT"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
