package com.cerebros.services;

import com.cerebros.exceptions.PortfolioNotFoundException;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioServiceTest {

	private PortfolioService portfolioService;
	private List<Portfolio> portfolioList;
	private Portfolio portfolio1= new Portfolio("12345","testDesc", "GOVT", BigDecimal.ONE,BigDecimal.TEN);
	private Portfolio portfolio2= new Portfolio("2345","testDesc", "CORP", new BigDecimal("300"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
	private Portfolio portfolio3= new Portfolio("345","testDesc", "CD", new BigDecimal("200"), new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
	private ClientService clientService;

	@BeforeEach
	void setUp() throws Exception {
		portfolioList= new ArrayList<>();
		portfolioList.add(portfolio1);
		portfolioList.add(portfolio2);
		portfolioList.add(portfolio3);

		portfolioService = new PortfolioService();
		portfolioService.setupDummyPortfolio();
		clientService=new ClientService();
		clientService.setupMockClients();


	}

	@AfterEach
	void tearDown() throws Exception {
		portfolioService=null;
	}

	@Test
	void test() {

		portfolioService= new PortfolioService();
		assertNotNull(portfolioService);
	}
	@Test
	void testConstructor(){


		assertNotNull(portfolioService);
	}

	@Test
	void testGetPortfolioSuccess(){
		assertEquals(portfolioList,portfolioService.getPortfolio("jane.doe@gmail.com")); // decide elient id or client email
	}
	@Test
	public void testGetPortfolioInvalidEmail() {
		// Arrange
		String clientEmail = "noat@example.com";


		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> portfolioService.getPortfolio(clientEmail));
	}
	@Test
	public void testUpdatePortfolio_Buy() {
		// Arrange
		Trade trade = new Trade("trade123", BigDecimal.ONE, BigDecimal.TEN, "B", BigDecimal.ZERO, "789", "12345", null);

		// Act
		portfolioService.updatePortfolio(trade);

		// Assert
		List<Portfolio> updatedPortfolio = portfolioService.getPortfolio("jane.doe@gmail.com");
		assertEquals(3, updatedPortfolio.size());

		Portfolio updatedItem = updatedPortfolio.get(0);
		assertEquals("12345", updatedItem.getInstrumentId());
		assertEquals(new BigDecimal("2"), updatedItem.getHoldings());
		assertEquals(BigDecimal.valueOf(6.7), updatedItem.getPrice());
	}
	@Test
	public void testUpdatePortfolio_Sell() {
		// Arrange
		Trade trade = new Trade("trade123", BigDecimal.ONE, BigDecimal.TEN, "S", BigDecimal.ZERO, "789", "12345", null);

		// Act
		portfolioService.updatePortfolio(trade);

		// Assert
		List<Portfolio> updatedPortfolio = portfolioService.getPortfolio("jane.doe@gmail.com");
		assertEquals(2, updatedPortfolio.size());


	}
	@Test
	public void testUpdatePortfolio_SellException() {
		// Arrange
		Trade trade = new Trade("trade123", BigDecimal.ONE, BigDecimal.TEN, "S", BigDecimal.ZERO, "789", "123456", null);

		// Act

		// Assert
		assertThrows(RuntimeException.class, ()->{
			portfolioService.updatePortfolio(trade);
		});


	}
	@Test
	public void testUpdatePortfolio_Buy_empty() {
		// Arrange
		Trade trade = new Trade("trade123", BigDecimal.ONE, BigDecimal.TEN, "B", BigDecimal.ZERO, "456", "12345", null);

		// Act
		portfolioService.updatePortfolio(trade);

		// Assert
		List<Portfolio> updatedPortfolio = portfolioService.getPortfolio("john.doe@gmail.com");
		assertEquals(1, updatedPortfolio.size());

		Portfolio updatedItem = updatedPortfolio.get(0);
		assertEquals("12345", updatedItem.getInstrumentId());
		assertEquals(new BigDecimal("1"), updatedItem.getHoldings());
		assertEquals(BigDecimal.TEN, updatedItem.getPrice());
	}

	@Test
	void testGeTPortfolioEmpty(){
	String invalidClient= "john.doe@gmail.com";
		Exception e= assertThrows(
				PortfolioNotFoundException.class,()->{
					portfolioService.getPortfolio(invalidClient);
				}
		);
		assertEquals(String.format( "Portfolio not found for client : %s", invalidClient), e.getMessage());
	}

}
