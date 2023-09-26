package com.cerebros.integration;

import org.apache.ibatis.annotations.Param;

import com.cerebros.models.Preferences;

public interface PreferencesMapper {
	Preferences getClientPreferecesById(String clientId);

	int addClientPreferences(@Param("preferences")Preferences preferences, @Param("clientId") String clientId);

	void updateClientPreferences(@Param("preferences")Preferences preferences,@Param("clientId") String clientId);
	

}