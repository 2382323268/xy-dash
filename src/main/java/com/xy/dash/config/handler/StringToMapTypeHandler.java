package com.xy.dash.config.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leif
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringToMapTypeHandler extends BaseTypeHandler<Map<String, String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Map<String, String> map, JdbcType jdbcType) throws SQLException {
        String str = JSON.toJSONString(map);
        preparedStatement.setString(i, str);
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return this.stringToMap(resultSet.getString(columnName));
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return this.stringToMap(resultSet.getString(i));
    }

    @Override
    public Map<String, String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return this.stringToMap(callableStatement.getString(i));
    }

    private Map<String, String> stringToMap(String str) {
        return Strings.isNullOrEmpty(str) ? new HashMap<>() : JSON.parseObject(str, Map.class);
    }
}
