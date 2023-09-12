package com.cerebros.models;

import java.time.LocalDate;
import java.util.Set;

import com.cerebros.contants.Country;

public class Client extends Person {

	private String clientId;
	private Set<ClientIdentification> clientIdentifications;

	public Client(String email, LocalDate dateofBirth, Country country, String postalCode,
			Set<ClientIdentification> clientIdentifications) {
		super(email, dateofBirth, country, postalCode);
		setClientIdentifications(clientIdentifications);
		setClientId(clientId);
	}

	private void setClientId(String clientId2) {
		// TODO Auto-generated method stub

	}

	public Set<ClientIdentification> getClientIdentifications() {
		return clientIdentifications;
	}

	public void setClientIdentifications(Set<ClientIdentification> clientIdentifications) {
		this.clientIdentifications = clientIdentifications;
	}

}
