package com.cerebros.models;

public class ClientIdentification {

	private String type;
	private String value;

	public ClientIdentification(String type, String value) {
		setType(type);
		setValue(value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if(type == null || type.isEmpty()){
			throw new IllegalArgumentException("Invalid type");
		}
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if(type == null || type.isEmpty()){
			throw new IllegalArgumentException("Invalid value");
		}
		this.value = value;
	}

}
