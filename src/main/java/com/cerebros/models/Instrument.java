package com.cerebros.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Instrument {

	private String instrumentId;
	private String externalIdType;
	private String externalId;
	private String categoryId;
	private String instrumentDescription;
	private BigDecimal minQuantity;
	private BigDecimal maxQuantity;
	private BigDecimal suitabilityScore;

	public BigDecimal getSuitabilityScore() {
		return suitabilityScore;
	}

	public void setSuitabilityScore(BigDecimal suitabilityScore) {
		this.suitabilityScore = suitabilityScore;
	}

	public Instrument(String instrumentId, String externalIdType, String externalId, String categoryId,
			String instrumentDescription, BigDecimal minQuantity, BigDecimal maxQuantity) {
		this.instrumentId = instrumentId;
		this.externalIdType = externalIdType;
		this.externalId = externalId;
		this.categoryId = categoryId;
		this.instrumentDescription = instrumentDescription;
		this.minQuantity = minQuantity;
		this.maxQuantity = maxQuantity;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + ", externalIdType=" + externalIdType + ", externalId="
				+ externalId + ", categoryId=" + categoryId + ", instrumentDescription=" + instrumentDescription
				+ ", minQuantity=" + minQuantity + ", maxQuantity=" + maxQuantity + ", suitabilityScore="
				+ suitabilityScore + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instrument other = (Instrument) obj;
		return Objects.equals(categoryId, other.categoryId) && Objects.equals(externalId, other.externalId)
				&& Objects.equals(externalIdType, other.externalIdType)
				&& Objects.equals(instrumentDescription, other.instrumentDescription)
				&& Objects.equals(instrumentId, other.instrumentId) && Objects.equals(maxQuantity, other.maxQuantity)
				&& Objects.equals(minQuantity, other.minQuantity)
				&& Objects.equals(suitabilityScore, other.suitabilityScore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId, externalId, externalIdType, instrumentDescription, instrumentId, maxQuantity,
				minQuantity, suitabilityScore);
	}

	public Instrument() {
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getExternalIdType() {
		return externalIdType;
	}

	public void setExternalIdType(String externalIdType) {
		this.externalIdType = externalIdType;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getInstrumentDescription() {
		return instrumentDescription;
	}

	public void setInstrumentDescription(String instrumentDescription) {
		this.instrumentDescription = instrumentDescription;
	}

	public BigDecimal getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(BigDecimal minQuantity) {
		this.minQuantity = minQuantity;
	}

	public BigDecimal getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(BigDecimal maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

}
