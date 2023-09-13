package com.cerebros.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InstrumentTest {

	private Instrument instrument;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
//		fail("Not yet implemented");
		Instrument instrument = new Instrument("instrumentId", "description", "externalIdType",  "category","maxQuantity", BigDecimal.ZERO,BigDecimal.ZERO);
		assertNotNull(instrument);

	}

}
