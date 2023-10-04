package com.cerebros.models;

import java.util.Objects;

public class ClientRequest {
    private String email;
    private String clientId;
    private String token;

    public ClientRequest(String email) {
        this.email = email;
    }

    public ClientRequest(String email, String clientId) {
        this.email = email;
        this.clientId = clientId;
    }

    public ClientRequest(String email, String clientId, String token) {
        this.email = email;
        this.clientId = clientId;
        this.token = token;
    }

    public ClientRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRequest that = (ClientRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(clientId, that.clientId) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, clientId, token);
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "email='" + email + '\'' +
                ", clientId='" + clientId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
    // Constructors, getters, and setters
    // ...
}
