package com.xy.dash.controller;


import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xy.dash.entity.AdminMenu;
import com.xy.dash.entity.DataSources;
import com.xy.dash.entity.Migrations;
import com.xy.dash.enums.MenuType;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.api.R;
import com.xy.dash.vo.AdminMenuVO;
import com.xy.dash.vo.DataSourcesReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import com.xy.dash.service.DataSourcesService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据源表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/dataSources")
@Api(value = "数据源配置", tags = "数据源配置", description = "")
public class DataSourcesController {

    @Autowired
    private DataSourcesService dataSourcesService;

    @GetMapping("/{id}")
    @ApiOperation(value = "详情")
    public DataSources detail(@PathVariable Long id) throws InterruptedException {
        return dataSourcesService.detail(id);
    }


    @GetMapping("/list")
    @ApiOperation(value = "查询列表")
    public List<DataSources> list() {
        return dataSourcesService.list(Wrappers.<DataSources>lambdaQuery().select(DataSources::getId, DataSources::getName, DataSources::getType));
    }

    @GetMapping("")
    @ApiOperation(value = "查询")
    public IPage<DataSources> page(@ApiIgnore @RequestParam Map<String, Object> param, Query query) throws InterruptedException {
        return dataSourcesService.queryPage(param, query);
    }

    @PostMapping("")
    @ApiOperation(value = "保存")
    public R save(@Valid @RequestBody DataSourcesReq dataSourcesReq) {
        return R.status(dataSourcesService.save(dataSourcesReq));
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "编辑")
    public R update(@PathVariable("id") Long id, @Valid @RequestBody DataSourcesReq dataSourcesReq) {
        dataSourcesReq.setId(id);
        return R.status(dataSourcesService.updateById(dataSourcesReq));
    }

    @PostMapping("/removes")
    @ApiOperation(value = "逻辑删除")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(dataSourcesService.deleteLogic(Arrays.asList(ids.split(","))));
    }
}
