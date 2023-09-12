package com.cerebros.models;

import java.time.LocalDate;

import com.cerebros.contants.Country;

public class Person {

	private String email;
	private LocalDate dateofBirth;
	private Country country;
	private String postalCode;

	public Person(String email, LocalDate dateofBirth, Country country, String postalCode) {
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
