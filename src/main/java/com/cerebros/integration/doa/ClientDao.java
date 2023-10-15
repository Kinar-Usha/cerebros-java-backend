package com.cerebros.integration.doa;

import com.cerebros.models.Cash;
import com.cerebros.models.Client;
import com.cerebros.models.Preferences;

import java.math.BigDecimal;

public interface ClientDao {

	boolean emailExists(String email);

	boolean clientIdExists(String clientId);

	int register(Client client, String password);

	boolean login(String email, String password);

	Client getClient(String clientId);

	Client getClientByEmail(String email);

	int addClientPreferences(Preferences preferences, String clientId);

	int updateClientPreferences(Preferences preferences, String clientId);

	Preferences getClientPreferences(String clientId);

	Cash getCashRemaining(String clientId);
	int insertCash(String clientId, BigDecimal cash);

}
