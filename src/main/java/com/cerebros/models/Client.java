package com.cerebros.models;

import java.util.Set;

public class Client {

	private String clientId;
	private Person person;
	private Preferences preferences;
	private Set<ClientIdentification> clientIdentifications;

	public Client(Person person, Set<ClientIdentification> clientIdentifications) {
		setPerson(person);
		setClientIdentifications(clientIdentifications);
		setClientId(clientId);
	}

	private void setClientId(String clientId2) {
		// TODO Auto-generated method stub

	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Set<ClientIdentification> getClientIdentifications() {
		return clientIdentifications;
	}

	public void setClientIdentifications(Set<ClientIdentification> clientIdentifications) {
		this.clientIdentifications = clientIdentifications;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}

}
