package com.sungeon.bos.expand;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 刘国帅
 * @date 2019-10-9
 * @description 用以mysql中json格式的字段，进行转换的自定义转换器，转换为实体类的JSONArray属性
 **/
@MappedTypes(JSONArray.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JSONArrayTypeHandler extends BaseTypeHandler<JSONArray> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, String.valueOf(parameter.toString()));
	}

	@Override
	public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String sqlJson = rs.getString(columnName);
		if (null != sqlJson) {
			return JSONArray.parseArray(sqlJson);
		}
		return null;
	}

	@Override
	public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String sqlJson = rs.getString(columnIndex);
		if (null != sqlJson) {
			return JSONArray.parseArray(sqlJson);
		}
		return null;
	}

	@Override
	public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String sqlJson = cs.getString(columnIndex);
		if (null != sqlJson) {
			return JSONArray.parseArray(sqlJson);
		}
		return null;
	}

}
