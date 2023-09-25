package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
class ClientDaoQueryTest {

	@Autowired
	private ClientDaoImpl dao;

	private Client client1;

	@BeforeEach
	void setUp() throws Exception {

		Person person1 = new Person("john.doe@gmail.com", LocalDate.of(2001, 9, 11), Country.INDIA, "600097");
		ClientIdentification clientIdentification1 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification1);

		client1 = new Client("YOUR_CLIENTID", person1, clientIdentifications);
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	// Email Exists or Not

	@Test
	void testEmailExistsTrue() {
		boolean emailExists = dao.emailExists("john.doe@gmail.com");
		assertTrue(emailExists);
	}

	@Test
	void testEmailExistsFalse() {
		boolean emailExists = dao.emailExists("jane.doe@gmail.com");
		assertFalse(emailExists);
	}

	// Get Client by ID

	@Test
	void testGetClientWithNonExistentId() {
		assertThrows(ClientNotFoundException.class, () -> dao.getClient("NO_ID"));
	}

	@Test
	void testGetClientReturnsClient1() {
		Client actualClient = dao.getClient("YOUR_CLIENTID");
		client1.setClientIdentifications(null);
		assertEquals(client1, actualClient);
	}

	// Get Client by Email

	@Test
	void testGetClientByEmailWithNonExistentId() {
		assertNull(dao.getClientByEmail("jane.doe@gmail.com"));
	}

	@Test
	void testGetClientByEmailReturnsClient1() {
		Client actualClient = dao.getClientByEmail("john.doe@gmail.com");
		client1.setClientIdentifications(null);
		assertEquals(client1, actualClient);
	}

	// Login

	@Test
	void testLoginWithInvalidEmail() {
		assertFalse(() -> dao.login("john@gmail.com", "1234567890"));
	}

	@Test
	void testLoginWithInvalidPassword() {
		assertFalse(() -> dao.login("john.doe@gmail.com", "1267890"));
	}

	@Test
	void testLoginWithValidCreds() {
		assertTrue(() -> dao.login("john.doe@gmail.com", "1234567890"));
	}
	
	@Test
	void testInstantiateObjectNotNull() {
		assertNotNull(dao);
	}
	
	@Test
	void testGetAllPreferencesById() {
		assertNotNull(dao.getClientPreferences("YOUR_CLIENTID"));
	}

	
	@Test
	void testGetPreferencesByIdEqualsPurposes() {
		Preferences pref = new Preferences("Savings","High","Long-term","High");
		assertEquals(pref.getPurpose(),dao.getClientPreferences("YOUR_CLIENTID").getPurpose());
	}
	
	 @Test
	 @Rollback
	 void testAddPreference() {
		Preferences pref = new Preferences("Investment","High","Long-term","High");
	    assertNotNull(dao.addClientPreferences(pref, "YOUR_CLIENTID2"));
	      
	  }
	 
	 @Test
	 @Rollback
	 void testInsertExistingClientPreference(){
		Preferences pref = new Preferences("Investment","High","Long-term","High");
	    assertThrows(DuplicateKeyException.class,()->{
	      dao.addClientPreferences(pref, "YOUR_CLIENTID");
	    });
	  }
	 
	 @Test
	 @Rollback
	 void testInsertClientPreferenceWithNullObject(){
		assertThrows(NullPointerException.class,()->{
	      dao.addClientPreferences(null, "YOUR_CLIENTID");
	    });
	  }
	 
	 @Test
	 @Rollback
	 void testInsertClientPreferenceWithEmptyString(){
		Preferences pref = new Preferences("Investment","High","Long-term","High");
		assertThrows(IllegalArgumentException.class,()->{
	      dao.addClientPreferences(pref, "");
	    });
	  }
	 
	 @Test
	 @Rollback
	 public void testUpdateClientPreferences() {
	        // Create a Preferences object with updated values
	        Preferences preferences = new Preferences();
	        // Set the appropriate client ID
	        preferences.setPurpose("Savings");
	        preferences.setRisk("Moderate");
	        preferences.setTime("Long-term");
	        preferences.setIncome("High");

	        // Call the updateClientPreferences method to perform the update
	        dao.updateClientPreferences(preferences,"YOUR_CLIENTID");

	        // Retrieve the updated preferences from the database
	        Preferences updatedPreferences = dao.getClientPreferences("YOUR_CLIENTID"); // Implement getClientPreferences to retrieve preferences by client ID

	        // Assert that the update was successful by checking if the values match
	        assertEquals("Savings", updatedPreferences.getPurpose());
	        assertEquals("Moderate", updatedPreferences.getRisk());
	        assertEquals("Long-term", updatedPreferences.getTime());
	        assertEquals("High", updatedPreferences.getIncome());
	    }
	 
	 
	  @Test
	  @Rollback
	  void testUpdateClientPreferenceWithNullObject(){
		assertThrows(NullPointerException.class,()->{
	      dao.updateClientPreferences(null, "YOUR_CLIENTID");
	    });
	  }
	 
	 @Test
	 @Rollback
	 void testUpdateClientPreferenceWithEmptyString(){
		 Preferences pref = new Preferences("Investment","High","Long-term","High");
		assertThrows(IllegalArgumentException.class,()->{
	      dao.updateClientPreferences(pref, "");
	    });
	  }

}
