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
		return "Instrument{" +
				"instrumentId='" + instrumentId + '\'' +
				", externalIdType='" + externalIdType + '\'' +
				", externalId='" + externalId + '\'' +
				", categoryId='" + categoryId + '\'' +
				", instrumentDescription='" + instrumentDescription + '\'' +
				", minQuantity=" + minQuantity +
				", maxQuantity=" + maxQuantity +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Instrument that = (Instrument) o;
		return Objects.equals(instrumentId, that.instrumentId) && Objects.equals(externalIdType, that.externalIdType) && Objects.equals(externalId, that.externalId) && Objects.equals(categoryId, that.categoryId) && Objects.equals(instrumentDescription, that.instrumentDescription) && Objects.equals(minQuantity, that.minQuantity) && Objects.equals(maxQuantity, that.maxQuantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(instrumentId, externalIdType, externalId, categoryId, instrumentDescription, minQuantity, maxQuantity);
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
