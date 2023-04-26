package com.xy.dash.config.handler;

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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leif
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringToListLongTypeHandler extends BaseTypeHandler<List<Long>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List list, JdbcType jdbcType) throws SQLException {
        String str = Joiner.on(",").skipNulls().join(list);
        preparedStatement.setString(i, str);
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return this.stringToList(resultSet.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return this.stringToList(resultSet.getString(i));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return this.stringToList(callableStatement.getString(i));
    }

    private List<Long> stringToList(String str) {
        return Strings.isNullOrEmpty(str) ? new ArrayList<>() : Splitter.on(",")
                .splitToList(str).stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
