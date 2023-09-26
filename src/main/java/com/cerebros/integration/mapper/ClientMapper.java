package com.cerebros.integration.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;

public interface ClientMapper {

	Client getClientByEmail(@Param("email") String email);

	Client getClient(@Param("clientId") String clientId);

	int checkLoginCreds(@Param("email") String email, @Param("password") String password);

	int insertClient(Client client);

	int insertClientIndentification(@Param("clientId") String clientId, @Param("cid") ClientIdentification cid);

	@Delete("DELETE FROM CEREBROS_CLIENT WHERE CLIENTID = #{clientId}")
	int deleteClientFromClient(@Param("clientId") String clientId);

	int insertClientPassword(@Param("clientId") String clientId, @Param("password") String password);

}
