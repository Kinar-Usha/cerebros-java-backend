package com.cerebros.models;



import java.math.BigDecimal;
import java.util.Objects;

public class Price {

	private BigDecimal askPrice;
	private BigDecimal bidPrice;
	private String priceTimestamp;
	private Instrument instrument;

	@Override
	public String toString() {
		return "Price{" +
				"askPrice=" + askPrice +
				", bidPrice=" + bidPrice +
				", priceTimestamp='" + priceTimestamp + '\'' +
				", instrument=" + instrument +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Price price = (Price) o;
		return Objects.equals(askPrice, price.askPrice) && Objects.equals(bidPrice, price.bidPrice) && Objects.equals(priceTimestamp, price.priceTimestamp) && Objects.equals(instrument, price.instrument);
	}

	@Override
	public int hashCode() {
		return Objects.hash(askPrice, bidPrice, priceTimestamp, instrument);
	}

	public void setPriceTimestamp(String priceTimestamp) {
		this.priceTimestamp = priceTimestamp;
	}

	public Price(BigDecimal askPrice, BigDecimal bidPrice, String priceTimestamp, Instrument instrument) {
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


	public String getPriceTimestamp() {
		return priceTimestamp;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public Price() {
	}

}
