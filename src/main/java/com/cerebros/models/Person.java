package com.cerebros.models;

import java.time.LocalDate;
import java.util.Objects;

import com.cerebros.constants.Country;

public class Person {

	private String name;
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

	public Person(String name, String email, LocalDate dateofBirth, Country country, String postalCode) {
		this.setName(name);
		this.setEmail(email);
		this.setDateofBirth(dateofBirth);
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

	@Override
	public int hashCode() {
		return Objects.hash(country, dateofBirth, email, postalCode);
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
		return country == other.country && Objects.equals(dateofBirth, other.dateofBirth)
				&& Objects.equals(email, other.email) && Objects.equals(postalCode, other.postalCode);
	}

}
