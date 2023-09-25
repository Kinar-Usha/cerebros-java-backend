package com.cerebros.integration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Preferences;

@Repository
@Primary
public class ClientDaoImpl implements ClientDao {
	
	@Autowired
	private PreferencesMapper preferenceMapper;

	private DataSource dataSource;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ClientMapper mapper;

	public ClientDaoImpl() {
	}

	public ClientDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean emailExists(String email) {
		logger.debug(email);
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
		int is_cred_valid = mapper.checkLoginCreds(email, password);

		return is_cred_valid == 1;

//		String sql = """
//				SELECT
//					COUNT(*) IS_CRED_VALID
//				FROM
//				    CEREBROS_CLIENTPASSWORDS
//				WHERE
//					clientId = (SELECT clientID FROM CEREBROS_CLIENT WHERE email = ?) AND passwordHash = ?
//				   """;
//
//		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//			stmt.setString(1, email);
//			stmt.setString(2, password);
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				int is_cred_valid = rs.getInt("IS_CRED_VALID");
//				return is_cred_valid == 1;
//			}
//
//		} catch (Exception e) {
//			logger.error("Something went wrong while checking password for login", e);
//			throw new DatabaseException("Something went wrong while checking password for login", e);
//		}
//
//		return false;
	}

	@Override
	public Client getClient(String clientId) {
		Client client = mapper.getClient(clientId);
		if (client == null) {
			throw new ClientNotFoundException();
		}

		return client;
	}

	@Override
	public Client getClientByEmail(String email) {
		Client client = mapper.getClientByEmail(email);
		return client;
	}

	@Override
	public int addClientPreferences(Preferences preferences,String clientId) {
		if(preferences==null) {
			throw new NullPointerException("Prferences cannot be null");
		}
		if(clientId=="") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		int result=0;
		
		result= preferenceMapper.addClientPreferences(preferences,clientId);
		if(result==0) {
			throw new DatabaseException("Insert failed");
		}
		return result;


	}



	@Override
	public void updateClientPreferences(Preferences preferences,String clientId) {
		if(preferences==null) {
			throw new NullPointerException("Prferences cannot be null");
		}
		if(clientId=="") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		preferenceMapper.updateClientPreferences(preferences,clientId);



	}
	
	@Override
	public Preferences getClientPreferences(String clientId) {
		return preferenceMapper.getClientPreferecesById(clientId);
	}

	
}