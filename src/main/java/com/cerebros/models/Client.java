package com.cerebros.models;

import java.util.Objects;
import java.util.Set;

public class Client {

	private String clientId;
	private Person person;
	private Preferences preferences;
	private Set<ClientIdentification> clientIdentifications;

	public Client() {
	}

	public Client(String clientId, Person person, Set<ClientIdentification> clientIdentifications) {
		setPerson(person);
		setClientIdentifications(clientIdentifications);
		setClientId(clientId);
		this.preferences = null;
	}

	public Client(String clientId, Person person) {
		setPerson(person);
		setClientId(clientId);
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public Person getPerson() {
		return this.person;
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

	@Override
	public int hashCode() {
		return Objects.hash(clientId, clientIdentifications, person, preferences);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(clientId, other.clientId)
				&& Objects.equals(clientIdentifications, other.clientIdentifications)
				&& Objects.equals(person, other.person) && Objects.equals(preferences, other.preferences);
	}

}
