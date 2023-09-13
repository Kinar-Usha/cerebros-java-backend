package com.cerebros.services;

import java.util.List;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Instrument;

public class MockFMTS {

	private List<Instrument> instruments;

	public boolean verifyClientIdentification(ClientIdentification clientIdentification) {
		// verify the syntax of each type of indentification
		// SSN = 111-22-3333
		// Aadhaar = 1234-4567-7890
		// Passport = A1234567

		String identificationNumber = clientIdentification.getValue();
		ClientIdentificationType identificationType = clientIdentification.getType();

		switch (identificationType) {
		case SSN:
			// Verify SSN syntax
			if (identificationNumber.matches("\\d{3}-\\d{2}-\\d{4}")) {
				return true;
			}
			break;
		case AADHAAR:
			// Verify Aadhaar syntax
			if (identificationNumber.matches("\\d{4}-\\d{4}-\\d{4}")) {
				return true;
			}
			break;
		case PASSPORT:
			// Verify Passport syntax
			if (identificationNumber.matches("[A-Z]\\d{7}")) {
				return true;
			}
			break;

		default:
			break;
		}

		return false;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public void setMockInstruments(List<Instrument> instruments) {
		setInstruments(instruments);
	}

}
