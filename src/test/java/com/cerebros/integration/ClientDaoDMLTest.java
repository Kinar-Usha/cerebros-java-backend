package com.cerebros.integration;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import com.cerebros.integration.doa.impl.ClientDaoImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientDaoDMLTest {

	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ClientDaoImpl dao;

	private Person person2;
	private Client client2;
	private Preferences preference2;
	ClientIdentification clientIdentification2;

	@BeforeEach
	void setUp() throws Exception {

		jdbcTemplate = new JdbcTemplate(dataSource);

		// sample data
		person2 = new Person("Jane Doe", "jane.doe@gmail.com", LocalDate.of(1998, 2, 1), Country.USA, "600097");

		ClientIdentification clientIdentification2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"A9624421");
		Set<ClientIdentification> clientIdentifications2 = new HashSet<ClientIdentification>();
		clientIdentifications2.add(clientIdentification2);

		client2 = new Client("JANE_ID", person2, clientIdentifications2);

		preference2 = new Preferences("Investment", "High", "Short-term", "High");
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	// Client Registration Tests

	@Test
	void testInsertCash(){
		assertEquals(1,dao.insertCash("YOUR_CLIENTID1", BigDecimal.TEN));
	}
	@Test
	void registerClientWithExistingEmail() {
		client2.getPerson().setEmail("john.doe@gmail.com");
		assertThrows(ClientAlreadyExistsException.class, () -> dao.register(client2, "123456"));
	}

	@Test
	void registerClientWithExistingPassport() {
		ClientIdentification clientIdentification2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentifications2 = new HashSet<ClientIdentification>();
		clientIdentifications2.add(clientIdentification2);

		client2.setClientIdentifications(clientIdentifications2);

		assertThrows(ClientAlreadyExistsException.class, () -> dao.register(client2, "123456"));
	}

	@Test
	void registerClientWithExistingPassport_DoesNotInsertAnyRows() {
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cerebros_client");

		ClientIdentification clientIdentification2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentifications2 = new HashSet<ClientIdentification>();
		clientIdentifications2.add(clientIdentification2);

		client2.setClientIdentifications(clientIdentifications2);

		assertThrows(ClientAlreadyExistsException.class, () -> dao.register(client2, "123456"));
		assertEquals(oldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "cerebros_client"));
	}

	@Test
	void registerClientWithNullClientID() {
		client2.setClientIdentifications(null);
		assertThrows(NullPointerException.class, () -> dao.register(client2, "123456"));
	}

	@Test
	void testRegisterClientIncreasedRowCount() throws SQLException {
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cerebros_client");
		dao.register(client2, "123456");
		assertEquals(oldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "cerebros_client"));
	}

	@Test
	void registerClientInserted_Client() throws SQLException {
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_client", "clientId = 'JANE_ID'"));
		dao.register(client2, "123456");
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_client", "clientId = 'JANE_ID'"));
	}

	@Test
	void registerClientInserted_ClientIdentifications() throws SQLException {
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_clientIdentifications",
				"clientId = 'JANE_ID' AND idType = 'PSP'"));
		dao.register(client2, "123456");
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_clientIdentifications",
				"clientId = 'JANE_ID' AND idType = 'PSP'"));
	}

	@Test
	void registerClientInserted_ClientPasswords() throws SQLException {
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_clientPasswords",
				"clientId = 'JANE_ID' AND passwordHash = '123456'"));
		dao.register(client2, "123456");
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cerebros_clientPasswords",
				"clientId = 'JANE_ID' AND passwordHash = '123456'"));
	}

	// TODO Delete Client Test

	@Test
	void testAddPreference() throws SQLException {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		dao.register(client2, "123");
		assertNotNull(dao.addClientPreferences(pref, client2.getClientId()));

	}
	
	@Test
	void testAddPreferenceWithInvalidId() throws SQLException {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		assertThrows(DataIntegrityViolationException.class, () -> {
			dao.addClientPreferences(pref, "YOUR_CLIENTID1234444");
		});

	}

	@Test
	void testInsertExistingClientPreference() {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		assertThrows(DuplicateKeyException.class, () -> {
			dao.addClientPreferences(pref, "YOUR_CLIENTID");
		});
	}

	@Test
	void testInsertClientPreferenceWithNullObject() {
		assertThrows(NullPointerException.class, () -> {
			dao.addClientPreferences(null, "YOUR_CLIENTID");
		});
	}

	@Test
	void testInsertClientPreferenceWithEmptyString() {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		assertThrows(IllegalArgumentException.class, () -> {
			dao.addClientPreferences(pref, "");
		});
	}

	@Test
	public void testUpdateClientPreferences() {
		// Create a Preferences object with updated values
		Preferences preferences = new Preferences();
		// Set the appropriate client ID
		preferences.setPurpose("Savings");
		preferences.setRisk("Moderate");
		preferences.setTime("Long-term");
		preferences.setIncome("High");

		// Call the updateClientPreferences method to perform the update
		assertEquals(1,dao.updateClientPreferences(preferences, "YOUR_CLIENTID"));

		// Retrieve the updated preferences from the database
		Preferences updatedPreferences = dao.getClientPreferences("YOUR_CLIENTID"); // Implement getClientPreferences to
																					// retrieve preferences by client ID

		// Assert that the update was successful by checking if the values match
		assertEquals("Savings", updatedPreferences.getPurpose());
		assertEquals("Moderate", updatedPreferences.getRisk());
		assertEquals("Long-term", updatedPreferences.getTime());
		assertEquals("High", updatedPreferences.getIncome());
	}

	@Test
	void testUpdateClientPreferenceWithNullObject() {
		assertThrows(NullPointerException.class, () -> {
			dao.updateClientPreferences(null, "YOUR_CLIENTID");
		});
	}

	@Test
	void testUpdateClientPreferenceWithEmptyString() {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		assertThrows(IllegalArgumentException.class, () -> {
			dao.updateClientPreferences(pref, "");
		});
	}

	@Test
	public void testUpdateClientPreferencesInvalidClientId() {
		// Create a Preferences object with updated values
		Preferences preferences = new Preferences();
		// Set the appropriate client ID
		preferences.setPurpose("Savings");
		preferences.setRisk("Moderate");
		preferences.setTime("Long-term");
		preferences.setIncome("High");

		assertThrows(DatabaseException.class, () -> {
			dao.updateClientPreferences(preferences, "YOUR_CLIENTID12333");
		});

	}

}
