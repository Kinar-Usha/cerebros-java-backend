package com.cerebros.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cerebros.models.Client;
import com.cerebros.models.Preferences;

public class ClientDaoImpl implements ClientDao {

	private DataSource dataSource;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ClientDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean emailExists(String email) {
		// TODO Return true if email exists in DB
		String sql = """
				SELECT
				    clientId
				FROM
				    cerebros_client
				WHERE
					email = ?
				   """;
		boolean emailAlreadyExists = false;

		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.isBeforeFirst()) {
				logger.info("Client with email {} already exists", email);
				emailAlreadyExists = true;
			}
		} catch (Exception e) {
			logger.error("Cannot get client with email", e);
			throw new DatabaseException("Cannot get client with email", e);
		}

		return emailAlreadyExists;
	}

	@Override
	public void register(Client client, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean login(String email, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Client getClient(String clientId) {
		// TODO Auto-generated method stub
		return null;
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
