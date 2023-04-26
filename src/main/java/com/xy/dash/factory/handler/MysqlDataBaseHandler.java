package com.xy.dash.factory.handler;

import com.xy.dash.converts.DataSourceConvert;
import com.xy.dash.entity.DataSources;
import com.xy.dash.enums.DataSourceType;
import com.xy.dash.factory.DataBaseHandler;
import com.xy.dash.utli.exception.ServiceException;
import com.xy.dash.vo.FieldVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 15:10
 * @Description
 **/
@Component
@Slf4j
public class MysqlDataBaseHandler implements DataBaseHandler {

    private final String DRUVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://%s";

    @Override
    public String type() {
        return DataSourceType.MYSQL.getValue();
    }

    @Override
    public boolean connect(DataSources dataSources) {
        try {
            Connection connection = getConnection(dataSources);
            connection.close();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> dataBase(DataSources detail) {
        List<String> list = new ArrayList<>();
        try {
            Connection connection = getConnection(detail);
            ResultSet schemas = connection.getMetaData().getCatalogs();
            while (schemas.next()) {
                list.add(schemas.getObject("TABLE_CAT").toString());
            }
            connection.close();
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<String> queryTable(DataSources detail, String dataName) {
        List<String> list = new ArrayList<>();
        try {
            Connection connection = getConnection(detail, dataName);
            ResultSet tables = connection.getMetaData().getTables(null, null, null, null);
            while (tables.next()) {
                if (tables.getString("TABLE_CAT").equals(dataName)) {
                    String name = tables.getString("TABLE_NAME");
                    list.add(name);
                }
            }
            connection.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<FieldVO> queryField(DataSources detail, String talbeName, String databaseName) {
        try {
            List<FieldVO> list = new ArrayList<>();
            Connection connection = getConnection(detail);
            ResultSet tables = connection.getMetaData().getColumns(databaseName, null, talbeName, null);
            ResultSet primaryKeyResultSet = connection.getMetaData().getPrimaryKeys(databaseName, null, talbeName);
            String key = null;
            while (primaryKeyResultSet.next()) {
                key = primaryKeyResultSet.getString("COLUMN_NAME");
            }
            while (tables.next()) {
                String typeName = tables.getString("TYPE_NAME");
                FieldVO build = FieldVO.builder()
                        .status(key.equals(tables.getString("COLUMN_NAME")) ? 1 : 0)
                        .size(tables.getString("COLUMN_SIZE"))
                        .remark(tables.getString("REMARKS"))
                        .type(typeName)
                        .javaType(DataSourceConvert.getDbColumnType(detail.getType(), typeName).getType())
                        .def(tables.getString("COLUMN_DEF"))
                        .isNull(tables.getString("IS_NULLABLE"))
                        .name(tables.getString("COLUMN_NAME")).build();
                list.add(build);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Connection getConnection(DataSources dataSources) throws Exception {
        return getConnection(dataSources, null);
    }

    private Connection getConnection(DataSources dataSources, String dataName) throws Exception {
        Class.forName(DRUVER);
        Connection connection = DriverManager.getConnection(String.format(URL, dataSources.getIp(), dataName == null ? "" : dataName),
                dataSources.getUserName(),
                dataSources.getPassword());
        return connection;
    }
}
