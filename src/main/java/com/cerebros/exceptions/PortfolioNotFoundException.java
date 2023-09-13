package com.cerebros.exceptions;

public class PortfolioNotFoundException extends RuntimeException {
    public PortfolioNotFoundException(String client) {

        super("Portfolio not found for client : " + client);
    }
}
