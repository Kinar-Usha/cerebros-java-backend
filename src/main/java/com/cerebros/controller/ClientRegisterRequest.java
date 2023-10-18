package com.cerebros.controller;

import java.util.Set;

import com.cerebros.models.Person;
import com.cerebros.models.ClientIdentification;

public class ClientRegisterRequest {
    private Person person;
    private Set<ClientIdentification> clientIdentifications;
    private String password;

    public ClientRegisterRequest() {
    }

    public ClientRegisterRequest(Person person, Set<ClientIdentification> clientIdentifications, String password) {
        this.person = person;
        this.clientIdentifications = clientIdentifications;
        this.password = password;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<ClientIdentification> getClientIdentifications() {
        return this.clientIdentifications;
    }

    public void setClientIdentifications(Set<ClientIdentification> clientIdentifications) {
        this.clientIdentifications = clientIdentifications;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}