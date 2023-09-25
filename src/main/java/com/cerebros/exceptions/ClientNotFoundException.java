package com.cerebros.exceptions;

public class ClientNotFoundException extends RuntimeException {
	public ClientNotFoundException() {
	}

	public ClientNotFoundException(String s) {

	}

	public ClientNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientNotFoundException(Throwable cause) {
		super(cause);
	}

	public ClientNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
