package com.xy.dash.factory.handler;

import com.xy.dash.entity.DataSources;
import com.xy.dash.enums.DataSourceType;
import com.xy.dash.factory.DataBaseHandler;
import com.xy.dash.vo.FieldVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 15:10
 * @Description
 **/
@Component
public class SqlServerDataBaseHandler implements DataBaseHandler {

    @Override
    public String type() {
        return DataSourceType.SQL_SERVER.getValue();
    }

    @Override
    public boolean connect(DataSources dataSources) {
        return false;
    }

    @Override
    public List<String> dataBase(DataSources detail) {
        return null;
    }

    @Override
    public List<String> queryTable(DataSources detail, String dataName) {
        return null;
    }

    @Override
    public List<FieldVO> queryField(DataSources detail, String talbeName, String databaseName) {
        return null;
    }
}
