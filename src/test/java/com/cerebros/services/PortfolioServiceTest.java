package com.cerebros.services;

import com.cerebros.exceptions.PortfolioNotFoundException;
import com.cerebros.models.Portfolio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioServiceTest {

	private PortfolioService portfolioService;
	private List<Portfolio> portfolioList;
	private Portfolio portfolio1= new Portfolio("12345","testDesc", "GOVT", new BigDecimal("100"), new BigDecimal("20.00"));
	private Portfolio portfolio2= new Portfolio("2345","testDesc", "CORP", new BigDecimal("300"), new BigDecimal("20.00"));
	private Portfolio portfolio3= new Portfolio("345","testDesc", "CD", new BigDecimal("200"), new BigDecimal("20.00"));
	private ClientService clientService;

	@BeforeEach
	void setUp() throws Exception {
		portfolioList= new ArrayList<>();
		portfolioList.add(portfolio1);
		portfolioList.add(portfolio2);
		portfolioList.add(portfolio3);
		portfolioService = new PortfolioService();
		portfolioService.setupdDummyPortfolio();


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
		assertEquals(portfolioList,portfolioService.getPortfolio("a@bc.com"));
	}

	@Test
	void testGeTPortfolioEmpty(){
	String invalidClient= "d@bc.com";
		Exception e= assertThrows(
				PortfolioNotFoundException.class,()->{
					portfolioService.getPortfolio(invalidClient);
				}
		);
		assertEquals(String.format( "Portfolio not found for client : %s", invalidClient), e.getMessage());
	}

}
