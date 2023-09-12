package com.cerebros.models;

import java.time.LocalDate;

public class Person {

	private String email;
	private LocalDate dateofBirth;
	private String country;
	private String postalCode;

	public Person(String email, LocalDate dateofBirth, String country, String postalCode) {
		this.setEmail(email);
		this.setDateofBirth(dateofBirth);
		this.setCountry(country);
		this.setPostalCode(postalCode);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(LocalDate dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
