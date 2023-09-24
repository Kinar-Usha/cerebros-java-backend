package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

class ClientDaoDMLTest {

	private JdbcTemplate jdbcTemplate;
	private DbTestUtils dbTestUtils;
	private SimpleDataSource dataSource;
	private Connection connection;
	private ClientDao dao;
	private TransactionManager txManager;

	private Person person2;
	private Client client2;
	private Preferences preference2;
	ClientIdentification clientIdentification2;

	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleDataSource();
		connection = dataSource.getConnection();
		txManager = new TransactionManager(dataSource);

		dao = new ClientDaoImpl(dataSource);

		// Start the TX
		txManager.startTransaction();

		dbTestUtils = new DbTestUtils(connection);
		jdbcTemplate = dbTestUtils.initJdbcTemplate();

		// sample data
		person2 = new Person("jane.doe@gmail.com", LocalDate.of(1998, 2, 1), Country.USA, "600097");

		ClientIdentification clientIdentification2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"A9624421");
		Set<ClientIdentification> clientIdentifications2 = new HashSet<ClientIdentification>();
		clientIdentifications2.add(clientIdentification2);

		client2 = new Client("JANE_ID", person2, clientIdentifications2);

		preference2 = new Preferences("Investment", "High", "Short-term", "High");
	}

	@AfterEach
	void tearDown() throws Exception {
		// Rollback the transaction
		txManager.rollbackTransaction();

		connection.close();
		dataSource.shutdown();
	}

	// Client Registration Tests

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

//		assertThrows(ClientAlreadyExistsException.class, () -> dao.register(client2, "123456"));
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

}
