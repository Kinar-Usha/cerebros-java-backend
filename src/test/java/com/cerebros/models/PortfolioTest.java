package com.cerebros.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PortfolioTest {

	private Portfolio portfolio;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Portfolio portfolio= new  Portfolio( "instrumentId",  "description",  "categoryId", BigDecimal.TEN , BigDecimal.TEN);
		assertNotNull(portfolio);
//		fail("Not yet implemented");
	}

}
