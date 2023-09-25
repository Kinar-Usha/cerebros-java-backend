package com.cerebros.constants;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ClientIdentificationType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ClientIdentificationTypeHandler extends BaseTypeHandler<ClientIdentificationType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ClientIdentificationType parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getType());
	}

	@Override
	public ClientIdentificationType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String type = rs.getString(columnName);
		return ClientIdentificationType.of(type);
	}

	@Override
	public ClientIdentificationType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String type = rs.getString(columnIndex);
		return ClientIdentificationType.of(type);
	}

	@Override
	public ClientIdentificationType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String type = cs.getString(columnIndex);
		return ClientIdentificationType.of(type);
	}
}