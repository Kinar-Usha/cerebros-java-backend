package com.cerebros.mockdata;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cerebros.models.Instrument;
import com.cerebros.models.Price;

public class MockData {
	public List<Instrument> allInstruments;
	public List<Price> allPrices;

	public MockData() {
		createInstrumentsMock();
		createPricesMock();
	}

	private void createInstrumentsMock() {
		allInstruments.add(new Instrument("Q123", "CUSIP", "02079K107", "STOCK", "Alphabet Inc. Class C Capital Stock",
				new BigDecimal(1), new BigDecimal(1000)));
		allInstruments.add(new Instrument("Q456", "CUSIP", "88160R101", "STOCK", "Tesla, Inc. Common Stock",
				new BigDecimal(1), new BigDecimal(1000)));
		allInstruments.add(new Instrument("N123456", "CUSIP", "46625H100", "STOCK",
				"JPMorgan Chase & Co. Capital Stock", new BigDecimal(1), new BigDecimal(1000)));
		allInstruments.add(new Instrument("N123789", "ISIN", "US0846707026", "STOCK", "Berkshire Hathaway Inc. Class A",
				new BigDecimal(1), new BigDecimal(10)));
		allInstruments.add(new Instrument("C100", "CUSIP", "48123Y5A0", "CD",
				"JPMorgan Chase Bank, National Association 01/19", new BigDecimal(100), new BigDecimal(1000)));
		allInstruments.add(new Instrument("T67890", "CUSIP", "9128285M8", "GOVT", "USA, Note 3.125 15nov2028 10Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67894", "CUSIP", "9128285Z9", "GOVT", "USA, Note 2.5 31jan2024 5Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67895", "CUSIP", "9128286A3", "GOVT", "USA, Note 2.625 31jan2026 7Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67897", "CUSIP", "9128285X4", "GOVT", "USA, Note 2.5 31jan2021 2Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67899", "CUSIP", "9128285V8", "GOVT", "USA, Notes 2.5% 15jan2022 3Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67880", "CUSIP", "9128285U0", "GOVT", "USA, Note 1.5 31dec2023 5Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67883", "CUSIP", "912810SE9", "GOVT", "USA, Bond 3.375 15nov2048 30Y",
				new BigDecimal(100), new BigDecimal(10000)));
		allInstruments.add(new Instrument("T67878", "CUSIP", "912810SD1", "GOVT", "USA, Bond 3 15aug2048 30Y",
				new BigDecimal(100), new BigDecimal(10000)));
	}

	private void createPricesMock() {
		allPrices.add(new Price(new BigDecimal(104.75), new BigDecimal(104.25),
				LocalDateTime.parse("21-AUG-19 10.00.01.042000000 AM GMT"),
				new Instrument("N123456", "CUSIP", "46625H100", "STOCK", "JPMorgan Chase & Co. Capital Stock",
						new BigDecimal(1), new BigDecimal(1000))));
		allPrices.add(new Price(new BigDecimal(312500), new BigDecimal(312000),
				LocalDateTime.parse("21-AUG-19 05.00.00.040000000 AM -05:00"),
				new Instrument("N123789", "ISIN", "US0846707026", "STOCK", "Berkshire Hathaway Inc. Class A",
						new BigDecimal(1), new BigDecimal(10))));
		allPrices.add(new Price(new BigDecimal(95.92), new BigDecimal(95.42),
				LocalDateTime.parse("21-AUG-19 10.00.02.042000000 AM GMT"),
				new Instrument("C100", "CUSIP", "48123Y5A0", "CD", "JPMorgan Chase Bank, National Association 01/19",
						new BigDecimal(100), new BigDecimal(1000))));
		allPrices.add(new Price(new BigDecimal(1.03375), new BigDecimal(1.03390625),
				LocalDateTime.parse("21-AUG-19 10.00.02.000000000 AM GMT"),
				new Instrument("T67890", "CUSIP", "9128285M8", "GOVT", "USA, Note 3.125 15nov2028 10Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(0.998125), new BigDecimal(0.99828125),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67894", "CUSIP", "9128285Z9", "GOVT", "USA, Note 2.5 31jan2024 5Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(1), new BigDecimal(1.00015625),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67895", "CUSIP", "9128286A3", "GOVT", "USA, Note 2.625 31jan2026 7Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(0.999375), new BigDecimal(0.999375),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67897", "CUSIP", "9128285X4", "GOVT", "USA, Note 2.5 31jan2021 2Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(0.999375), new BigDecimal(0.999375),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67899", "CUSIP", "9128285V8", "GOVT", "USA, Notes 2.5% 15jan2022 3Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(1.00375), new BigDecimal(1.00375),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67880", "CUSIP", "9128285U0", "GOVT", "USA, Note 1.5 31dec2023 5Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(1.0596875), new BigDecimal(1.0596875),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"),
				new Instrument("T67883", "CUSIP", "912810SE9", "GOVT", "USA, Bond 3.375 15nov2048 30Y",
						new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(0.9853125), new BigDecimal(0.98546875),
				LocalDateTime.parse("21-AUG-19 10.00.02.002000000 AM GMT"), new Instrument("T67878", "CUSIP",
						"912810SD1", "GOVT", "USA, Bond 3 15aug2048 30Y", new BigDecimal(100), new BigDecimal(10000))));
		allPrices.add(new Price(new BigDecimal(1162.42), new BigDecimal(1161.42),
				LocalDateTime.parse("21-AUG-19 06.52.20.350000000 PM AMERICA/NEW_YORK"),
				new Instrument("Q123", "CUSIP", "02079K107", "STOCK", "Alphabet Inc. Class C Capital Stock",
						new BigDecimal(1), new BigDecimal(1000))));
		allPrices.add(new Price(new BigDecimal(323.39), new BigDecimal(322.89),
				LocalDateTime.parse("21-AUG-19 06.52.20.356000000 PM AMERICA/NEW_YORK"), new Instrument("Q456", "CUSIP",
						"88160R101", "STOCK", "Tesla, Inc. Common Stock", new BigDecimal(1), new BigDecimal(1000))));
	}
}
