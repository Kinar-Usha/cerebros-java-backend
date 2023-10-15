package com.cerebros.models;

import java.time.LocalDate;
import java.util.Objects;

import com.cerebros.constants.Country;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

public class Person {

	private String name;
	private String email;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	private Country country;
	private String postalCode;

	public Person() {
	}

	public Person(String email, LocalDate dateOfBirth, Country country, String postalCode) {
		this.setEmail(email);
		this.setDateOfBirth(dateOfBirth);
		this.setCountry(country);
		this.setPostalCode(postalCode);
	}

	public Person(String name, String email, LocalDate dateOfBirth, Country country, String postalCode) {
		this.setName(name);
		this.setEmail(email);
		this.setDateOfBirth(dateOfBirth);
		this.setCountry(country);
		this.setPostalCode(postalCode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	@Override
	public int hashCode() {
		return Objects.hash(country, dateOfBirth, email, postalCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return country == other.country && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(email, other.email) && Objects.equals(postalCode, other.postalCode);
	}

}
