package com.cerebros.models;

public class Instrument {
    private String instrumentId;
    private String description;
    private String externalIdType;
    private String minQuantity;
    private String maxQuantity;
    private String category;

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getDescription() {
        return description;
    }

    public String getExternalIdType() {
        return externalIdType;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public String getCategory() {
        return category;
    }

    public Instrument(String instrumentId, String description, String externalIdType, String minQuantity, String maxQuantity, String category) {
        this.instrumentId = instrumentId;
        this.description = description;
        this.externalIdType = externalIdType;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.category = category;
    }

}
