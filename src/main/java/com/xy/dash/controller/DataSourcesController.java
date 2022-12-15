package com.xy.dash.controller;


import org.springframework.stereotype.Controller;
import com.xy.dash.service.DataSourcesService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 数据源表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Controller
@Api(value = "", tags = "", description="")
public class DataSourcesController {

    @Autowired
    private DataSourcesService dataSourcesService;
}
