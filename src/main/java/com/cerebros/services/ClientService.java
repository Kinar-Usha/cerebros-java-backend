package com.cerebros.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientService {

	private static final String EMAIL_PATTERN="^[A-Za-z0-9+_.-]+@(.+)$";

	public boolean verifyEmailAddress(String email) {
		Pattern pattern= Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()){
			throw new IllegalArgumentException("Invalid Email Format");
		}

		// DONE check the email address by regex
		// TODO If regex of email is fine, then check if it exists in the mock data
		// using MockFMTS
		// TODO return true or false here. The appropriate use of true or false here
		// depends on login or signup use case
		// TODO if the regex itself is invalid, return false
		return false;
	}

}
