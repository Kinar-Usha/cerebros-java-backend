package com.cerebros.models;



import java.math.BigDecimal;
import java.util.Objects;

public class Price {

	private BigDecimal askPrice;
	private BigDecimal bidPrice;
	private BigDecimal suitabilityScore;
	public BigDecimal getSuitabilityScore() {
		return suitabilityScore;
	}

	public void setSuitabilityScore(BigDecimal suitabilityScore) {
		this.suitabilityScore = suitabilityScore;
	}

	private String priceTimestamp;
	private Instrument instrument;

	@Override
	public String toString() {
		return "Price [askPrice=" + askPrice + ", bidPrice=" + bidPrice + ", suitabilityScore=" + suitabilityScore
				+ ", priceTimestamp=" + priceTimestamp + ", instrument=" + instrument + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		return Objects.equals(askPrice, other.askPrice) && Objects.equals(bidPrice, other.bidPrice)
				&& Objects.equals(instrument, other.instrument) && Objects.equals(priceTimestamp, other.priceTimestamp)
				&& Objects.equals(suitabilityScore, other.suitabilityScore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(askPrice, bidPrice, instrument, priceTimestamp, suitabilityScore);
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
