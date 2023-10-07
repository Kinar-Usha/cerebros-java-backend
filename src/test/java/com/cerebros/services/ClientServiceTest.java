package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.integration.doa.ClientDao;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

class ClientServiceTest {

	@Mock
	ClientDao clientDao;

	@Autowired
	@InjectMocks
	private ClientService clientService;

	private Person person;
	private ClientIdentification clientIdentification;
	private Set<ClientIdentification> clientIdentifications;
	private Client client;
	private Preferences preferences;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		// Sample client data
		person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		preferences = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");

		client = new Client("123", person, clientIdentifications);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest
	@ValueSource(strings = { "bhavesh@gmail.com", "john.doe@gmail.com", "jane.doe@gmail.com" })
	void verifyExistingEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		// Should return false for emails that already exist in the clients
		assertFalse(clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "Kin.s@gmail.com", "k1@gmail.com", "A_kinar@yahoo.com" })
	void TestVerifyEmailAdressFormat_success(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		assertDoesNotThrow(() -> clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		assertThrows(IllegalArgumentException.class, () -> clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
	void verifyNonexistentEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(false);
		// Should return true for emails that are not registered yet
		assertTrue(clientService.verifyEmailAddress(email));
	}

	@Test
	void registerInvalidClientIdentification() {
		clientIdentification.setValue("333-22-44544");
		clientIdentifications.add(clientIdentification);

		assertThrows(IllegalArgumentException.class,
				() -> clientService.registerClient(person, clientIdentifications, "1234"));
	}

	@Test
	void registerValidClient() throws SQLException {

		Mockito.doNothing().when(clientDao).register(Mockito.any(Client.class), Mockito.anyString());

		assertDoesNotThrow(() -> clientService.registerClient(person, clientIdentifications, "123456"));
	}

	@Test
	void registrationFailsOnExistingClientEmail() {

		Mockito.when(clientDao.emailExists(person.getEmail())).thenReturn(true);

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications, "123456"));
	}

	@Test
	void registrationAddsExistingClientWithNewEmail() {

		Mockito.doThrow(ClientAlreadyExistsException.class).when(clientDao).register(Mockito.any(Client.class),
				Mockito.anyString());

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications, "123456"));
	}
	
	 @Test
	 public void testGetPreferencesWithValidClientId() {
	        String clientId = "YOUR_CLIENTID";
	        Preferences preference = new Preferences("Investment","High","Long-term","High");
	        when(clientDao.getClientPreferences(clientId)).thenReturn(preference);

	        Preferences actualPreferences = clientService.getPreferences(clientId);

	        assertEquals(preference, actualPreferences);
	        verify(clientDao, times(1)).getClientPreferences(clientId);
	    }
	 
	 @Test
	    public void testGetPreferencesWithEmptyClientId() {
	        String emptyClientId = "";


	        assertThrows(IllegalArgumentException.class, () -> {
	            clientService.getPreferences(emptyClientId);
	        });

	        verify(clientDao, never()).getClientPreferences(emptyClientId);
	    }
	 
	 
	 @Test
	    public void testAddPreferencesWithSuccessfulAddition() {
	        String clientId = "YOUR_CLIENTID1";
	        Preferences preference = new Preferences("Investment","High","Long-term","High");
		       
	        when(clientDao.addClientPreferences(preferences, clientId)).thenReturn(1);

	        int result = clientService.addPreferences(clientId, preferences);

	        assertEquals(1, result);
	        verify(clientDao, times(1)).addClientPreferences(preferences, clientId);
	    }
	 
	  @Test
	    public void testAddPreferencesWithFailedAddition() {
	        String clientId = "YOUR_CLIENTID";
	        Preferences preference = new Preferences("Investment","High","Long-term","High");
		       
	        when(clientDao.addClientPreferences(preferences, clientId)).thenReturn(0);

	        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	        	clientService.addPreferences(clientId, preferences);
	        });

	        assertEquals("Failed to add preferences", exception.getMessage());

	        verify(clientDao, times(1)).addClientPreferences(preferences, clientId);
	    }


	  @Test
	    public void testUpdatePreferencesWithValidInput() {
		   String clientId = "YOUR_CLIENTID";
	       Preferences preference = new Preferences("Investment","High","Long-term","High");
		       
	       when(clientDao.updateClientPreferences(preferences, clientId)).thenReturn(1);

	        int updatedRows = clientDao.updateClientPreferences(preferences, clientId);

	        assertEquals(1, updatedRows);
	        verify(clientDao, times(1)).updateClientPreferences(preferences, clientId);
	    }
	  
	  @Test
	    public void testUpdatePreferencesWithNullPreference() {
	        String clientId = "YOUR_CLIENTID";
	        Preferences nullPreference = null;

	        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	            clientService.updatePreferences(clientId, nullPreference);
	        });

	        assertEquals("Preference cannot be null", exception.getMessage());

	        // Verify that dao.updateClientPreferences is never called in this case
	        verify(clientDao, never()).updateClientPreferences(nullPreference, clientId);
	    }
	  
	  @Test
	    public void testUpdatePreferencesWithFailedUpdate() {
	        String clientId = "YOUR_CLIENTID1";
	        Preferences preference = new Preferences("Investment","High","Long-term","High");
			  
	        when(clientDao.updateClientPreferences(preferences, clientId)).thenReturn(0);

	        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	            clientService.updatePreferences(clientId, preferences);
	        });

	        assertEquals("Failed to update preferences", exception.getMessage());

	        verify(clientDao, times(1)).updateClientPreferences(preferences, clientId);
	    }

	@Test
	public void invalidEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-44445";

		Mockito.when(clientDao.login(email, password)).thenReturn(false);

		assertThrows(InvalidCredentialsException.class, () -> clientService.login(email, password));
	}

	@Test
	public void validEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-4444";

		Mockito.when(clientDao.login(email, password)).thenReturn(true);

		assertTrue(clientService.login(email, password));
	}

}
