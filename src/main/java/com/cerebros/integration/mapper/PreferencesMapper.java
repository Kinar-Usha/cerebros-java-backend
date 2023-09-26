package com.cerebros.integration.mapper;

import org.apache.ibatis.annotations.Param;

import com.cerebros.models.Preferences;

public interface PreferencesMapper {
	Preferences getClientPreferecesById(String clientId);

	int addClientPreferences(@Param("preferences")Preferences preferences, @Param("clientId") String clientId);

	int updateClientPreferences(@Param("preferences")Preferences preferences,@Param("clientId") String clientId);
	

}