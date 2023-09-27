package com.cerebros.services;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.integration.doa.PortfolioDao;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

	@Mock
	private PortfolioDao portfolioDao;

	@InjectMocks
	private PortfolioService portfolioService;
	private List<Portfolio> portfolioList;
	private Map<String, List<Portfolio>> clientPortfolios;
	private Portfolio portfolio1= new Portfolio("12345","testDesc", "GOVT", BigDecimal.ONE,BigDecimal.TEN);
	private Portfolio portfolio2= new Portfolio("2345","testDesc", "CORP", new BigDecimal("300"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
	private Portfolio portfolio3= new Portfolio("345","testDesc", "CD", new BigDecimal("200"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));



	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		// Initialize clientPortfolios with mock data for testing
		clientPortfolios = new HashMap<>();
		List<Portfolio> portfolioList = new ArrayList<>();
		portfolioList.add(portfolio1);
		portfolioList.add(portfolio2);
		clientPortfolios.put("YOuR_CLIENTID", portfolioList);

		// You can add more mock data for other clients as needed
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testConstructor(){
		assertNotNull(portfolioService);
	}

	@Test
	void testGetPortfolioSuccess()  {
		String clientID = "YOUR_CLIENTID";

		List<Portfolio> mockPortfolioList = new ArrayList<>();
		mockPortfolioList.add(portfolio1);
		mockPortfolioList.add(portfolio2);
		// Mock behavior of PortfolioDao (if needed)
		when(portfolioDao.getPortfolio(clientID)).thenReturn(mockPortfolioList);


		List<Portfolio> result = portfolioService.getPortfolio(clientID);


		assertEquals(2, result.size());
		verify(portfolioDao, times(1)).getPortfolio(clientID);
	}
	@Test
	public void testGetPortfolioInvalidEmail() {
		// Arrange
		String clientID = "nonExistentClient";

		// Act and Assert
		assertThrows(ClientNotFoundException.class, () -> portfolioService.getPortfolio(clientID));
	}
	@Test
	public void testUpdatePortfolio_Buy() {
		// Arrange
		Trade trade = new Trade();
		trade.setClientid("client1");
		trade.setInstrumentId("instrument1");
		trade.setDirection("B");
		trade.setQuantity(BigDecimal.TEN);
		trade.setExecutionPrice(BigDecimal.valueOf(100));

		Portfolio existingPortfolio = new Portfolio("instrument1", "", "", BigDecimal.TEN, BigDecimal.valueOf(100));

		when(portfolioDao.getPortfolio("client1")).thenReturn(new ArrayList<>(List.of(existingPortfolio)));
		when(portfolioDao.updatePortfolio(any(), eq("client1"))).thenReturn(1);

		portfolioService.updatePortfolio(trade);

		verify(portfolioDao, times(1)).getPortfolio("client1");
		verify(portfolioDao, times(1)).updatePortfolio(any(), eq("client1"));
		assertEquals(BigDecimal.valueOf(20), existingPortfolio.getHoldings());
		assertEquals(
				BigDecimal.valueOf(100.0), // Replace with your expected price value
				existingPortfolio.getPrice()
		);

	}
	@Test
	public void testUpdatePortfolio_Sell() {
		// Arrange
		Trade trade = new Trade();
		trade.setClientid("client1");
		trade.setInstrumentId("instrument1");
		trade.setDirection("S");
		trade.setQuantity(BigDecimal.TEN);
		trade.setExecutionPrice(BigDecimal.valueOf(100));

		Portfolio existingPortfolio = new Portfolio("instrument1", "", "", BigDecimal.TEN, BigDecimal.valueOf(100));

		when(portfolioDao.getPortfolio("client1")).thenReturn(new ArrayList<>(List.of(existingPortfolio)));
		when(portfolioDao.updatePortfolio(any(), eq("client1"))).thenReturn(1);

		portfolioService.updatePortfolio(trade);

		verify(portfolioDao, times(1)).getPortfolio("client1");
		verify(portfolioDao, times(1)).updatePortfolio(any(), eq("client1"));
		assertEquals(BigDecimal.valueOf(0), existingPortfolio.getHoldings());
		assertEquals(
				BigDecimal.valueOf(0), // Replace with your expected price value
				existingPortfolio.getPrice()
		);

	}
	@Test
	public void testUpdatePortfolio_SellException() {
		Trade trade = new Trade();
		trade.setClientid("client1");
		trade.setInstrumentId("instrument1");
		trade.setDirection("S");
		trade.setQuantity(BigDecimal.valueOf(15)); // Attempt to sell more than what's available
		trade.setExecutionPrice(BigDecimal.valueOf(100));

		Portfolio existingPortfolio = new Portfolio("instrument1", "", "", BigDecimal.TEN, BigDecimal.valueOf(100));

		when(portfolioDao.getPortfolio("client1")).thenReturn(new ArrayList<>(List.of(existingPortfolio)));

		assertThrows(RuntimeException.class, () -> {
			portfolioService.updatePortfolio(trade);
		});

		verify(portfolioDao, times(1)).getPortfolio("client1");
		verify(portfolioDao, never()).updatePortfolio(any(), eq("client1"));

	}
	@Test
	public void testUpdatePortfolio_Buy_empty() {
		Trade trade = new Trade();
		trade.setClientid("client1");
		trade.setInstrumentId("instrument1");
		trade.setDirection("B");
		trade.setQuantity(BigDecimal.TEN);
		trade.setExecutionPrice(BigDecimal.valueOf(100));

		// Simulate that there are no existing portfolio rows for the client
		when(portfolioDao.getPortfolio("client1")).thenReturn(new ArrayList<>());

		// Perform the buy operation
		portfolioService.updatePortfolio(trade);

		// Verify that a new portfolio item is added
		verify(portfolioDao, times(1)).getPortfolio("client1");
		verify(portfolioDao, times(1)).addToPortfolio(any(), eq("client1"));
		ArgumentCaptor<Portfolio> portfolioCaptor = ArgumentCaptor.forClass(Portfolio.class);
		verify(portfolioDao).addToPortfolio(portfolioCaptor.capture(), eq("client1"));

		Portfolio addedPortfolio = portfolioCaptor.getValue();

		// Assert the holdings and price values
		assertEquals(BigDecimal.TEN, addedPortfolio.getHoldings());
		assertEquals(BigDecimal.valueOf(100), addedPortfolio.getPrice());
	}

	@Test
	void testGeTPortfolioEmpty() {
		String invalidClient = "john.doe@gmail.com";
		String clientId = "456";
		when(portfolioDao.getPortfolio(clientId)).thenThrow(new ClientNotFoundException(clientId));

		// Act and Assert
		assertThrows(ClientNotFoundException.class, () -> {
			portfolioService.getPortfolio(clientId);
		});
	}

}
