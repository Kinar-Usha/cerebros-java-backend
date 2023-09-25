package com.cerebros.constants;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Country.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class CountryTypeHandler extends BaseTypeHandler<Country> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Country parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getCode());
	}

	@Override
	public Country getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String code = rs.getString(columnName);
		if (code != null) {
			return Country.of(code);
		} else {
			return null;
		}
	}

	@Override
	public Country getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String code = rs.getString(columnIndex);
		if (code != null) {
			return Country.of(code);
		} else {
			return null;
		}
	}

	@Override
	public Country getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String code = cs.getString(columnIndex);
		if (code != null) {
			return Country.of(code);
		} else {
			return null;
		}
	}
}