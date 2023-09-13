package com.cerebros.constants;

public enum Direction {
	BUY("B"), SELL("S");

	private String stringValue;

	Direction(String s) {
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
