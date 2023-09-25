package com.cerebros.constants;

public enum Country {
	INDIA("IN"), USA("US"), IRELAND("IE");

	private String code;

	Country(String code) {
		this.code = code;
	}

	public static Country of(String code) {
		for (Country c : values()) {
			if (c.code.equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Unknown country ISO code. Should be US, IN, IE");
	}

	public String getCode() {
		return code;
	}

}
