package com.cerebros.exceptions;

public class PortfolioNotFoundException extends RuntimeException {

	public PortfolioNotFoundException(String clientId) {
		super("Portfolio not found for client : " + clientId);
	}

}
