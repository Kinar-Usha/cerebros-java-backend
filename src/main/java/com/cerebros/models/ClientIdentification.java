package com.cerebros.models;

import com.cerebros.constants.ClientIdentificationType;

public class ClientIdentification {

	private ClientIdentificationType type;
	private String value;

	public ClientIdentification(ClientIdentificationType type, String value) {
		setType(type);
		setValue(value);
	}

	public ClientIdentificationType getType() {
		return type;
	}

	public void setType(ClientIdentificationType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
