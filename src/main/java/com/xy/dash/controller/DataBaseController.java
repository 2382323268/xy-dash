package com.xy.dash.controller;

import com.xy.dash.entity.DataSources;
import com.xy.dash.enums.DataSourceType;
import com.xy.dash.factory.DataBaseHandler;
import com.xy.dash.factory.DataBaseMapping;
import com.xy.dash.service.DataSourcesService;
import com.xy.dash.utli.api.R;
import com.xy.dash.vo.FieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 15:36
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/dataBase")
@Api(value = "数据库", tags = "数据库", description = "")
public class DataBaseController {

    private final DataBaseMapping handlers;

    @Autowired
    private DataSourcesService dataSourcesService;

    @Autowired
    public DataBaseController(List<DataBaseHandler> dataBaseHandlers) {
        handlers = new DataBaseMapping(dataBaseHandlers);
    }

    @GetMapping("/{dataSourceId}")
    @ApiOperation(value = "查询连接下的所有库")
    public List<String> dataBase(@PathVariable Long dataSourceId) {
        DataSources detail = dataSourcesService.detail(dataSourceId);
        DataBaseHandler dataBaseHandler = handlers.get(DataSourceType.valueByType(detail.getType()));
        return dataBaseHandler.dataBase(detail);
    }

    @GetMapping("/{dataSourceId}/connect")
    @ApiOperation(value = "连接数据库")
    public R connect(@PathVariable Long dataSourceId) {
        DataSources detail = dataSourcesService.detail(dataSourceId);
        DataBaseHandler dataBaseHandler = handlers.get(DataSourceType.valueByType(detail.getType()));
        return R.status(dataBaseHandler.connect(detail));
    }

    @PostMapping("/connect")
    @ApiOperation(value = "连接数据库")
    public R connect(@RequestBody DataSources dataSources) {
        DataBaseHandler dataBaseHandler = handlers.get(DataSourceType.valueByType(dataSources.getType()));
        return R.status(dataBaseHandler.connect(dataSources));
    }

    @GetMapping("/queryTable/{dataSourceId}")
    @ApiOperation(value = "查询库下所有表")
    public List<String> queryTable(@PathVariable Long dataSourceId, @RequestParam String dataName) {
        DataSources detail = dataSourcesService.detail(dataSourceId);
        DataBaseHandler dataBaseHandler = handlers.get(DataSourceType.valueByType(detail.getType()));
        return dataBaseHandler.queryTable(detail, dataName);
    }

    @GetMapping("/queryField/{dataSourceId}")
    @ApiOperation(value = "查询表下字段属性")
    public List<FieldVO> queryField(@PathVariable Long dataSourceId, @RequestParam String talbeName, @RequestParam String databaseName) {
        DataSources detail = dataSourcesService.detail(dataSourceId);
        DataBaseHandler dataBaseHandler = handlers.get(DataSourceType.valueByType(detail.getType()));
        return dataBaseHandler.queryField(detail, talbeName,databaseName);
    }
}
