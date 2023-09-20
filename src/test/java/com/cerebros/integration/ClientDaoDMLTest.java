package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

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

		assertThrows(ClientAlreadyExistsException.class, () -> dao.register(client2, "123456"));
	}

}
