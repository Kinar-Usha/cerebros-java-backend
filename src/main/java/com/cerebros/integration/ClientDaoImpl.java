package com.cerebros.integration;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public void register(Client client, String password) {
		// Verify Email
		boolean isEmailUnregistered = !emailExists(client.getPerson().getEmail());



		if (!isEmailUnregistered) {
			throw new ClientAlreadyExistsException("User with this email is already registered");
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