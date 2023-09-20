package com.cerebros.models;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientIdentification other = (ClientIdentification) obj;
		return type == other.type && Objects.equals(value, other.value);
	}
}
