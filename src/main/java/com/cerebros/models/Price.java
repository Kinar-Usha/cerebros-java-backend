package com.cerebros.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {
    private String InstrumentId;
    private LocalDateTime timeStamp;

    private BigDecimal askPrice;
    private BigDecimal bidPrice;
    private Instrument instrument;

    public Price(String instrumentId, LocalDateTime timeStamp, BigDecimal askPrice, BigDecimal bidPrice, Instrument instrument) {
        InstrumentId = instrumentId;
        this.timeStamp = timeStamp;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
        this.instrument = instrument;
    }
}
