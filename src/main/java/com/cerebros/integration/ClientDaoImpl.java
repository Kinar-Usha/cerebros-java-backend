package com.cerebros.integration;



import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;



import javax.sql.DataSource;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.UserNotFoundException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;



public class ClientDaoImpl implements ClientDao {



	private DataSource dataSource;
	private final Logger logger = LoggerFactory.getLogger(getClass());



	public ClientDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}



	@Override
	public boolean emailExists(String email) {
		// Return true if email exists in DB
		if (getClientByEmail(email) != null) {
			return true;
		}



		return false;
	}



	@Override
	public void register(Client client, String password) throws SQLException {
		// Insert Into Client

		String clientSql = """
				INSERT INTO
					CEREBROS_CLIENT (CLIENTID, NAME, EMAIL, DOB, POSTALCODE, COUNTRY)
				VALUES
					(?, ?, ?, ?, ?, ?)
				""";

		String pwdSQl = """
				INSERT INTO
					CEREBROS_CLIENTPASSWORDS (CLIENTID, PASSWORDHASH)
				VALUES
					(?, ?)
				""";

		try (Connection conn = dataSource.getConnection();
				PreparedStatement clientStmt = conn.prepareStatement(clientSql);
				PreparedStatement pwdStmt = conn.prepareStatement(pwdSQl);) {

			// Insert Client
			clientStmt.setString(1, client.getClientId());
			clientStmt.setString(2, client.getPerson().getName());
			clientStmt.setString(3, client.getPerson().getEmail());
			clientStmt.setDate(4, Date.valueOf(client.getPerson().getDateofBirth()));
			clientStmt.setString(5, client.getPerson().getPostalCode());
			clientStmt.setString(6, client.getPerson().getCountry().getCode());

			int numClientRowsUpdated = clientStmt.executeUpdate();
			logger.debug("Inserted {} client rows", numClientRowsUpdated);

			// Insert Client Identifications
			for (ClientIdentification cid : client.getClientIdentifications()) {
				insertClientIdentification(client.getClientId(), cid);
			}

			// Insert Into Client_Passwords
			pwdStmt.setString(1, client.getClientId());
			pwdStmt.setString(2, password);

			int numPwdRowsUpdated = pwdStmt.executeUpdate();
			logger.debug("Inserted {} client password rows", numPwdRowsUpdated);

		} catch (SQLIntegrityConstraintViolationException e) {
			logger.error("Failed to insert row as client email already exists", e);
			throw new ClientAlreadyExistsException("Client is already registered");
		} catch (SQLException e) {
			logger.error("Failed to insert row", e);
			throw new DatabaseException("Failed to insert row", e);
		}

	}

	private void insertClientIdentification(String clientId, ClientIdentification clientIdentification)
			throws SQLIntegrityConstraintViolationException {
		String idSql = """
				INSERT INTO
					CEREBROS_CLIENTIDENTIFICATIONS (CLIENTID, IDTYPE, IDNUMBER)
				VALUES
					(?, ?, ?)
				""";

		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(idSql);) {

			// Insert Client Identification
			stmt.setString(1, clientId);
			stmt.setString(2, clientIdentification.getType().getType());
			stmt.setString(3, clientIdentification.getValue());

			int numRowsUpdated = stmt.executeUpdate();
			logger.debug("Inserted {} client identification rows", numRowsUpdated);

		} catch (SQLIntegrityConstraintViolationException e) {
			deleteClientFromClient(clientId);
			logger.error("Failed to insert row as client identification already exists", e);
			throw new ClientAlreadyExistsException("Client is already registered");
		} catch (SQLException e) {
			logger.error("Failed to insert row in client identification", e);
			throw new DatabaseException("Failed to insert row in client identification", e);
		}

	}

	private void deleteClientFromClient(String clientId) {
		String sql = "DELETE FROM CEREBROS_CLIENT WHERE CLIENTID = ?";

		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

//			stmt.setString(1, table);
			stmt.setString(1, clientId);

			int numRowsUpdated = stmt.executeUpdate();
			logger.debug("Deleted {} row", numRowsUpdated);
			if (numRowsUpdated != 1) {
				logger.error("DeleteClient: {} rows were updated. Check whether the row exists.", numRowsUpdated);
				throw new DatabaseException(
						String.format("DeleteClient: Only 1 row was supposed to be affected but {} rows were affected",
								numRowsUpdated));
			}

		} catch (SQLException e) {
			logger.error("Failed to delete row", e);
			throw new DatabaseException("Failed to delete row", e);
		}
	}



	@Override
	public boolean login(String email, String password) {
		String sql = """
				SELECT
					COUNT(*) IS_CRED_VALID
				FROM
				    CEREBROS_CLIENTPASSWORDS
				WHERE
					clientId = (SELECT clientID FROM CEREBROS_CLIENT WHERE email = ?) AND passwordHash = ?
				   """;



		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();



			while (rs.next()) {
				int is_cred_valid = rs.getInt("IS_CRED_VALID");
				return is_cred_valid == 1;
			}



		} catch (Exception e) {
			logger.error("Something went wrong while checking password for login", e);
			throw new DatabaseException("Something went wrong while checking password for login", e);
		}



		return false;
	}



	@Override
	public Client getClient(String clientId) {
		String sql = """
				SELECT
				    NAME ,
				    EMAIL ,
				    DOB ,
				    POSTALCODE ,
				    COUNTRY
				FROM
				    CEREBROS_CLIENT
				WHERE
					CLIENTID = ?
				   """;
		Person person = null;
		Client client = null;



		// TODO Get Client Identifications
		ClientIdentification clientIdentification = null;
		Set<ClientIdentification> ClientIdentifications = new HashSet<ClientIdentification>();



		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, clientId);
			ResultSet rs = stmt.executeQuery();



			if (!rs.isBeforeFirst()) {
				throw new UserNotFoundException();
			}



			while (rs.next()) {
				logger.debug(clientId + " " + rs.getString("NAME"));



				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				LocalDate dob = rs.getDate("DOB").toLocalDate();
				String postalCode = rs.getString("POSTALCODE");
				String countryStr = rs.getString("COUNTRY");
				Country country = Country.of(countryStr);



				person = new Person(name, email, dob, country, postalCode);



				client = new Client(clientId, person);
			}



		} catch (UserNotFoundException ue) {
			logger.info("Client with id {} doesn't exist", clientId);
			throw new UserNotFoundException("Client not found with id: " + clientId);



		} catch (Exception e) {
			logger.error("Cannot get client with clientId", e);
			throw new DatabaseException("Cannot get client with clientId", e);
		}



		return client;
	}



	@Override
	public Client getClientByEmail(String email) {
		String sql = """
				SELECT
					CLIENTID ,
				    NAME ,
				    DOB ,
				    POSTALCODE ,
				    COUNTRY
				FROM
				    CEREBROS_CLIENT
				WHERE
					EMAIL = ?
				   """;
		Person person = null;
		Client client = null;



		// TODO Get Client Identifications
		ClientIdentification clientIdentification = null;
		Set<ClientIdentification> ClientIdentifications = new HashSet<ClientIdentification>();



		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();



			if (!rs.isBeforeFirst()) {
				logger.info("Client with email {} doesn't exist", email);
				return null;
			}



			while (rs.next()) {
				logger.debug(email + " " + rs.getString("NAME"));



				String clientId = rs.getString("CLIENTID");
				String name = rs.getString("NAME");
				LocalDate dob = rs.getDate("DOB").toLocalDate();
				String postalCode = rs.getString("POSTALCODE");
				String countryStr = rs.getString("COUNTRY");
				Country country = Country.of(countryStr);



				person = new Person(name, email, dob, country, postalCode);



				client = new Client(clientId, person);
			}



		} catch (Exception e) {
			logger.error("Cannot get client with clientId", e);
			throw new DatabaseException("Cannot get client with clientId", e);
		}



		return client;



	}



	@Override
	public void addClientPreferences(Preferences preferences) {
		// TODO Auto-generated method stub



	}



	@Override
	public void updateClientPreferences(Preferences preferences) {
		// TODO Auto-generated method stub



	}



	@Override
	public Preferences getClientPreferences(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}



}