package com.cerebros.integration.doa.impl;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.mapper.ClientMapper;
import com.cerebros.integration.mapper.PreferencesMapper;
import com.cerebros.integration.doa.ClientDao;
import com.cerebros.models.Cash;
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
	public boolean clientIdExists(String clientId) {
		// Return true if clientId exists in DB
		if (getClient(clientId) != null) {
			return true;
		}

		return false;
	}

	@Override
	public int register(Client client, String password) {
		int numClientRowsUpdated = 0;

		// Insert Into Client
		try {
			numClientRowsUpdated = mapper.insertClient(client);
			logger.debug("Inserted {} client rows", numClientRowsUpdated);
		} catch (Exception e) {
			try {
				throw e.getCause();
			} catch (SQLIntegrityConstraintViolationException e1) {
				logger.error("Failed to insert row as client email already exists", e1);
				throw new ClientAlreadyExistsException("Client is already registered");
			} catch (Throwable e2) {
				throw new DatabaseException("insert failed", e2);
			}
		}
		insertCash(client.getClientId(),new BigDecimal("100"));
		;

		// Insert Client Identifications
		for (ClientIdentification cid : client.getClientIdentifications()) {
			// insertClientIdentification(client.getClientId(), cid);
			try {
				int numClientIdRowsUpdated = mapper.insertClientIndentification(client.getClientId(), cid);
				logger.debug("Inserted {} client identification rows", numClientIdRowsUpdated);
			} catch (Exception e) {
				try {
					throw e.getCause();
				} catch (SQLIntegrityConstraintViolationException e1) {
					mapper.deleteCash(client.getClientId());
					mapper.deleteClientFromClient(client.getClientId());
					logger.error("Failed to insert row as client identification already exists", e1);
					throw new ClientAlreadyExistsException("Client is already registered");
				} catch (Throwable e2) {
					logger.error("Failed to insert row in client identification", e2);
					throw new DatabaseException("Failed to insert row in client identification", e2);
				}
			}
			;
		}

		// Insert Into Client_Passwords
		try {
			int numClientPwdRowsUpdated = mapper.insertClientPassword(client.getClientId(), password);
			logger.debug("Inserted {} client password rows", numClientPwdRowsUpdated);
		} catch (Exception e) {
			try {
				throw e.getCause();
			} catch (SQLIntegrityConstraintViolationException e1) {
				logger.error("Failed to insert row as client email already exists", e1);
				throw new ClientAlreadyExistsException("Client is already registered");
			} catch (Throwable e2) {
				throw new DatabaseException("insert failed", e2);
			}
		}
		;

		return numClientRowsUpdated;
	}

	@Override
	public boolean login(String email, String password) {
		int is_cred_valid = mapper.checkLoginCreds(email, password);

		return is_cred_valid == 1;
	}

	@Override
	public Client getClient(String clientId) {
		Client client = mapper.getClient(clientId);
		if (client == null) {
			throw new ClientNotFoundException();
		}

		client.setClientIdentifications(getClientIdentifications(client.getClientId()));

		return client;
	}

	@Override
	public Client getClientByEmail(String email) {
		Client client = mapper.getClientByEmail(email);
		if (client == null) {
			return null;
		}

		client.setClientIdentifications(getClientIdentifications(client.getClientId()));
		return client;
	}

	@Override
	public Set<ClientIdentification> getClientIdentifications(String clientId) {
		Set<ClientIdentification> clientIdentifications = mapper.getClientIdentifications(clientId);
		return clientIdentifications;
	}

	@Override
	public int addClientPreferences(Preferences preferences, String clientId) {
		if (preferences == null) {
			throw new NullPointerException("Prferences cannot be null");
		}
		if (clientId == "") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		int result = 0;

		result = preferenceMapper.addClientPreferences(preferences, clientId);
		if (result == 0) {
			throw new DatabaseException("Insert failed");
		}
		return result;

	}

	@Override
	public int updateClientPreferences(Preferences preferences, String clientId) {
		if (preferences == null) {
			throw new NullPointerException("Prferences cannot be null");
		}
		if (clientId == "") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		int result = preferenceMapper.updateClientPreferences(preferences, clientId);
		if (result == 0) {
			throw new DatabaseException("Client Preference Update failed");
		}

		return result;

	}

	@Override
	public Preferences getClientPreferences(String clientId) {
		if (clientId == "") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		Preferences pref = null;
		pref = preferenceMapper.getClientPreferecesById(clientId);
		if (pref == null) {
			throw new DatabaseException("Client not found");
		}
		return pref;
	}

	@Override
	public Cash getCashRemaining(String clientId) {
		if (clientId == "") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		Cash cash = null;
		cash = mapper.getCashRemaining(clientId);
		if (cash == null) {
			throw new DatabaseException("Client not found");
		}
		return cash;
	}

	@Override
	public int insertCash(String clientId, BigDecimal cash) {
		if (clientId == "") {
			throw new IllegalArgumentException("Client ID cannot be null");
		}
		Map<String, Object> cashMap = new HashMap<>();
		cashMap.put("clientId", clientId);
		cashMap.put("cashRemaining", cash);
		return mapper.insertCash(cashMap);
	}
}