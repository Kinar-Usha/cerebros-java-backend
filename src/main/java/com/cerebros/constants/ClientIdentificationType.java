package com.cerebros.constants;

public enum ClientIdentificationType {

	SSN("SSN"), AADHAAR("ADH"), PASSPORT("PSP");

	private String type;

	ClientIdentificationType(String type) {
		this.type = type;
	}

	public static ClientIdentificationType of(String type) {
		for (ClientIdentificationType t : values()) {
			if (t.type.equals(type)) {
				return t;
			}
		}
		throw new IllegalArgumentException("Unknown identification type. Should be SSN, ADH, PSP");
	}

	public String getType() {
		return type;
	}

}
