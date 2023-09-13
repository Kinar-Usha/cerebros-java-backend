package com.cerebros.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {

	private BigDecimal askPrice;
	private BigDecimal bidPrice;
	private LocalDateTime priceTimestamp;
	private Instrument instrument;

	public Price(BigDecimal askPrice, BigDecimal bidPrice, LocalDateTime priceTimestamp, Instrument instrument) {
		this.askPrice = askPrice;
		this.bidPrice = bidPrice;
		this.priceTimestamp = priceTimestamp;
		this.instrument = instrument;
	}

	public BigDecimal getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public LocalDateTime getPriceTimestamp() {
		return priceTimestamp;
	}

	public void setPriceTimestamp(LocalDateTime priceTimestamp) {
		this.priceTimestamp = priceTimestamp;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

}
