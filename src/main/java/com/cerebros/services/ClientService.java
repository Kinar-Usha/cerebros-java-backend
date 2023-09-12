package com.cerebros.services;

import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientService {
	private static Set<ClientIdentification> id1;

	static {
		Set<ClientIdentification> clientIdentifications = new HashSet<>();
		clientIdentifications.add(new ClientIdentification("AADHAR", "123456789102"));
		id1 = new HashSet<>(clientIdentifications);
	}

	private static Set<ClientIdentification> id2;

	static {
		Set<ClientIdentification> clientIdentifications = new HashSet<>();
		clientIdentifications.add(new ClientIdentification("SSN", "123456782932"));
		id2 = new HashSet<ClientIdentification>(clientIdentifications);
	}

	private static Map<String, Client> mockClient= Map.ofEntries(
			Map.entry("a@bc.com", new Client("1", "a@bc.com", LocalDate.now(),"India","570023",id1)),
			Map.entry("b@bc.com", new Client("2", "b@bc.com", LocalDate.now(),"India","570023",id2))


	);
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
	public boolean verifyClientId(String clientId){


        return true;
    }

}
