package com.xy.dash.factory;

import com.xy.dash.entity.DataSources;
import com.xy.dash.vo.FieldVO;

import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 15:09
 * @Description
 **/
public interface DataBaseHandler {
    String type();

    boolean connect(DataSources dataSources);

    List<String> dataBase(DataSources detail);

    List<String> queryTable(DataSources detail, String dataName);

    List<FieldVO> queryField(DataSources detail, String talbeName, String databaseName);
}
